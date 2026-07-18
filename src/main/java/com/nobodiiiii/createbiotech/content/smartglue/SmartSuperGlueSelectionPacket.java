package com.nobodiiiii.createbiotech.content.smartglue;

import java.util.Set;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueSelectionHelper;
import com.simibubi.create.foundation.advancement.AllAdvancements;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SmartSuperGlueSelectionPacket implements CustomPacketPayload {

	public static final Type<SmartSuperGlueSelectionPacket> TYPE =
		new Type<>(CreateBiotech.asResource("smart_super_glue_selection"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SmartSuperGlueSelectionPacket> STREAM_CODEC =
		StreamCodec.ofMember(SmartSuperGlueSelectionPacket::write, SmartSuperGlueSelectionPacket::decode);

	private final BlockPos from;
	private final BlockPos to;

	public SmartSuperGlueSelectionPacket(BlockPos from, BlockPos to) {
		this.from = from.immutable();
		this.to = to.immutable();
	}

	private static SmartSuperGlueSelectionPacket decode(RegistryFriendlyByteBuf buffer) {
		return new SmartSuperGlueSelectionPacket(buffer.readBlockPos(), buffer.readBlockPos());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeBlockPos(from);
		buffer.writeBlockPos(to);
	}

	public void handle(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player)
			apply(player);
	}

	private void apply(ServerPlayer player) {
		double range = player.blockInteractionRange() + 2;
		if (player.distanceToSqr(Vec3.atCenterOf(to)) > range * range || !to.closerThan(from, 25))
			return;

		Set<BlockPos> group = SuperGlueSelectionHelper.searchGlueGroup(player.level(), from, to, false);
		if (group == null || !group.contains(to))
			return;
		if (!SmartSuperGlueHelper.collectGlueFromInventory(player, 1, true))
			return;

		SmartSuperGlueHelper.collectGlueFromInventory(player, 1, false);
		SuperGlueEntity entity = new SuperGlueEntity(player.level(), SuperGlueEntity.span(from, to));
		player.level().addFreshEntity(entity);
		entity.spawnParticles();
		AllAdvancements.SUPER_GLUE.awardTo(player);
	}

	@Override
	public Type<SmartSuperGlueSelectionPacket> type() {
		return TYPE;
	}
}
