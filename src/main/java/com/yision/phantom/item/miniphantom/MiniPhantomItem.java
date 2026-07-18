package com.yision.phantom.item.miniphantom;

import com.simibubi.create.content.logistics.box.PackageItem;
import com.yision.phantom.entity.courier.AirCourierEntity;
import com.yision.phantom.logistics.courier.AirCourierReturnMode;
import com.yision.phantom.registry.AllItems;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MiniPhantomItem extends Item {
	private static final int EMPTY_CARRIER_MAX_STACK_SIZE = 64;
	private static final String CARGO_KEY = "Cargo";
	private static final String HEADING_KEY = "Heading";
	private static final String HUD_ID_KEY = "HudEntryId";
	private static final String RETURN_TARGET_KEY = "ReturnTarget";
	private static final String PLAYER_RETURN_TARGET_KEY = "PlayerReturnTarget";
	private static final String RETURN_MODE_KEY = "ReturnMode";

	public MiniPhantomItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return isPlainCarrier(stack) ? EMPTY_CARRIER_MAX_STACK_SIZE : 1;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			openMenu(serverPlayer, stack, usedHand);
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		if (!hasCargo(stack) || context.getClickedFace() != Direction.UP) {
			return InteractionResult.PASS;
		}

		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}

		Vec3 spawnPos = Vec3.atBottomCenterOf(context.getClickedPos().above()).add(0, 0.01, 0);
		Vec3 facingDirection = player.getLookAngle().multiply(1, 0, 1);
		if (facingDirection.lengthSqr() < 1.0E-6) {
			facingDirection = Vec3.directionFromRotation(0, player.getYRot()).multiply(-1, 0, -1);
		}
		facingDirection = facingDirection.normalize();
		AirCourierEntity courier = AirCourierEntity.createWaiting(level, copyCargoPackage(stack), facingDirection);
		courier.setPos(spawnPos);

		UUID hudEntryId = getHudEntryId(stack);
		if (hudEntryId != null) {
			courier.setHudEntryId(hudEntryId);
		}

		if (!level.noCollision(courier, courier.getBoundingBox())) {
			return InteractionResult.FAIL;
		}

		if (!level.isClientSide()) {
			level.addFreshEntity(courier);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
		TooltipFlag tooltipFlag) {
		ItemStack cargoPackage = copyCargoPackage(stack);
		if (!cargoPackage.isEmpty()) {
			cargoPackage.getItem().appendHoverText(cargoPackage, context, tooltipComponents, tooltipFlag);
		}
	}

	protected static void openMenu(ServerPlayer serverPlayer, ItemStack stack, InteractionHand usedHand) {
		PlayerMiniPhantomClipboardInventory clipboard = PlayerMiniPhantomClipboardInventory.get(serverPlayer);
		serverPlayer.openMenu(
			new SimpleMenuProvider((id, inv, p) -> MiniPhantomMenu.create(id, inv, stack, usedHand),
				Component.translatable("item.create_biotech.mini_phantom")),
			buffer -> {
				ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, stack);
				buffer.writeEnum(usedHand);
				ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, clipboard.getStackInSlot(0));
				buffer.writeUtf(clipboard.getAddress());
			});
	}

	public static ItemStack createLoaded(ItemStack packageStack) {
		ItemStack phantom = AllItems.MINI_PHANTOM.asStack();
		loadCargo(phantom, packageStack);
		return phantom;
	}

	public static ItemStack createLoadedWithHeading(ItemStack packageStack, int headingAngle) {
		ItemStack phantom = createLoaded(packageStack);
		setHeadingAngle(phantom, headingAngle);
		return phantom;
	}

	public static boolean loadCargo(ItemStack phantom, ItemStack packageStack) {
		MiniPhantomCargo cargo = new MiniPhantomCargo(packageStack);
		if (!cargo.isValid()) {
			remove(phantom, CARGO_KEY);
			return false;
		}

		phantom.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(cargo.packageCopy())));
		remove(phantom, CARGO_KEY);
		return true;
	}

	public static ItemStack copyCargoPackage(ItemStack phantom) {
		ItemStack packageStack = phantom.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
		return PackageItem.isPackage(packageStack) ? packageStack : ItemStack.EMPTY;
	}

	public static boolean hasCargo(ItemStack phantom) {
		return !copyCargoPackage(phantom).isEmpty();
	}

	public static boolean isPlainCarrier(ItemStack stack) {
		return stack.is(AllItems.MINI_PHANTOM.get())
			&& !hasCargo(stack)
			&& getReturnTarget(stack).isEmpty()
			&& getPlayerReturnTarget(stack).isEmpty()
			&& !hasReturnMode(stack)
			&& !hasHudEntryId(stack);
	}

	public static void clearCargo(ItemStack phantom) {
		phantom.remove(DataComponents.CONTAINER);
		remove(phantom, CARGO_KEY);
	}

	public static void setHeadingAngle(ItemStack stack, int headingAngle) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(HEADING_KEY, Math.floorMod(headingAngle, 360)));
	}

	public static boolean hasHeadingAngle(ItemStack stack) {
		return customData(stack).contains(HEADING_KEY, Tag.TAG_INT);
	}

	public static int getHeadingAngle(ItemStack stack) {
		return Math.floorMod(customData(stack).getInt(HEADING_KEY), 360);
	}

	public static void setHudEntryId(ItemStack stack, UUID hudEntryId) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(HUD_ID_KEY, hudEntryId));
	}

	@Nullable
	public static UUID getHudEntryId(ItemStack stack) {
		CompoundTag tag = customData(stack);
		return tag.hasUUID(HUD_ID_KEY) ? tag.getUUID(HUD_ID_KEY) : null;
	}

	public static boolean hasHudEntryId(ItemStack stack) {
		return customData(stack).hasUUID(HUD_ID_KEY);
	}

	public static void clearHudEntryId(ItemStack stack) {
		remove(stack, HUD_ID_KEY);
	}

	public static ItemStack returningTo(ResourceKey<Level> dimension, BlockPos pos) {
		ItemStack stack = AllItems.MINI_PHANTOM.asStack();
		setReturnTarget(stack, dimension, pos);
		return stack;
	}

	public static ItemStack returningToPlayer(UUID playerId) {
		ItemStack stack = AllItems.MINI_PHANTOM.asStack();
		setPlayerReturnTarget(stack, playerId);
		return stack;
	}

	public static void setReturnTarget(ItemStack stack, ResourceKey<Level> dimension, BlockPos pos) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag ->
			tag.put(RETURN_TARGET_KEY, new MiniPhantomReturnTarget(dimension, pos.immutable()).write()));
		remove(stack, PLAYER_RETURN_TARGET_KEY);
	}

	public static Optional<MiniPhantomReturnTarget> getReturnTarget(ItemStack stack) {
		CompoundTag tag = customData(stack);
		if (!tag.contains(RETURN_TARGET_KEY, Tag.TAG_COMPOUND)) {
			return Optional.empty();
		}
		return MiniPhantomReturnTarget.read(tag.getCompound(RETURN_TARGET_KEY));
	}

	public static void setPlayerReturnTarget(ItemStack stack, UUID playerId) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(PLAYER_RETURN_TARGET_KEY, playerId));
		remove(stack, RETURN_TARGET_KEY);
	}

	public static Optional<UUID> getPlayerReturnTarget(ItemStack stack) {
		CompoundTag tag = customData(stack);
		return tag.hasUUID(PLAYER_RETURN_TARGET_KEY)
			? Optional.of(tag.getUUID(PLAYER_RETURN_TARGET_KEY))
			: Optional.empty();
	}

	public static void setReturnMode(ItemStack stack, AirCourierReturnMode mode) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(RETURN_MODE_KEY,
			(mode == null ? AirCourierReturnMode.DEFAULT_FOR_PORT : mode).serializedName()));
	}

	public static AirCourierReturnMode getReturnMode(ItemStack stack) {
		CompoundTag tag = customData(stack);
		if (tag.contains(RETURN_MODE_KEY, Tag.TAG_STRING)) {
			return AirCourierReturnMode.byName(tag.getString(RETURN_MODE_KEY));
		}
		return getReturnTarget(stack).isPresent()
			? AirCourierReturnMode.DEFAULT_FOR_PORT
			: AirCourierReturnMode.DEFAULT_FOR_PLAYER_LAUNCH;
	}

	public static boolean hasReturnMode(ItemStack stack) {
		return customData(stack).contains(RETURN_MODE_KEY, Tag.TAG_STRING);
	}

	private static CompoundTag customData(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	private static void remove(ItemStack stack, String key) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(key));
	}

}
