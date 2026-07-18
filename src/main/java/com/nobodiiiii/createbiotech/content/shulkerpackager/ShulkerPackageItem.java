package com.nobodiiiii.createbiotech.content.shulkerpackager;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.registry.CBItems;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.logistics.box.PackageStyles.PackageStyle;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ShulkerPackageItem extends PackageItem {

	public static final PackageStyle STYLE = new PackageStyle("shulker", 12, 12, 23f, false);

	public ShulkerPackageItem(Item.Properties properties) {
		super(properties, STYLE);
		// Keep this package out of Create's global random style pools.
		PackageStyles.ALL_BOXES.remove(this);
		PackageStyles.STANDARD_BOXES.remove(this);
		PackageStyles.RARE_BOXES.remove(this);
	}

	@Override
	public String getDescriptionId() {
		return "item." + CreateBiotech.MOD_ID + ".shulker_package";
	}

	public static ItemStack containing(ItemStackHandler stacks) {
		ItemStack box = new ItemStack(CBItems.SHULKER_PACKAGE.get());
		box.set(AllDataComponents.PACKAGE_CONTENTS, ItemHelper.containerContentsFromHandler(stacks));
		return box;
	}

	public static void migrateLegacyContents(ItemStack box, HolderLookup.Provider registries) {
		if (box.has(AllDataComponents.PACKAGE_CONTENTS))
			return;

		CustomData customData = box.get(DataComponents.CUSTOM_DATA);
		if (customData == null)
			return;

		CompoundTag legacyData = customData.copyTag();
		if (!legacyData.contains("Items", Tag.TAG_COMPOUND))
			return;

		ItemStackHandler contents = new ItemStackHandler(PackageItem.SLOTS);
		contents.deserializeNBT(registries, legacyData.getCompound("Items"));
		box.set(AllDataComponents.PACKAGE_CONTENTS, ItemHelper.containerContentsFromHandler(contents));

		legacyData.remove("Items");
		if (legacyData.isEmpty())
			box.remove(DataComponents.CUSTOM_DATA);
		else
			box.set(DataComponents.CUSTOM_DATA, CustomData.of(legacyData));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		migrateLegacyContents(stack, level.registryAccess());
		super.inventoryTick(stack, level, entity, slotId, isSelected);
	}
}
