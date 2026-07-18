package com.nobodiiiii.createbiotech.content.smartglue;

import java.util.Set;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SmartSuperGlueRemovalPacket implements CustomPacketPayload {

	public static final Type<SmartSuperGlueRemovalPacket> TYPE =
		new Type<>(CreateBiotech.asResource("smart_super_glue_removal"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SmartSuperGlueRemovalPacket> STREAM_CODEC =
		StreamCodec.ofMember(SmartSuperGlueRemovalPacket::write, SmartSuperGlueRemovalPacket::decode);

	private final int entityId;
	private final BlockPos soundSource;
	private final boolean removeConnected;

	public SmartSuperGlueRemovalPacket(int entityId, BlockPos soundSource, boolean removeConnected) {
		this.entityId = entityId;
		this.soundSource = soundSource.immutable();
		this.removeConnected = removeConnected;
	}

	private static SmartSuperGlueRemovalPacket decode(RegistryFriendlyByteBuf buffer) {
		return new SmartSuperGlueRemovalPacket(buffer.readInt(), buffer.readBlockPos(), buffer.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeInt(entityId);
		buffer.writeBlockPos(soundSource);
		buffer.writeBoolean(removeConnected);
	}

	public void handle(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player)
			apply(player);
	}

	private void apply(ServerPlayer player) {
		Entity entity = player.level().getEntity(entityId);
		if (!(entity instanceof SuperGlueEntity superGlue) || !SmartSuperGlueHelper.isSmartGlueCompatible(superGlue))
			return;

		double range = 32;
		if (player.distanceToSqr(superGlue.position()) > range * range)
			return;

		Set<SuperGlueEntity> glueToRemove = removeConnected
			? SmartSuperGlueHelper.findConnectedGlueEntities(player.level(), superGlue)
			: Set.of(superGlue);
		if (glueToRemove.isEmpty())
			return;

		AllSoundEvents.SLIME_ADDED.play(player.level(), null, soundSource, 0.5F, 0.5F);
		for (SuperGlueEntity glueEntity : glueToRemove) {
			glueEntity.spawnParticles();
			glueEntity.discard();
		}
	}

	@Override
	public Type<SmartSuperGlueRemovalPacket> type() {
		return TYPE;
	}
}
