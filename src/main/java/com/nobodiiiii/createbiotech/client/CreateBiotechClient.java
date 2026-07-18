package com.nobodiiiii.createbiotech.client;

import java.util.function.Predicate;

import org.joml.Vector3f;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nobodiiiii.createbiotech.content.evokerenchantingchamber.EvokerEnchantingChamberItemRenderer;
import com.nobodiiiii.createbiotech.content.evokerenchantingchamber.EvokerEnchantingChamberRenderer;
import com.nobodiiiii.createbiotech.content.experience.ExperiencePumpRenderer;
import com.nobodiiiii.createbiotech.content.biopackager.BioPackagerRenderer;
import com.nobodiiiii.createbiotech.content.biopackager.BioPackagerVisual;
import com.nobodiiiii.createbiotech.content.boneratchet.BoneRatchetRenderer;
import com.nobodiiiii.createbiotech.content.cardboardbox.CardboardBoxClientExtensions;
import com.nobodiiiii.createbiotech.content.cardboardbox.CardboardBoxEntityRenderer;
import com.nobodiiiii.createbiotech.content.creeperblastchamber.BlastProofChainDriveRenderer;
import com.nobodiiiii.createbiotech.content.creeperblastchamber.CreeperBlastChamberBlock;
import com.nobodiiiii.createbiotech.content.creeperblastchamber.CreeperBlastChamberRenderer;
import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.client.render.SlimeBeltFunnelModel;
import com.nobodiiiii.createbiotech.content.cardboardbox.CapturedEntityBoxHelper;
import com.nobodiiiii.createbiotech.content.cardboardbox.CardboardBoxPartials;
import com.nobodiiiii.createbiotech.content.explosionproofitemvault.ExplosionProofItemVaultCTBehaviour;
import com.nobodiiiii.createbiotech.content.fixedcarrotfishingrod.FixedCarrotFishingRodRenderer;
import com.nobodiiiii.createbiotech.content.magmabelt.MagmaBeltBlock;
import com.nobodiiiii.createbiotech.content.magmabelt.MagmaBeltHelper;
import com.nobodiiiii.createbiotech.content.magmabelt.MagmaBeltRenderer;
import com.nobodiiiii.createbiotech.content.magmabelt.MagmaBeltSpriteShifts;
import com.nobodiiiii.createbiotech.content.powerbelt.PowerBeltRenderer;
import com.nobodiiiii.createbiotech.content.powerbelt.PowerBeltSpriteShifts;
import com.nobodiiiii.createbiotech.content.petridish.PetriDishRenderer;
import com.nobodiiiii.createbiotech.content.schrodingerscat.SchrodingersCatRenderer;
import com.nobodiiiii.createbiotech.content.shulkerpackager.ShulkerPackagePartials;
import com.nobodiiiii.createbiotech.content.shulkerpackager.ShulkerPackagerRenderer;
import com.nobodiiiii.createbiotech.content.shulkerpackager.ShulkerPackagerVisual;
import com.nobodiiiii.createbiotech.content.shulkerteleporter.ShulkerTeleporterRenderer;
import com.nobodiiiii.createbiotech.content.shulkerteleporter.ShulkerTeleporterScreen;
import com.nobodiiiii.createbiotech.content.slimebelt.SlimeBeltHelper;
import com.nobodiiiii.createbiotech.content.slimebelt.SlimeBeltRenderer;
import com.nobodiiiii.createbiotech.content.slimebelt.SlimeBeltSpriteShifts;
import com.nobodiiiii.createbiotech.content.spiderassemblytable.SpiderAssemblyTableClientExtensions;
import com.nobodiiiii.createbiotech.content.spiderassemblytable.SpiderAssemblyTableCogRenderer;
import com.nobodiiiii.createbiotech.content.spiderassemblytable.SpiderAssemblyTableRenderer;
import com.nobodiiiii.createbiotech.content.spiderassemblytable.SpiderAssemblyTableScreen;
import com.nobodiiiii.createbiotech.content.squidprinter.SquidPrinterItemRenderer;
import com.nobodiiiii.createbiotech.content.squidprinter.SquidPrinterRenderer;
import com.nobodiiiii.createbiotech.content.universaljoint.UniversalJointRenderer;
import com.nobodiiiii.createbiotech.content.wirelessterminal.WirelessStockKeeperRequestMenu;
import com.nobodiiiii.createbiotech.content.wirelessterminal.WirelessStockKeeperRequestScreen;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestMenu;
import com.yision.phantom.block.phantomport.PhantomPortScreen;
import com.yision.phantom.client.gui.hud.AirCourierHudOverlay;
import com.yision.phantom.client.render.AirCourierEntityRenderer;
import com.yision.phantom.client.render.MiniPhantomItemRenderer;
import com.yision.phantom.item.miniphantom.MiniPhantomScreen;
import com.yision.phantom.item.storagecard.StorageChannelExtensionCardItem;
import com.nobodiiiii.createbiotech.foundation.item.RenderedLivingEntityItem;
import com.nobodiiiii.createbiotech.foundation.ponder.CreateBiotechPonderPlugin;
import com.nobodiiiii.createbiotech.foundation.render.RenderedLivingEntityItemRenderer;
import com.nobodiiiii.createbiotech.client.particle.StraightEnchantParticle;
import com.nobodiiiii.createbiotech.client.render.SlimeMimicRenderLayer;
import com.nobodiiiii.createbiotech.registry.CBBlocks;
import com.nobodiiiii.createbiotech.registry.CBBlockEntityTypes;
import com.nobodiiiii.createbiotech.registry.CBEntityTypes;
import com.nobodiiiii.createbiotech.registry.CBFluids;
import com.nobodiiiii.createbiotech.registry.CBItems;
import com.nobodiiiii.createbiotech.registry.CBMenuTypes;
import com.nobodiiiii.createbiotech.registry.CBParticleTypes;
import com.nobodiiiii.createbiotech.client.CasingConnectedHorizontalCTBehaviour;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogVisual;
import com.simibubi.create.content.kinetics.transmission.SplitShaftVisual;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.item.TooltipModifier;

