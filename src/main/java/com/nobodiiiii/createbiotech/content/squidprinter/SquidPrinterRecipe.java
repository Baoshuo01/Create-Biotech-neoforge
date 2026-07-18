package com.nobodiiiii.createbiotech.content.squidprinter;

import com.mojang.serialization.MapCodec;
import com.nobodiiiii.createbiotech.registry.CBItems;
import com.nobodiiiii.createbiotech.registry.CBRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class SquidPrinterRecipe extends ProcessingRecipe<RecipeWrapper, ProcessingRecipeParams> {

	private static final IRecipeTypeInfo TYPE_INFO = new IRecipeTypeInfo() {
		@Override
		public ResourceLocation getId() {
			return CBRecipeTypes.SQUID_PRINTER_TYPE.getId();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends RecipeSerializer<?>> T getSerializer() {
			return (T) CBRecipeTypes.SQUID_PRINTER_SERIALIZER.get();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
			return (RecipeType<R>) CBRecipeTypes.SQUID_PRINTER_TYPE.get();
		}
	};

	public static final MapCodec<SquidPrinterRecipe> CODEC =
		ProcessingRecipe.codec(SquidPrinterRecipe::new, ProcessingRecipeParams.CODEC);
	public static final StreamCodec<RegistryFriendlyByteBuf, SquidPrinterRecipe> STREAM_CODEC =
		ProcessingRecipe.streamCodec(SquidPrinterRecipe::new, ProcessingRecipeParams.STREAM_CODEC);

	public SquidPrinterRecipe(ProcessingRecipeParams params) {
		super(TYPE_INFO, params);
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level level) {
		return !inv.isEmpty() && !ingredients.isEmpty() && ingredients.getFirst().test(inv.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 1;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 1;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	public SizedFluidIngredient getRequiredFluid() {
		if (fluidIngredients.isEmpty())
			throw new IllegalStateException("Squid Printer recipe has no fluid ingredient");
		return fluidIngredients.getFirst();
	}

	public boolean matchesTemplate(ItemStack template) {
		return EnchantmentBookCopyItem.hasCopyableEnchantments(template);
	}

	public int getTicksPerLevel() {
		return Math.max(1, getProcessingDuration());
	}

	public int getWaterPerLevel() {
		return Math.max(1, getRequiredFluid().amount());
	}

	public int getTemplateLevelTotal(ItemStack template) {
		return Math.max(1, EnchantmentBookCopyItem.sumCopySourceEnchantmentLevels(template));
	}

	public int getRequiredTicks(ItemStack template) {
		return getTicksPerLevel() * getTemplateLevelTotal(template);
	}

	public int getRequiredWater(ItemStack template) {
		return getWaterPerLevel() * getTemplateLevelTotal(template);
	}

	public ItemStack createResult(ItemStack template) {
		return EnchantmentBookCopyItem.fromTemplate(template, CBItems.ENCHANTMENT_BOOK_COPY.get());
	}

	public static class Serializer implements RecipeSerializer<SquidPrinterRecipe> {
		@Override
		public MapCodec<SquidPrinterRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SquidPrinterRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
