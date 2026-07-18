package com.nobodiiiii.createbiotech.registry;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.content.fluid.LiquidLivingSlimeFluidType;
import com.simibubi.create.content.fluids.VirtualFluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CBFluids {

	public static final DeferredRegister<FluidType> FLUID_TYPES =
		DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, CreateBiotech.MOD_ID);

	public static final DeferredRegister<Fluid> FLUIDS =
		DeferredRegister.create(Registries.FLUID, CreateBiotech.MOD_ID);

	public static final DeferredRegister<Block> FLUID_BLOCKS =
		DeferredRegister.create(Registries.BLOCK, CreateBiotech.MOD_ID);

	public static final DeferredRegister<Item> FLUID_ITEMS =
		DeferredRegister.create(Registries.ITEM, CreateBiotech.MOD_ID);


	public static final DeferredHolder<FluidType, FluidType> EXPERIENCE_TYPE =
		FLUID_TYPES.register("experience",
			() -> new FluidType(FluidType.Properties.create()
				.lightLevel(15)));

	public static final DeferredHolder<Fluid, VirtualFluid> EXPERIENCE =
		FLUIDS.register("experience", () -> VirtualFluid.createSource(experienceProperties()));

	public static final DeferredHolder<Fluid, VirtualFluid> EXPERIENCE_FLOWING =
		FLUIDS.register("flowing_experience", () -> VirtualFluid.createFlowing(experienceProperties()));

	public static final DeferredHolder<FluidType, LiquidLivingSlimeFluidType> LIQUID_LIVING_SLIME_TYPE =
		FLUID_TYPES.register("liquid_living_slime",
			() -> new LiquidLivingSlimeFluidType(FluidType.Properties.create()
				.motionScale(0.004D)
				.fallDistanceModifier(0F)
				.sound(SoundActions.BUCKET_FILL, SoundEvents.SLIME_JUMP)
				.sound(SoundActions.BUCKET_EMPTY, SoundEvents.SLIME_JUMP)
				.viscosity(5000)
				.density(1400)));

	public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIQUID_LIVING_SLIME =
		FLUIDS.register("liquid_living_slime",
			() -> new BaseFlowingFluid.Source(CBFluids.liquidLivingSlimeProperties()));

	public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> LIQUID_LIVING_SLIME_FLOWING =
		FLUIDS.register("liquid_living_slime_flowing",
			() -> new BaseFlowingFluid.Flowing(CBFluids.liquidLivingSlimeProperties()));

	public static final DeferredHolder<Block, LiquidBlock> LIQUID_LIVING_SLIME_BLOCK =
		FLUID_BLOCKS.register("liquid_living_slime",
			() -> new LiquidBlock(LIQUID_LIVING_SLIME.get(), Block.Properties.of()
				.noCollission()
				.sound(SoundType.SLIME_BLOCK)
				.strength(100f)
				.noLootTable()
				.liquid()));

	public static final DeferredHolder<Item, BucketItem> LIQUID_LIVING_SLIME_BUCKET =
		FLUID_ITEMS.register("liquid_living_slime_bucket",
			() -> new BucketItem(LIQUID_LIVING_SLIME.get(), new Item.Properties()
				.craftRemainder(Items.BUCKET)
				.stacksTo(1)));

	private static BaseFlowingFluid.Properties experienceProperties() {
		return new BaseFlowingFluid.Properties(EXPERIENCE_TYPE, EXPERIENCE, EXPERIENCE_FLOWING);
	}

	private static BaseFlowingFluid.Properties liquidLivingSlimeProperties() {
		return new BaseFlowingFluid.Properties(
			LIQUID_LIVING_SLIME_TYPE,
			LIQUID_LIVING_SLIME,
			LIQUID_LIVING_SLIME_FLOWING)
			.bucket(LIQUID_LIVING_SLIME_BUCKET)
			.block(LIQUID_LIVING_SLIME_BLOCK)
			.levelDecreasePerBlock(2)
			.tickRate(60)
			.slopeFindDistance(4)
			.explosionResistance(100f);
	}

	private CBFluids() {}

	public static void register(IEventBus modEventBus) {
		FLUID_TYPES.register(modEventBus);
		FLUIDS.register(modEventBus);
		FLUID_BLOCKS.register(modEventBus);
		FLUID_ITEMS.register(modEventBus);
	}
}
