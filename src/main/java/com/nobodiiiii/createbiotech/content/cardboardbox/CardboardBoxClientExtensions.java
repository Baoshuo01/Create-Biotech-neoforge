package com.nobodiiiii.createbiotech.content.cardboardbox;

import com.nobodiiiii.createbiotech.registry.CBItems;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@OnlyIn(Dist.CLIENT)
public final class CardboardBoxClientExtensions {

	private CardboardBoxClientExtensions() {}

	public static void register(RegisterClientExtensionsEvent event) {
		register(event, CBItems.CARDBOARD_BOX.get());
		register(event, CBItems.LARGE_CARDBOARD_BOX.get());
	}

	private static void register(RegisterClientExtensionsEvent event, Item item) {
		event.registerItem(SimpleCustomRenderer.create(item, new CapturedEntityBoxItemRenderer()), item);
	}
}
