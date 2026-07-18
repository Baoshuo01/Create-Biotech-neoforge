package com.nobodiiiii.createbiotech.content.slimebelt;

import com.nobodiiiii.createbiotech.registry.CBBlockEntityTypes;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class SlimeBeltCapabilities {
	private SlimeBeltCapabilities() {}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			CBBlockEntityTypes.SLIME_BELT.get(),
			SlimeBeltBlockEntity::getItemHandler
		);
	}
}
