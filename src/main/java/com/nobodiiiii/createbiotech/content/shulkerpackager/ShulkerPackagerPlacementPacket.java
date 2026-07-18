package com.nobodiiiii.createbiotech.content.shulkerpackager;

import java.util.Collection;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShulkerPackagerPlacementPacket implements CustomPacketPayload {

	public static final Type<ShulkerPackagerPlacementPacket> TYPE =
		new Type<>(CreateBiotech.asResource("shulker_packager_placement"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerPackagerPlacementPacket> STREAM_CODEC =
		StreamCodec.ofMember(ShulkerPackagerPlacementPacket::write, ShulkerPackagerPlacementPacket::decode);

	private final ListTag interactionPoints;
	private final BlockPos pos;

	public ShulkerPackagerPlacementPacket(Collection<ArmInteractionPoint> points, BlockPos pos) {
		this.pos = pos.immutable();
		this.interactionPoints = new ListTag();
		points.stream()
			.map(point -> point.serialize(this.pos))
			.forEach(interactionPoints::add);
	}

	private ShulkerPackagerPlacementPacket(ListTag interactionPoints, BlockPos pos) {
		this.interactionPoints = interactionPoints;
		this.pos = pos.immutable();
	}

	private static ShulkerPackagerPlacementPacket decode(RegistryFriendlyByteBuf buffer) {
		CompoundTag nbt = buffer.readNbt();
		ListTag points = nbt == null ? new ListTag() : nbt.getList("Points", Tag.TAG_COMPOUND);
		return new ShulkerPackagerPlacementPacket(points, buffer.readBlockPos());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		CompoundTag nbt = new CompoundTag();
		nbt.put("Points", interactionPoints);
		buffer.writeNbt(nbt);
		buffer.writeBlockPos(pos);
	}

	public void handle(IPayloadContext context) {
		if (!(context.player() instanceof ServerPlayer player))
			return;
		Level level = player.level();
		if (!level.isLoaded(pos))
			return;
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof ShulkerPackagerBlockEntity packager)
			packager.setInteractionPointTag(interactionPoints);
	}

	@Override
	public Type<ShulkerPackagerPlacementPacket> type() {
		return TYPE;
	}

	public static class ClientBoundRequest implements CustomPacketPayload {

		public static final Type<ClientBoundRequest> TYPE =
			new Type<>(CreateBiotech.asResource("shulker_packager_placement_request"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundRequest> STREAM_CODEC =
			StreamCodec.ofMember(ClientBoundRequest::write, ClientBoundRequest::decode);

		private final BlockPos pos;

		public ClientBoundRequest(BlockPos pos) {
			this.pos = pos.immutable();
		}

		private static ClientBoundRequest decode(RegistryFriendlyByteBuf buffer) {
			return new ClientBoundRequest(buffer.readBlockPos());
		}

		private void write(RegistryFriendlyByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}

		public void handle(IPayloadContext context) {
			ShulkerPackagerConnectionHandler.flushSettings(pos);
		}

		@Override
		public Type<ClientBoundRequest> type() {
			return TYPE;
		}
	}
}
