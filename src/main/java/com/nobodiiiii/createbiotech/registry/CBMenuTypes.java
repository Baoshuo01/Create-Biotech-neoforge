package com.nobodiiiii.createbiotech.registry;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.content.shulkerteleporter.ShulkerTeleporterMenu;
import com.nobodiiiii.createbiotech.content.spiderassemblytable.SpiderAssemblyTableMenu;
import com.nobodiiiii.createbiotech.content.wirelessterminal.WirelessStockKeeperRequestMenu;
import com.yision.phantom.block.phantomport.PhantomPortMenu;
import com.yision.phantom.item.miniphantom.MiniPhantomMenu;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CBMenuTypes {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES =
		DeferredRegister.create(Registries.MENU, CreateBiotech.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<SpiderAssemblyTableMenu>> SPIDER_ASSEMBLY_TABLE =
		MENU_TYPES.register("spider_assembly_table", () -> IMenuTypeExtension.create(SpiderAssemblyTableMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<WirelessStockKeeperRequestMenu>> WIRELESS_STOCK_KEEPER_REQUEST =
		MENU_TYPES.register("wireless_stock_keeper_request",
			() -> IMenuTypeExtension.create(WirelessStockKeeperRequestMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<ShulkerTeleporterMenu>> SHULKER_TELEPORTER =
		MENU_TYPES.register("shulker_teleporter", () -> IMenuTypeExtension.create(ShulkerTeleporterMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<PhantomPortMenu>> PHANTOMPORT =
		MENU_TYPES.register("phantomport", () -> IMenuTypeExtension.create(PhantomPortMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<MiniPhantomMenu>> MINI_PHANTOM =
		MENU_TYPES.register("mini_phantom", () -> IMenuTypeExtension.create(MiniPhantomMenu::new));

	private CBMenuTypes() {}

	public static void register(IEventBus modEventBus) {
		MENU_TYPES.register(modEventBus);
	}
}
