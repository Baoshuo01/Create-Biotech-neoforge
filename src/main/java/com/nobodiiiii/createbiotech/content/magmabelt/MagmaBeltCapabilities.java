package com.nobodiiiii.createbiotech.content.magmabelt;

import com.nobodiiiii.createbiotech.registry.CBBlockEntityTypes;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class MagmaBeltCapabilities {
	private MagmaBeltCapabilities() {}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			CBBlockEntityTypes.MAGMA_BELT.get(),
			MagmaBeltBlockEntity::getItemHandler
		);
	}
}
