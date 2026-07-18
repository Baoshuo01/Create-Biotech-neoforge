package com.yision.phantom.block.phantomport;

import com.nobodiiiii.createbiotech.registry.CBBlockEntityTypes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class PhantomPortCapabilities {
	private PhantomPortCapabilities() {}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			CBBlockEntityTypes.PHANTOMPORT.get(),
			PhantomPortBlockEntity::getItemHandler
		);
	}
}
