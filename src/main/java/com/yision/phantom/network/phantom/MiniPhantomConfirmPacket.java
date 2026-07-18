package com.yision.phantom.network.phantom;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.yision.phantom.item.miniphantom.MiniPhantomMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MiniPhantomConfirmPacket implements CustomPacketPayload {
	public static final Type<MiniPhantomConfirmPacket> TYPE =
		new Type<>(CreateBiotech.asResource("mini_phantom_confirm"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MiniPhantomConfirmPacket> STREAM_CODEC =
		StreamCodec.ofMember(MiniPhantomConfirmPacket::write, MiniPhantomConfirmPacket::new);

	private final String address;

	public MiniPhantomConfirmPacket(String address) {
		this.address = address == null ? "" : address;
	}

	private MiniPhantomConfirmPacket(RegistryFriendlyByteBuf buffer) {
		this(buffer.readUtf());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeUtf(address);
	}

	public static void handle(MiniPhantomConfirmPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (!(context.player() instanceof ServerPlayer sender)
				|| !(sender.containerMenu instanceof MiniPhantomMenu menu)) {
				return;
			}
			if (menu.confirm(packet.address)) {
				sender.closeContainer();
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
