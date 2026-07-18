package com.yision.phantom.item.storagecard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlockEntity;
import com.simibubi.create.content.equipment.clipboard.ClipboardContent;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StorageChannelExtensionCardItem extends Item {

	private static final String NETWORK_TAG = "PhantomStorageChannelFreq";
	private static final String ADDRESSES_TAG = "PhantomStorageChannelAddresses";

	public StorageChannelExtensionCardItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack) {
		return isLinked(stack);
	}

	public static boolean isLinked(ItemStack stack) {
		return customData(stack).hasUUID(NETWORK_TAG);
	}

	@Nullable
	public static UUID networkFromStack(ItemStack stack) {
		CompoundTag tag = customData(stack);
		return tag.hasUUID(NETWORK_TAG) ? tag.getUUID(NETWORK_TAG) : null;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
		@NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);

		List<String> addresses = loadAddressesFromStack(stack);
		if (!addresses.isEmpty()) {
			tooltip.add(Component.translatable("item.create_biotech.storage_channel_extension_card.address_count",
				addresses.size()).withStyle(FontHelper.Palette.STANDARD_CREATE.highlight()));
		}

		if (!isLinked(stack))
			return;

		CreateLang.translate("logistically_linked.tooltip")
			.style(ChatFormatting.GOLD)
			.addTo(tooltip);
		CreateLang.translate("logistically_linked.tooltip_clear")
			.style(ChatFormatting.GRAY)
			.addTo(tooltip);
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null)
			return InteractionResult.FAIL;

		ItemStack stack = context.getItemInHand();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (level.getBlockEntity(pos) instanceof ClipboardBlockEntity clipboard) {
			if (level.isClientSide)
				return InteractionResult.SUCCESS;
			int written = saveAddressesFromClipboard(stack, clipboard.components()
				.getOrDefault(AllDataComponents.CLIPBOARD_CONTENT, ClipboardContent.EMPTY).pages());
			player.displayClientMessage(
				Component.translatable("item.create_biotech.storage_channel_extension_card.address_count", written),
				true);
			return InteractionResult.SUCCESS;
		}

		LogisticallyLinkedBehaviour link = BlockEntityBehaviour.get(level, pos, LogisticallyLinkedBehaviour.TYPE);
		if (link == null)
			return InteractionResult.PASS;

		if (level.isClientSide)
			return InteractionResult.SUCCESS;

		if (!link.mayInteractMessage(player))
			return InteractionResult.SUCCESS;

		UUID oldNetwork = networkFromStack(stack);
		UUID newNetwork = link.freqId;
		saveCategoriesIfAvailable(stack, level, pos, oldNetwork, newNetwork);
		assignFrequency(stack, player, newNetwork);
		return InteractionResult.SUCCESS;
	}

	public static void assignFrequency(ItemStack stack, Player player, UUID frequency) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(NETWORK_TAG, frequency));
		player.displayClientMessage(CreateLang.translate("logistically_linked.tuned")
			.component(), true);
	}

	private static void saveCategoriesIfAvailable(ItemStack stack, Level level, BlockPos pos, @Nullable UUID oldNetwork,
		UUID newNetwork) {
		if (level.getBlockEntity(pos) instanceof StockTickerBlockEntity stockTicker) {
			CompoundTag tag = stockTicker.saveWithFullMetadata(level.registryAccess());
			List<ItemStack> categories = NBTHelper.readItemList(
				tag.getList("Categories", Tag.TAG_COMPOUND), level.registryAccess());
			saveCategoriesToStack(stack, categories);
			return;
		}

		if (oldNetwork != null && !oldNetwork.equals(newNetwork))
			stack.remove(DataComponents.CONTAINER);
	}

	public static void saveCategoriesToStack(ItemStack stack, List<ItemStack> categories) {
		if (categories == null || categories.isEmpty()) {
			stack.remove(DataComponents.CONTAINER);
			return;
		}

		stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(categories));
	}

	public static List<ItemStack> loadCategoriesFromStack(ItemStack stack) {
		List<ItemStack> categories = new ArrayList<>(stack
			.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
			.stream()
			.toList());
		categories.removeIf(itemStack -> !itemStack.isEmpty() && !(itemStack.getItem() instanceof FilterItem));
		return categories;
	}

	public static int saveAddressesFromClipboard(ItemStack stack, ItemStack clipboard) {
		return saveAddressesFromClipboard(stack, ClipboardEntry.readAll(clipboard));
	}

	public static int saveAddressesFromClipboard(ItemStack stack, List<List<ClipboardEntry>> pages) {
		List<String> addresses = extractAddresses(pages);
		saveAddressesToStack(stack, addresses);
		return addresses.size();
	}

	public static void saveAddressesToStack(ItemStack stack, List<String> addresses) {
		if (addresses == null || addresses.isEmpty()) {
			clearTag(stack, ADDRESSES_TAG);
			return;
		}

		ListTag listTag = new ListTag();
		for (String address : addresses)
			listTag.add(StringTag.valueOf(address));
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.put(ADDRESSES_TAG, listTag));
	}

	public static List<String> loadAddressesFromStack(ItemStack stack) {
		CompoundTag tag = customData(stack);
		if (!tag.contains(ADDRESSES_TAG, Tag.TAG_LIST))
			return new ArrayList<>();

		ListTag listTag = tag.getList(ADDRESSES_TAG, Tag.TAG_STRING);
		List<String> addresses = new ArrayList<>(listTag.size());
		for (int i = 0; i < listTag.size(); i++) {
			String address = listTag.getString(i);
			if (!address.isBlank())
				addresses.add(address);
		}
		return addresses;
	}

	public static List<String> extractAddresses(ItemStack clipboard) {
		return extractAddresses(ClipboardEntry.readAll(clipboard));
	}

	public static List<String> extractAddresses(List<List<ClipboardEntry>> pages) {
		Set<String> added = new LinkedHashSet<>();
		for (List<ClipboardEntry> page : pages) {
			for (ClipboardEntry entry : page) {
				if (entry.checked)
					continue;
				String text = entry.text.getString();
				if (!text.startsWith("#") || text.length() == 1)
					continue;
				String address = text.substring(1)
					.trim();
				if (!address.isBlank())
					added.add(address);
			}
		}
		return List.copyOf(added);
	}

	private static CompoundTag customData(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	private static void clearTag(ItemStack stack, String key) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(key));
	}
}
