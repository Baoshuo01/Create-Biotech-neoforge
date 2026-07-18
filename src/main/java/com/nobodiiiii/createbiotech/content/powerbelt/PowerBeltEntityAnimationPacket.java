package com.nobodiiiii.createbiotech.content.powerbelt;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.client.PowerBeltClientAnimationHandler;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PowerBeltEntityAnimationPacket implements CustomPacketPayload {

	public static final Type<PowerBeltEntityAnimationPacket> TYPE =
		new Type<>(CreateBiotech.asResource("power_belt_entity_animation"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PowerBeltEntityAnimationPacket> STREAM_CODEC =
		StreamCodec.ofMember(PowerBeltEntityAnimationPacket::write, PowerBeltEntityAnimationPacket::decode);

	private static final float MAX_SURFACE_MOVEMENT = 1.0f;

	private final int entityId;
	private final float distance;

	public PowerBeltEntityAnimationPacket(int entityId, float distance) {
		this.entityId = entityId;
		this.distance = distance;
	}

	private static PowerBeltEntityAnimationPacket decode(RegistryFriendlyByteBuf buffer) {
		return new PowerBeltEntityAnimationPacket(buffer.readVarInt(), buffer.readFloat());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeVarInt(entityId);
		buffer.writeFloat(distance);
	}

	public void handle(IPayloadContext context) {
		if (!Float.isFinite(distance))
			return;

		float clampedDistance = Mth.clamp(distance, 0, MAX_SURFACE_MOVEMENT);
		PowerBeltClientAnimationHandler.handleSurfaceMovement(entityId, clampedDistance);
	}

	@Override
	public Type<PowerBeltEntityAnimationPacket> type() {
		return TYPE;
	}
}
