package com.nobodiiiii.createbiotech.content.spiderassemblytable;

import com.nobodiiiii.createbiotech.registry.CBItems;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@OnlyIn(Dist.CLIENT)
public final class SpiderAssemblyTableClientExtensions {

	private SpiderAssemblyTableClientExtensions() {}

	public static void register(RegisterClientExtensionsEvent event) {
		Item item = CBItems.SPIDER_ASSEMBLY_TABLE.get();
		event.registerItem(SimpleCustomRenderer.create(item, new SpiderAssemblyTableItemRenderer()), item);
	}
}
