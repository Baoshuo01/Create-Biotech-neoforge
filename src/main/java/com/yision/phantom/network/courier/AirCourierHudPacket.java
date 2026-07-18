package com.yision.phantom.network.courier;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.yision.phantom.client.gui.hud.AirCourierHudOverlay;
import com.yision.phantom.logistics.courier.hud.AirCourierHudEntry;
import com.yision.phantom.logistics.courier.hud.AirCourierHudPayload;
import io.netty.handler.codec.DecoderException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class AirCourierHudPacket implements CustomPacketPayload {
	public static final Type<AirCourierHudPacket> TYPE =
		new Type<>(CreateBiotech.asResource("air_courier_hud"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AirCourierHudPacket> STREAM_CODEC =
		StreamCodec.ofMember(AirCourierHudPacket::write, AirCourierHudPacket::new);

	private final AirCourierHudPayload payload;

	private AirCourierHudPacket(RegistryFriendlyByteBuf buffer) {
		int count = buffer.readVarInt();
		if (count < 0 || count > AirCourierHudPayload.MAX_VISIBLE_ENTRIES) {
			throw new DecoderException("Invalid air courier HUD entry count: " + count);
		}
		List<AirCourierHudEntry> entries = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			entries.add(AirCourierHudEntry.STREAM_CODEC.decode(buffer));
		}
		this.payload = new AirCourierHudPayload(entries);
	}

	private AirCourierHudPacket(AirCourierHudPayload payload) {
		this.payload = payload;
	}

	public static AirCourierHudPacket of(AirCourierHudPayload payload) {
		return new AirCourierHudPacket(payload);
	}

	public static AirCourierHudPacket hidden() {
		return new AirCourierHudPacket(AirCourierHudPayload.hidden());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeVarInt(payload.entries().size());
		for (AirCourierHudEntry entry : payload.entries()) {
			AirCourierHudEntry.STREAM_CODEC.encode(buffer, entry);
		}
	}

	public static void handle(AirCourierHudPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> AirCourierHudOverlay.updateState(packet.payload));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
