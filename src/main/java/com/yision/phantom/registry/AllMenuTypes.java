package com.yision.phantom.registry;

import com.nobodiiiii.createbiotech.registry.CBMenuTypes;
import com.yision.phantom.block.phantomport.PhantomPortMenu;
import com.yision.phantom.item.miniphantom.MiniPhantomMenu;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class AllMenuTypes {
	public static final DeferredHolder<MenuType<?>, MenuType<PhantomPortMenu>> PHANTOMPORT = CBMenuTypes.PHANTOMPORT;
	public static final DeferredHolder<MenuType<?>, MenuType<MiniPhantomMenu>> MINI_PHANTOM = CBMenuTypes.MINI_PHANTOM;

	private AllMenuTypes() {}
}
