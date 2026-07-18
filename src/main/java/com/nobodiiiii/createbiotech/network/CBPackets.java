package com.nobodiiiii.createbiotech.network;

import com.nobodiiiii.createbiotech.content.biopackager.BioPackagerContraptionAnimationPacket;
import com.nobodiiiii.createbiotech.content.powerbelt.PowerBeltEntityAnimationPacket;
import com.nobodiiiii.createbiotech.content.powerbelt.PowerBeltSurfaceMovementPacket;
import com.nobodiiiii.createbiotech.content.shulkerpackager.ShulkerPackagerPlacementPacket;
import com.nobodiiiii.createbiotech.content.shulkerteleporter.ShulkerTeleporterConfigPacket;
import com.nobodiiiii.createbiotech.content.smartglue.SmartSuperGlueRemovalPacket;
import com.nobodiiiii.createbiotech.content.smartglue.SmartSuperGlueSelectionPacket;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class CBPackets {

	private static final String NETWORK_VERSION = "8";
	private CBPackets() {}

	public static void registerPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(NETWORK_VERSION);

		registrar.playToServer(PowerBeltSurfaceMovementPacket.TYPE,
			PowerBeltSurfaceMovementPacket.STREAM_CODEC, PowerBeltSurfaceMovementPacket::handle);
		registrar.playToClient(PowerBeltEntityAnimationPacket.TYPE,
			PowerBeltEntityAnimationPacket.STREAM_CODEC, PowerBeltEntityAnimationPacket::handle);
		registrar.playToClient(BioPackagerContraptionAnimationPacket.TYPE,
			BioPackagerContraptionAnimationPacket.STREAM_CODEC, BioPackagerContraptionAnimationPacket::handle);
		registrar.playToServer(SmartSuperGlueSelectionPacket.TYPE,
			SmartSuperGlueSelectionPacket.STREAM_CODEC, SmartSuperGlueSelectionPacket::handle);
		registrar.playToServer(SmartSuperGlueRemovalPacket.TYPE,
			SmartSuperGlueRemovalPacket.STREAM_CODEC, SmartSuperGlueRemovalPacket::handle);
		registrar.playToServer(ShulkerPackagerPlacementPacket.TYPE,
			ShulkerPackagerPlacementPacket.STREAM_CODEC, ShulkerPackagerPlacementPacket::handle);
		registrar.playToClient(ShulkerPackagerPlacementPacket.ClientBoundRequest.TYPE,
			ShulkerPackagerPlacementPacket.ClientBoundRequest.STREAM_CODEC,
			ShulkerPackagerPlacementPacket.ClientBoundRequest::handle);
		registrar.playToServer(ShulkerTeleporterConfigPacket.TYPE,
			ShulkerTeleporterConfigPacket.STREAM_CODEC, ShulkerTeleporterConfigPacket::handle);
	}

	public static void sendToServer(CustomPacketPayload payload) {
		PacketDistributor.sendToServer(payload);
	}

	public static void sendToTrackingEntity(CustomPacketPayload payload, Entity entity) {
		PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
	}

	public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, payload);
	}
}
