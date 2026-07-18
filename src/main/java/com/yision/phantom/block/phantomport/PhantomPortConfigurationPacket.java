package com.yision.phantom.block.phantomport;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.simibubi.create.foundation.utility.AdventureUtil;
import com.yision.phantom.logistics.courier.AirCourierReturnMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class PhantomPortConfigurationPacket implements CustomPacketPayload {
	private static final int MAX_INTERACTION_RANGE = 20;

	public static final Type<PhantomPortConfigurationPacket> TYPE =
		new Type<>(CreateBiotech.asResource("phantom_port_configuration"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PhantomPortConfigurationPacket> STREAM_CODEC =
		StreamCodec.ofMember(PhantomPortConfigurationPacket::write, PhantomPortConfigurationPacket::new);

	private final BlockPos pos;
	private final String newFilter;
	private final boolean acceptPackages;
	private final AirCourierReturnMode returnMode;

	public PhantomPortConfigurationPacket(BlockPos pos, String newFilter, boolean acceptPackages,
		AirCourierReturnMode returnMode) {
		this.pos = pos;
		this.newFilter = newFilter == null ? "" : newFilter;
		this.acceptPackages = acceptPackages;
		this.returnMode = returnMode == null ? AirCourierReturnMode.DEFAULT_FOR_PORT : returnMode;
	}

	private PhantomPortConfigurationPacket(RegistryFriendlyByteBuf buffer) {
		this(buffer.readBlockPos(), buffer.readUtf(), buffer.readBoolean(),
			AirCourierReturnMode.byId(buffer.readVarInt()));
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeUtf(newFilter);
		buffer.writeBoolean(acceptPackages);
		buffer.writeVarInt(returnMode.id());
	}

	public static void handle(PhantomPortConfigurationPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (!(context.player() instanceof ServerPlayer player)
				|| player.isSpectator()
				|| AdventureUtil.isAdventure(player)) {
				return;
			}

			Level level = player.level();
			if (!level.isLoaded(packet.pos)
				|| !player.canInteractWithBlock(packet.pos, MAX_INTERACTION_RANGE)
				|| !(level.getBlockEntity(packet.pos) instanceof PhantomPortBlockEntity blockEntity)) {
				return;
			}

			packet.applySettings(blockEntity);
			blockEntity.sendData();
			blockEntity.setChanged();
		});
	}

	private void applySettings(PhantomPortBlockEntity blockEntity) {
		boolean filterChanged = !blockEntity.addressFilter.equals(newFilter)
			|| blockEntity.acceptsPackages != acceptPackages;
		boolean modeChanged = blockEntity.getReturnMode() != returnMode;
		if (!filterChanged && !modeChanged) {
			return;
		}

		if (filterChanged) {
			blockEntity.addressFilter = newFilter;
			blockEntity.acceptsPackages = acceptPackages;
			blockEntity.filterChanged();
		}
		if (modeChanged) {
			blockEntity.setReturnMode(returnMode);
		}
		blockEntity.notifyUpdate();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
