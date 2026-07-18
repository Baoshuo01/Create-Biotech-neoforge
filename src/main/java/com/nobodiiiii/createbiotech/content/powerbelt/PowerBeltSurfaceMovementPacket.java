package com.nobodiiiii.createbiotech.content.powerbelt;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.registry.CBConfigs;
import com.simibubi.create.content.kinetics.belt.BeltSlope;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PowerBeltSurfaceMovementPacket implements CustomPacketPayload {

	public static final Type<PowerBeltSurfaceMovementPacket> TYPE =
		new Type<>(CreateBiotech.asResource("power_belt_surface_movement"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PowerBeltSurfaceMovementPacket> STREAM_CODEC =
		StreamCodec.ofMember(PowerBeltSurfaceMovementPacket::write, PowerBeltSurfaceMovementPacket::decode);

	private final BlockPos pos;
	private final float surfaceSpeed;

	public PowerBeltSurfaceMovementPacket(BlockPos pos, float surfaceSpeed) {
		this.pos = pos.immutable();
		this.surfaceSpeed = surfaceSpeed;
	}

	private static PowerBeltSurfaceMovementPacket decode(RegistryFriendlyByteBuf buffer) {
		return new PowerBeltSurfaceMovementPacket(buffer.readBlockPos(), buffer.readFloat());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeFloat(surfaceSpeed);
	}

	public void handle(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player)
			apply(player);
	}

	private void apply(ServerPlayer player) {
		if (player.isSpectator() || player.getAbilities().flying)
			return;
		if (!Float.isFinite(surfaceSpeed))
			return;

		Level level = player.level();
		if (!level.isLoaded(pos))
			return;
		if (!pos.closerThan(player.blockPosition(), 4))
			return;

		BlockState state = level.getBlockState(pos);
		if (!PowerBeltBlock.isPowerBelt(state)
			|| state.getValue(PowerBeltBlock.SLOPE) != BeltSlope.HORIZONTAL)
			return;
		if (!PowerBeltBlock.isEntityOnBeltSurface(pos, player) || !isHorizontallyOverBelt(player, pos))
			return;

		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof PowerBeltBlockEntity powerBelt)
			powerBelt.addSurfaceMovement(Mth.clamp(surfaceSpeed, -getMaxPlayerSurfaceSpeed(),
				getMaxPlayerSurfaceSpeed()));
	}

	private static boolean isHorizontallyOverBelt(Player player, BlockPos pos) {
		AABB box = player.getBoundingBox();
		return box.maxX > pos.getX() && box.minX < pos.getX() + 1
			&& box.maxZ > pos.getZ() && box.minZ < pos.getZ() + 1;
	}

	private static float getMaxPlayerSurfaceSpeed() {
		return CBConfigs.SERVER.powerBelt.maxPlayerSurfaceSpeed.get().floatValue();
	}

	@Override
	public Type<PowerBeltSurfaceMovementPacket> type() {
		return TYPE;
	}
}
