package com.nobodiiiii.createbiotech.content.shulkerteleporter;

import java.util.ArrayList;
import java.util.List;

import com.nobodiiiii.createbiotech.CreateBiotech;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShulkerTeleporterConfigPacket implements CustomPacketPayload {

	public static final Type<ShulkerTeleporterConfigPacket> TYPE =
		new Type<>(CreateBiotech.asResource("shulker_teleporter_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerTeleporterConfigPacket> STREAM_CODEC =
		StreamCodec.ofMember(ShulkerTeleporterConfigPacket::write, ShulkerTeleporterConfigPacket::decode);

	private final BlockPos pos;
	private final String ownAddress;
	private final String targetAddress;
	private final List<String> candidateAddresses;

	public ShulkerTeleporterConfigPacket(BlockPos pos, String ownAddress, String targetAddress,
		List<String> candidateAddresses) {
		this.pos = pos.immutable();
		this.ownAddress = ShulkerTeleporterBlockEntity.normalizeAddress(ownAddress);
		this.targetAddress = ShulkerTeleporterBlockEntity.normalizeAddress(targetAddress);
		this.candidateAddresses = List.copyOf(
			ShulkerTeleporterBlockEntity.normalizeCandidateAddresses(candidateAddresses));
	}

	private static ShulkerTeleporterConfigPacket decode(RegistryFriendlyByteBuf buffer) {
		BlockPos pos = buffer.readBlockPos();
		String ownAddress = buffer.readUtf(ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH);
		String targetAddress = buffer.readUtf(ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH);
		int size = buffer.readVarInt();
		if (size < 0 || size > ShulkerTeleporterBlockEntity.MAX_CANDIDATE_ADDRESSES)
			throw new IllegalArgumentException("Invalid shulker teleporter candidate address count: " + size);
		List<String> addresses = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
			addresses.add(buffer.readUtf(ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH));
		return new ShulkerTeleporterConfigPacket(pos, ownAddress, targetAddress, addresses);
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeUtf(ownAddress, ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH);
		buffer.writeUtf(targetAddress, ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH);
		buffer.writeVarInt(candidateAddresses.size());
		for (String candidateAddress : candidateAddresses)
			buffer.writeUtf(candidateAddress, ShulkerTeleporterBlockEntity.MAX_ADDRESS_LENGTH);
	}

	public void handle(IPayloadContext context) {
		if (!(context.player() instanceof ServerPlayer player))
			return;
		if (player.distanceToSqr(pos.getX() + 0.5d, pos.getY() + 1.0d, pos.getZ() + 0.5d) > 64.0d)
			return;
		BlockEntity blockEntity = player.level().getBlockEntity(pos);
		if (blockEntity instanceof ShulkerTeleporterBlockEntity teleporter)
			teleporter.setConfiguration(ownAddress, targetAddress, candidateAddresses);
	}

	@Override
	public Type<ShulkerTeleporterConfigPacket> type() {
		return TYPE;
	}
}
