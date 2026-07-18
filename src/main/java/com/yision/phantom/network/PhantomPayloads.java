package com.yision.phantom.network;

import com.yision.phantom.block.phantomport.PhantomPortConfigurationPacket;
import com.yision.phantom.network.courier.AirCourierHudPacket;
import com.yision.phantom.network.phantom.MiniPhantomConfirmPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class PhantomPayloads {
	private static final String NETWORK_VERSION = "1";

	private PhantomPayloads() {}

	public static void registerPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(NETWORK_VERSION)
			.executesOn(HandlerThread.NETWORK);
		registrar.playToClient(AirCourierHudPacket.TYPE, AirCourierHudPacket.STREAM_CODEC,
			AirCourierHudPacket::handle);
		registrar.playToServer(MiniPhantomConfirmPacket.TYPE, MiniPhantomConfirmPacket.STREAM_CODEC,
			MiniPhantomConfirmPacket::handle);
		registrar.playToServer(PhantomPortConfigurationPacket.TYPE, PhantomPortConfigurationPacket.STREAM_CODEC,
			PhantomPortConfigurationPacket::handle);
	}
}