import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.fluids.FluidStack;

@EventBusSubscriber(modid = CreateBiotech.MOD_ID, value = Dist.CLIENT)
public class CreateBiotechClient {
	private static final ResourceLocation EXPERIENCE_STILL_TEXTURE =
		CreateBiotech.asResource("fluid/experience_still");
	private static final ResourceLocation EXPERIENCE_FLOW_TEXTURE =
		CreateBiotech.asResource("fluid/experience_flow");
	private static final ResourceLocation LIQUID_LIVING_SLIME_STILL_TEXTURE =
		CreateBiotech.asResource("fluid/liquid_living_slime_still");
	private static final ResourceLocation LIQUID_LIVING_SLIME_FLOW_TEXTURE =
		CreateBiotech.asResource("fluid/liquid_living_slime_flow");
	private static final Vector3f LIQUID_LIVING_SLIME_FOG_COLOR = new Vector3f(0.48F, 0.86F, 0.42F);


	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(CBBlockEntityTypes.EVOKER_ENCHANTING_CHAMBER.get(),
			EvokerEnchantingChamberRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.EXPERIENCE_PUMP.get(), ExperiencePumpRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SQUID_PRINTER.get(), SquidPrinterRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SLIME_BELT.get(), SlimeBeltRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.MAGMA_BELT.get(), MagmaBeltRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.POWER_BELT.get(), PowerBeltRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.UNIVERSAL_JOINT.get(), UniversalJointRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SLIME_CLUTCH.get(), SplitShaftRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SCHRODINGERS_CAT.get(), SchrodingersCatRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SPIDER_ASSEMBLY_TABLE.get(),
			SpiderAssemblyTableRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SPIDER_ASSEMBLY_TABLE_COG.get(),
			SpiderAssemblyTableCogRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.FIXED_CARROT_FISHING_ROD.get(),
			FixedCarrotFishingRodRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.BLAST_PROOF_CHAIN_DRIVE.get(),
			BlastProofChainDriveRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.CREEPER_BLAST_CHAMBER.get(),
			CreeperBlastChamberRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.BIO_PACKAGER.get(), BioPackagerRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SHULKER_PACKAGER.get(), ShulkerPackagerRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.SHULKER_TELEPORTER.get(), ShulkerTeleporterRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.BONE_RATCHET.get(), BoneRatchetRenderer::new);
		event.registerBlockEntityRenderer(CBBlockEntityTypes.PETRI_DISH.get(), PetriDishRenderer::new);
		event.registerEntityRenderer(CBEntityTypes.CARDBOARD_BOX.get(), CardboardBoxEntityRenderer::new);
		event.registerEntityRenderer(CBEntityTypes.AIR_COURIER.get(), AirCourierEntityRenderer::new);
	}

	@SubscribeEvent
	public static void addEntityRenderLayers(EntityRenderersEvent.AddLayers event) {
		SlimeMimicRenderLayer.registerOnAll(Minecraft.getInstance().getEntityRenderDispatcher());
	}

	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/universal_joint_endpoint_slime_overlay")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/blast_chamber_display/panel")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/blast_chamber_display/dial")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/blast_chamber_display/creeper_face")));
		event.register(ModelResourceLocation.standalone(BoneRatchetRenderer.COGWHEEL_MODEL_LOCATION));
		event.register(ModelResourceLocation.standalone(ExperiencePumpRenderer.COG_MODEL_LOCATION));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/schrodingers_cat/redstone_torch_on")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/schrodingers_cat/redstone_torch_off")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/spider_assembly_table/body")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/spider_assembly_table/head")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/spider_assembly_table/abdomen")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/spider_assembly_table/leg")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/bio_packager/hatch_open")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/bio_packager/hatch_closed")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/bio_packager/tray")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/shulker_packager/hatch_open")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/shulker_packager/hatch_closed")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("block/shulker_packager/tray")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/shulker_package")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/cardboard_box")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/small_cardboard_box")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/small_cardboard_box_captured")));
		event.register(ModelResourceLocation.standalone(CardboardBoxPartials.SMALL_BOX_LOGISTICS_LOCATION));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/large_cardboard_box")));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/large_cardboard_box_captured")));
		event.register(ModelResourceLocation.standalone(CardboardBoxPartials.LARGE_BOX_LOGISTICS_LOCATION));
		event.register(ModelResourceLocation.standalone(CreateBiotech.asResource("item/mini_phantom_package")));
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, CreateBiotech.asResource("air_courier_hud"),
			AirCourierHudOverlay.INSTANCE);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		CardboardBoxClientExtensions.register(event);
		SpiderAssemblyTableClientExtensions.register(event);
		event.registerBlock(new MagmaBeltBlock.RenderProperties(), CBBlocks.MAGMA_BELT.get());

		Item miniPhantom = CBItems.MINI_PHANTOM.get();
		event.registerItem(SimpleCustomRenderer.create(miniPhantom, new MiniPhantomItemRenderer()), miniPhantom);

		Item squidPrinter = CBItems.SQUID_PRINTER.get();
		event.registerItem(SimpleCustomRenderer.create(squidPrinter, new SquidPrinterItemRenderer()), squidPrinter);

		Item evokerEnchantingChamber = CBItems.EVOKER_ENCHANTING_CHAMBER.get();
		event.registerItem(SimpleCustomRenderer.create(evokerEnchantingChamber,
			new EvokerEnchantingChamberItemRenderer()), evokerEnchantingChamber);

		registerRenderedLivingEntityItem(event, CBItems.CAPTURED_SMALL_SLIME.get());
		registerRenderedLivingEntityItem(event, CBItems.CAPTURED_PHANTOM.get());

		event.registerFluidType(createExperienceFluidExtensions(), CBFluids.EXPERIENCE_TYPE.get());
		event.registerFluidType(createLiquidLivingSlimeExtensions(), CBFluids.LIQUID_LIVING_SLIME_TYPE.get());
	}

	private static void registerRenderedLivingEntityItem(RegisterClientExtensionsEvent event, Item item) {
		if (!(item instanceof RenderedLivingEntityItem<?> renderedItem))
			throw new IllegalStateException("Expected rendered living entity item: " + item);
		event.registerItem(RenderedLivingEntityItemRenderer.create(renderedItem), item);
	}

	private static IClientFluidTypeExtensions createExperienceFluidExtensions() {
		return new IClientFluidTypeExtensions() {
			@Override
			public ResourceLocation getStillTexture() {
				return EXPERIENCE_STILL_TEXTURE;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return EXPERIENCE_FLOW_TEXTURE;
			}
		};
	}

	private static IClientFluidTypeExtensions createLiquidLivingSlimeExtensions() {
		return new IClientFluidTypeExtensions() {
			@Override
			public ResourceLocation getStillTexture() {
				return LIQUID_LIVING_SLIME_STILL_TEXTURE;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return LIQUID_LIVING_SLIME_FLOW_TEXTURE;
			}

			@Override
			public int getTintColor() {
				return 0xFFFFFFFF;
			}

			@Override
			public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
				return 0xFFFFFFFF;
			}

			@Override
			public int getTintColor(FluidStack stack) {
				return 0xFFFFFFFF;
			}

			@Override
			public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance,
				float darkenWorldAmount, Vector3f fluidFogColor) {
				return LIQUID_LIVING_SLIME_FOG_COLOR;
			}

			@Override
			public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
				float nearDistance, float farDistance, FogShape shape) {
				RenderSystem.setShaderFogShape(FogShape.CYLINDER);
				RenderSystem.setShaderFogStart(-8.0F);
				RenderSystem.setShaderFogEnd(9.6F);
			}
		};
	}

	@SubscribeEvent
	public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		SlimeBeltSpriteShifts.init();
		MagmaBeltSpriteShifts.init();
		PowerBeltSpriteShifts.init();
		CBSpriteShifts.init();
		event.registerReloadListener(new ResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				SlimeMimicRenderLayer.clearCachedTextureData();
			}
		});
		event.registerReloadListener(SlimeBeltHelper.LISTENER);
		event.registerReloadListener(MagmaBeltHelper.LISTENER);
	}

	@SubscribeEvent
	public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(CBParticleTypes.STRAIGHT_ENCHANT.get(), StraightEnchantParticle.Provider::new);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(CBMenuTypes.SPIDER_ASSEMBLY_TABLE.get(), SpiderAssemblyTableScreen::new);
		event.register(CBMenuTypes.PHANTOMPORT.get(), PhantomPortScreen::new);
		event.register(CBMenuTypes.MINI_PHANTOM.get(), MiniPhantomScreen::new);
		event.register(CBMenuTypes.SHULKER_TELEPORTER.get(), ShulkerTeleporterScreen::new);
		event.<StockKeeperRequestMenu, WirelessStockKeeperRequestScreen>register(
			CBMenuTypes.WIRELESS_STOCK_KEEPER_REQUEST.get(),
			(menu, inventory, title) -> new WirelessStockKeeperRequestScreen(
				(WirelessStockKeeperRequestMenu) menu, inventory, title));
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {

		event.enqueueWork(() -> {
			registerItemTooltips();
			registerCardboardBoxModelProperties();
			PonderIndex.addPlugin(new CreateBiotechPonderPlugin());
			CardboardBoxPartials.register();
			ShulkerPackagePartials.register();
			SimpleBlockEntityVisualizer.builder(CBBlockEntityTypes.BIO_PACKAGER.get())
				.factory(BioPackagerVisual::new)
				.neverSkipVanillaRender()
				.apply();
			SimpleBlockEntityVisualizer.builder(CBBlockEntityTypes.SHULKER_PACKAGER.get())
				.factory(ShulkerPackagerVisual::new)
				.neverSkipVanillaRender()
				.apply();
			SimpleBlockEntityVisualizer.builder(CBBlockEntityTypes.BONE_RATCHET.get())
				.factory((context, blockEntity, partialTick) -> new EncasedCogVisual(context, blockEntity, false,
					partialTick, Models.partial(BoneRatchetRenderer.COGWHEEL)))
				.apply();
			SimpleBlockEntityVisualizer.builder(CBBlockEntityTypes.SLIME_CLUTCH.get())
				.factory(SplitShaftVisual::new)
				.apply();
			ItemBlockRenderTypes.setRenderLayer(CBFluids.LIQUID_LIVING_SLIME.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(CBFluids.LIQUID_LIVING_SLIME_FLOWING.get(), RenderType.translucent());
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(Create.asResource("andesite_belt_funnel"), SlimeBeltFunnelModel::new);
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(Create.asResource("brass_belt_funnel"), SlimeBeltFunnelModel::new);
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("magma_belt"),
					com.simibubi.create.content.kinetics.belt.BeltModel::new);
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("power_belt"),
					com.simibubi.create.content.kinetics.belt.BeltModel::new);
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("asurine_casing"),
					model -> new CTModel(model, new EncasedCTBehaviour(CBSpriteShifts.ASURINE_CASING)));
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("biotech_casing"),
					model -> new CTModel(model, new EncasedCTBehaviour(CBSpriteShifts.BIOTECH_CASING)));
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("explosion_proof_casing"),
					model -> new CTModel(model, new CasingConnectedHorizontalCTBehaviour(
						CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE, CBSpriteShifts.EXPLOSION_PROOF_CASING)));
			CreateClient.CASING_CONNECTIVITY.makeCasing(CBBlocks.ASURINE_CASING.get(),
				CBSpriteShifts.ASURINE_CASING);
			CreateClient.CASING_CONNECTIVITY.makeCasing(CBBlocks.BIOTECH_CASING.get(),
				CBSpriteShifts.BIOTECH_CASING);
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("creeper_blast_chamber"),
					model -> new CTModel(model, new CasingConnectedHorizontalCTBehaviour(
						CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE, CBSpriteShifts.EXPLOSION_PROOF_CASING)));
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("explosion_proof_item_vault"),
					model -> new CTModel(model, new ExplosionProofItemVaultCTBehaviour()));
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("blast_proof_chain_drive"),
					model -> new CTModel(model,
						new EncasedCTBehaviour(CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE)));
			CreateClient.MODEL_SWAPPER.getCustomBlockModels()
				.register(CreateBiotech.asResource("blast_proof_framed_glass"),
					model -> new CTModel(model, new SimpleCTBehaviour(CBSpriteShifts.BLAST_PROOF_FRAMED_GLASS)));
			CreateClient.CASING_CONNECTIVITY.makeCasing(CBBlocks.EXPLOSION_PROOF_CASING.get(),
				CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE);
			CreateClient.CASING_CONNECTIVITY.make(CBBlocks.CREEPER_BLAST_CHAMBER.get(),
				CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE,
				(state, face) -> state.hasProperty(CreeperBlastChamberBlock.FORMED)
					&& state.getValue(CreeperBlastChamberBlock.FORMED));
			CreateClient.CASING_CONNECTIVITY.make(CBBlocks.BLAST_PROOF_CHAIN_DRIVE.get(),
				CBSpriteShifts.EXPLOSION_PROOF_CASING_SIDE,
				(state, face) -> face.getAxis() != state.getValue(BlockStateProperties.AXIS));
		});
	}

	private static void registerItemTooltips() {
		ItemDescription.useKey(CBItems.SMALL_EXPERIENCE_BUD.get(), "block.create_biotech.experience_bud");
		ItemDescription.useKey(CBItems.MEDIUM_EXPERIENCE_BUD.get(), "block.create_biotech.experience_bud");
		ItemDescription.useKey(CBItems.LARGE_EXPERIENCE_BUD.get(), "block.create_biotech.experience_bud");
		CBItems.BUFFER_PADS.values()
			.forEach(entry -> ItemDescription.useKey(entry.get(), "block.create_biotech.buffer_pad"));

		registerCreateStyleTooltip(CBItems.BUDDING_EXPERIENCE.get());
		registerCreateStyleTooltip(CBItems.SMALL_EXPERIENCE_BUD.get());
		registerCreateStyleTooltip(CBItems.MEDIUM_EXPERIENCE_BUD.get());
		registerCreateStyleTooltip(CBItems.LARGE_EXPERIENCE_BUD.get());
		registerCreateStyleTooltip(CBItems.EXPERIENCE_CLUSTER.get());
		registerCreateStyleTooltip(CBItems.PETRI_DISH.get());
		registerCreateStyleTooltip(CBItems.CARDBOARD_BOX.get(), CapturedEntityBoxHelper::hasCapturedEntity);
		registerCreateStyleTooltip(CBItems.LARGE_CARDBOARD_BOX.get(), CapturedEntityBoxHelper::hasCapturedEntity);
		registerCreateStyleTooltip(CBItems.CAPTURED_SMALL_SLIME.get());
		registerCreateStyleTooltip(CBItems.SMART_SUPER_GLUE.get());
		registerCreateStyleTooltip(CBItems.FIXED_CARROT_FISHING_ROD.get());
		registerCreateStyleTooltip(CBItems.WIRELESS_TERMINAL.get());
		registerCreateStyleTooltip(CBItems.STORAGE_CHANNEL_EXTENSION_CARD.get());
		registerCreateStyleTooltip(CBItems.SHULKER_PACKAGER.get());
		registerCreateStyleTooltip(CBItems.SHULKER_TELEPORTER.get());
		registerCreateStyleTooltip(CBItems.PHANTOMPORT.get());
		registerCreateStyleTooltip(CBItems.MINI_PHANTOM.get());
		CBItems.BUFFER_PADS.values()
			.forEach(entry -> registerCreateStyleTooltip(entry.get()));
	}

	private static void registerCreateStyleTooltip(Item item) {
		registerCreateStyleTooltip(item, stack -> false);
	}

	private static void registerCreateStyleTooltip(Item item, Predicate<ItemStack> skipCondition) {
		TooltipModifier description = new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE);
		TooltipModifier.REGISTRY.register(item, context -> {
			if (skipCondition.test(context.getItemStack()))
				return;
			description.modify(context);
		});
	}

	private static void registerCardboardBoxModelProperties() {
		ItemProperties.register(CBItems.CARDBOARD_BOX.get(), CreateBiotech.asResource("captured"),
			(stack, level, entity, seed) -> CapturedEntityBoxHelper.hasCapturedEntity(stack) ? 1.0f : 0.0f);
		ItemProperties.register(CBItems.LARGE_CARDBOARD_BOX.get(), CreateBiotech.asResource("captured"),
			(stack, level, entity, seed) -> CapturedEntityBoxHelper.hasCapturedEntity(stack) ? 1.0f : 0.0f);
		ItemProperties.register(CBItems.STORAGE_CHANNEL_EXTENSION_CARD.get(), CreateBiotech.asResource("linked"),
			(stack, level, entity, seed) -> StorageChannelExtensionCardItem.isLinked(stack) ? 1.0f : 0.0f);
	}
}
