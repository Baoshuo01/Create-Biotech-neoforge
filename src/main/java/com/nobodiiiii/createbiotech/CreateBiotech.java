package com.nobodiiiii.createbiotech;

import com.nobodiiiii.createbiotech.content.biopackager.BioPackagerBlockEntity;
import com.nobodiiiii.createbiotech.content.creeperblastchamber.CreeperBlastChamberBlockEntity;
import com.nobodiiiii.createbiotech.content.evokerenchantingchamber.EvokerEnchantingChamberBlockEntity;
import com.nobodiiiii.createbiotech.content.experience.BuddingExperienceBlockEntity;
import com.nobodiiiii.createbiotech.content.experience.ExperiencePumpBlockEntity;
import com.nobodiiiii.createbiotech.content.explosionproofitemvault.ExplosionProofItemVaultBlockEntity;
import com.nobodiiiii.createbiotech.content.fixedcarrotfishingrod.FixedCarrotFishingRodGoalHandler;
import com.nobodiiiii.createbiotech.content.magmabelt.MagmaBeltCapabilities;
import com.nobodiiiii.createbiotech.content.petridish.PetriDishBlockEntity;
import com.nobodiiiii.createbiotech.content.shulkerpackager.ShulkerPackagerArmInteractions;
import com.nobodiiiii.createbiotech.content.slimebelt.SlimeBeltCapabilities;
import com.nobodiiiii.createbiotech.content.bufferpad.BufferPadMovementBehaviour;
import com.nobodiiiii.createbiotech.content.experience.ExperienceOpenPipeEffectHandler;
import com.nobodiiiii.createbiotech.content.explosionproofitemvault.ExplosionProofItemVaultCompat;
import com.nobodiiiii.createbiotech.network.CBPackets;
import com.nobodiiiii.createbiotech.registry.CBBlockEntityTypes;
import com.nobodiiiii.createbiotech.registry.CBBlocks;
import com.nobodiiiii.createbiotech.registry.CBConfigs;
import com.nobodiiiii.createbiotech.registry.CBCreativeModeTabs;
import com.nobodiiiii.createbiotech.registry.CBEntityTypes;
import com.nobodiiiii.createbiotech.registry.CBFluids;
import com.nobodiiiii.createbiotech.registry.CBItems;
import com.nobodiiiii.createbiotech.registry.CBMenuTypes;
import com.nobodiiiii.createbiotech.registry.CBParticleTypes;
import com.nobodiiiii.createbiotech.registry.CBRecipeTypes;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.yision.phantom.block.phantomport.PhantomPortCapabilities;
import com.yision.phantom.block.phantomport.PhantomPortTargetRegistry;
import com.yision.phantom.logistics.courier.AirCourierTaskManager;
import com.yision.phantom.logistics.courier.hud.AirCourierHudSync;
import com.yision.phantom.network.PhantomPayloads;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(CreateBiotech.MOD_ID)
public class CreateBiotech {
	public static final String MOD_ID = "create_biotech";

	public CreateBiotech(IEventBus modEventBus, ModContainer modContainer) {
		CBConfigs.register(modContainer);
		CBBlocks.register(modEventBus);
		CBItems.register(modEventBus);
		CBFluids.register(modEventBus);
		CBCreativeModeTabs.register(modEventBus);
		CBBlockEntityTypes.register(modEventBus);
		CBEntityTypes.register(modEventBus);
		CBMenuTypes.register(modEventBus);
		CBParticleTypes.register(modEventBus);
		CBRecipeTypes.register(modEventBus);
		modEventBus.addListener(CreateBiotech::onCommonSetup);
		modEventBus.addListener(CreateBiotech::onRegister);
		modEventBus.addListener(CreateBiotech::registerCapabilities);
		modEventBus.addListener(CBPackets::registerPayloads);
		modEventBus.addListener(PhantomPayloads::registerPayloads);
		modEventBus.addListener(PhantomPortCapabilities::registerCapabilities);
		modEventBus.addListener(SlimeBeltCapabilities::registerCapabilities);
		modEventBus.addListener(MagmaBeltCapabilities::registerCapabilities);
		registerPhantomEvents();
		FixedCarrotFishingRodGoalHandler.register();
	}

	private static void registerPhantomEvents() {
		NeoForge.EVENT_BUS.addListener(AirCourierHudSync::onServerTick);
		NeoForge.EVENT_BUS.addListener(AirCourierTaskManager::onServerTick);
		NeoForge.EVENT_BUS.addListener(PhantomPortTargetRegistry::onServerTick);
		NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
			AirCourierTaskManager.onServerStarting(event.getServer()));
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		BioPackagerBlockEntity.registerCapabilities(event);
		EvokerEnchantingChamberBlockEntity.registerCapabilities(event);
		CreeperBlastChamberBlockEntity.registerCapabilities(event);
		BuddingExperienceBlockEntity.registerCapabilities(event);
		ExperiencePumpBlockEntity.registerCapabilities(event);
		ExplosionProofItemVaultBlockEntity.registerCapabilities(event);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CBBlockEntityTypes.PETRI_DISH.get(),
			PetriDishBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CBBlockEntityTypes.PETRI_DISH.get(),
			PetriDishBlockEntity::getFluidHandler);
	}

	private static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ExperienceOpenPipeEffectHandler.register();
			ExplosionProofItemVaultCompat.register();
			BufferPadMovementBehaviour bufferPadMovementBehaviour = new BufferPadMovementBehaviour();
			for (DyeColor color : DyeColor.values())
				MovementBehaviour.REGISTRY.register(CBBlocks.BUFFER_PADS.get(color).get(), bufferPadMovementBehaviour);
		});
	}

	private static void onRegister(RegisterEvent event) {
		ShulkerPackagerArmInteractions.register();
	}

	public static ResourceLocation asResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
