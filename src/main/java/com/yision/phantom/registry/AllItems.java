package com.yision.phantom.registry;

import com.nobodiiiii.createbiotech.registry.CBItems;
import com.yision.phantom.item.miniphantom.MiniPhantomItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class AllItems {
	public static final ItemRef<MiniPhantomItem> MINI_PHANTOM = new ItemRef<>(CBItems.MINI_PHANTOM);

	private AllItems() {}

	public record ItemRef<T extends Item>(DeferredHolder<Item, T> object) {
		public T get() {
			return object.get();
		}

		public ItemStack asStack() {
			return new ItemStack(get());
		}
	}
}
