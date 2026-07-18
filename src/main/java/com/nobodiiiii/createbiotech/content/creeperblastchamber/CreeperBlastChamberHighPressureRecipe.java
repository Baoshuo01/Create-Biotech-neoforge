package com.nobodiiiii.createbiotech.content.creeperblastchamber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nobodiiiii.createbiotech.registry.CBRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.codec.CreateCodecs;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class CreeperBlastChamberHighPressureRecipe
	extends ProcessingRecipe<RecipeWrapper, CreeperBlastChamberHighPressureRecipe.Params> {

	private static final IRecipeTypeInfo TYPE_INFO = new IRecipeTypeInfo() {
		@Override
		public ResourceLocation getId() {
			return CBRecipeTypes.CREEPER_BLAST_CHAMBER_HIGH_PRESSURE_TYPE.getId();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends RecipeSerializer<?>> T getSerializer() {
			return (T) CBRecipeTypes.CREEPER_BLAST_CHAMBER_HIGH_PRESSURE_SERIALIZER.get();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
			return (RecipeType<R>) CBRecipeTypes.CREEPER_BLAST_CHAMBER_HIGH_PRESSURE_TYPE.get();
		}
	};

	public static final MapCodec<CreeperBlastChamberHighPressureRecipe> CODEC =
		ProcessingRecipe.codec(CreeperBlastChamberHighPressureRecipe::new, Params.CODEC);
	public static final StreamCodec<RegistryFriendlyByteBuf, CreeperBlastChamberHighPressureRecipe> STREAM_CODEC =
		ProcessingRecipe.streamCodec(CreeperBlastChamberHighPressureRecipe::new, Params.STREAM_CODEC);

	private static final RandomSource RANDOM = RandomSource.create();

	private final List<ResultCountRange> resultCountRanges;
	private final boolean exclusiveResults;

	public CreeperBlastChamberHighPressureRecipe(Params params) {
		super(TYPE_INFO, params);
		resultCountRanges = new ArrayList<>(params.resultCountRanges);
		exclusiveResults = params.exclusiveResults;
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level level) {
		if (inv.isEmpty())
			return false;
		return !ingredients.isEmpty() && ingredients.getFirst().test(inv.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 8;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	public List<ItemStack> rollResults() {
		return rollResults(RANDOM);
	}

	public List<ItemStack> rollResults(List<ProcessingOutput> rollableResults) {
		return rollResults(rollableResults, RANDOM);
	}

	@Override
	public List<ItemStack> rollResults(RandomSource random) {
		return rollResults(getRollableResults(), random);
	}

	@Override
	public List<ItemStack> rollResults(List<ProcessingOutput> rollableResults, RandomSource random) {
		if (exclusiveResults)
			return rollExclusiveResult(rollableResults, random);

		List<ItemStack> rolledResults = new ArrayList<>();
		for (int i = 0; i < rollableResults.size(); i++) {
			ProcessingOutput output = rollableResults.get(i);
			ResultCountRange range = getResultCountRange(i);

			ItemStack stack;
			if (range == null) {
				stack = output.rollOutput(random);
			} else {
				if (random.nextFloat() > output.getChance())
					continue;
				stack = output.getStack().copy();
				stack.setCount(range.roll(random));
			}

			if (!stack.isEmpty())
				rolledResults.add(stack);
		}
		return rolledResults;
	}

	private List<ItemStack> rollExclusiveResult(List<ProcessingOutput> rollableResults, RandomSource random) {
		float totalWeight = 0;
		for (ProcessingOutput output : rollableResults)
			totalWeight += Math.max(0, output.getChance());
		if (totalWeight <= 0)
			return List.of();

		float selectedWeight = random.nextFloat() * totalWeight;
		for (int i = 0; i < rollableResults.size(); i++) {
			ProcessingOutput output = rollableResults.get(i);
			float weight = Math.max(0, output.getChance());
			if (selectedWeight >= weight) {
				selectedWeight -= weight;
				continue;
			}

			ItemStack stack = output.getStack().copy();
			ResultCountRange range = getResultCountRange(i);
			if (range != null)
				stack.setCount(range.roll(random));
			return stack.isEmpty() ? List.of() : List.of(stack);
		}
		return List.of();
	}

	public ResultCountRange getResultCountRange(int index) {
		return index >= 0 && index < resultCountRanges.size() ? resultCountRanges.get(index) : null;
	}

	public boolean hasExclusiveResults() {
		return exclusiveResults;
	}

	public static class Serializer implements RecipeSerializer<CreeperBlastChamberHighPressureRecipe> {
		@Override
		public MapCodec<CreeperBlastChamberHighPressureRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CreeperBlastChamberHighPressureRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}

	public static class Params extends ProcessingRecipeParams {
		private static final Codec<Either<SizedFluidIngredient, Ingredient>> INGREDIENT_CODEC =
			Codec.either(CreateCodecs.FLAT_SIZED_FLUID_INGREDIENT_WITH_TYPE, Ingredient.CODEC);
		private static final Codec<Either<FluidStack, RangedProcessingOutput>> RESULT_CODEC =
			Codec.either(FluidStack.CODEC, RangedProcessingOutput.CODEC);

		public static final MapCodec<Params> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			INGREDIENT_CODEC.listOf()
				.fieldOf("ingredients")
				.forGetter(Params::ingredientsForCodec),
			RESULT_CODEC.listOf()
				.fieldOf("results")
				.forGetter(Params::resultsForCodec),
			Codec.INT.optionalFieldOf("processing_time", 0)
				.forGetter(params -> params.processingDuration),
			HeatCondition.CODEC.optionalFieldOf("heat_requirement", HeatCondition.NONE)
				.forGetter(params -> params.requiredHeat),
			Codec.BOOL.optionalFieldOf("exclusive_results", false)
				.forGetter(params -> params.exclusiveResults)
		).apply(instance, Params::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, Params> STREAM_CODEC = streamCodec(Params::new);

		private final List<ResultCountRange> resultCountRanges = new ArrayList<>();
		private boolean exclusiveResults;

		private Params() {
		}

		private Params(List<Either<SizedFluidIngredient, Ingredient>> ingredients,
			List<Either<FluidStack, RangedProcessingOutput>> results, int processingDuration,
			HeatCondition requiredHeat, boolean exclusiveResults) {
			for (Either<SizedFluidIngredient, Ingredient> ingredient : ingredients)
				ingredient.ifLeft(fluidIngredients::add).ifRight(this.ingredients::add);
			for (Either<FluidStack, RangedProcessingOutput> result : results) {
				result.ifLeft(fluidResults::add).ifRight(output -> {
					this.results.add(output.output());
					resultCountRanges.add(output.range());
				});
			}
			this.processingDuration = processingDuration;
			this.requiredHeat = requiredHeat;
			this.exclusiveResults = exclusiveResults;
		}

		private List<Either<SizedFluidIngredient, Ingredient>> ingredientsForCodec() {
			List<Either<SizedFluidIngredient, Ingredient>> encoded =
				new ArrayList<>(ingredients.size() + fluidIngredients.size());
			ingredients.forEach(ingredient -> encoded.add(Either.right(ingredient)));
			fluidIngredients.forEach(ingredient -> encoded.add(Either.left(ingredient)));
			return encoded;
		}

		private List<Either<FluidStack, RangedProcessingOutput>> resultsForCodec() {
			List<Either<FluidStack, RangedProcessingOutput>> encoded =
				new ArrayList<>(results.size() + fluidResults.size());
			for (int i = 0; i < results.size(); i++)
				encoded.add(Either.right(new RangedProcessingOutput(results.get(i), rangeAt(i))));
			fluidResults.forEach(result -> encoded.add(Either.left(result)));
			return encoded;
		}

		private ResultCountRange rangeAt(int index) {
			return index < resultCountRanges.size() ? resultCountRanges.get(index) : null;
		}

		@Override
		protected void encode(RegistryFriendlyByteBuf buffer) {
			super.encode(buffer);
			buffer.writeVarInt(resultCountRanges.size());
			for (ResultCountRange range : resultCountRanges) {
				buffer.writeBoolean(range != null);
				if (range != null) {
					buffer.writeVarInt(range.min());
					buffer.writeVarInt(range.max());
				}
			}
			buffer.writeBoolean(exclusiveResults);
		}

		@Override
		protected void decode(RegistryFriendlyByteBuf buffer) {
			super.decode(buffer);
			resultCountRanges.clear();
			int rangeCount = buffer.readVarInt();
			for (int i = 0; i < rangeCount; i++) {
				if (buffer.readBoolean())
					resultCountRanges.add(new ResultCountRange(buffer.readVarInt(), buffer.readVarInt()));
				else
					resultCountRanges.add(null);
			}
			exclusiveResults = buffer.readBoolean();
		}
	}

	private record RangedProcessingOutput(ProcessingOutput output, ResultCountRange range) {
		private static final Codec<RangedProcessingOutput> CODEC = RecordCodecBuilder.<RangedProcessingOutput>create(instance -> instance.group(
			BuiltInRegistries.ITEM.byNameCodec()
				.fieldOf("id")
				.forGetter((RangedProcessingOutput value) -> value.output().getStack().getItem()),
			Codec.INT.optionalFieldOf("count", 1)
				.forGetter((RangedProcessingOutput value) -> value.output().getStack().getCount()),
			DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
				.forGetter((RangedProcessingOutput value) -> value.output().getStack().getComponentsPatch()),
			Codec.FLOAT.optionalFieldOf("chance", 1.0f)
				.forGetter((RangedProcessingOutput value) -> value.output().getChance()),
			Codec.INT.optionalFieldOf("count_min")
				.forGetter((RangedProcessingOutput value) -> Optional.ofNullable(value.range()).map(ResultCountRange::min)),
			Codec.INT.optionalFieldOf("count_max")
				.forGetter((RangedProcessingOutput value) -> Optional.ofNullable(value.range()).map(ResultCountRange::max))
		).apply(instance, RangedProcessingOutput::create)).flatXmap(RangedProcessingOutput::validate,
			RangedProcessingOutput::validate);

		private static RangedProcessingOutput create(Item item, int count, DataComponentPatch components, float chance,
			Optional<Integer> countMin, Optional<Integer> countMax) {
			ResultCountRange range = null;
			if (countMin.isPresent() || countMax.isPresent()) {
				int min = countMin.orElse(count);
				int max = countMax.orElse(min);
				range = new ResultCountRange(min, max);
			}
			return new RangedProcessingOutput(new ProcessingOutput(item, count, components, chance), range);
		}

		private static DataResult<RangedProcessingOutput> validate(RangedProcessingOutput value) {
			ItemStack stack = value.output.getStack();
			if (stack.isEmpty())
				return DataResult.error(() -> "High-pressure implosion recipe output cannot be empty");
			if (stack.getCount() <= 0)
				return DataResult.error(() -> "High-pressure implosion recipe output count must be positive");
			if (value.output.getChance() < 0 || value.output.getChance() > 1)
				return DataResult.error(() -> "High-pressure implosion recipe output chance must be between 0 and 1");
			if (value.range != null && (value.range.min() <= 0 || value.range.max() < value.range.min()))
				return DataResult.error(() -> "Invalid high-pressure implosion result count range: "
					+ value.range.min() + "-" + value.range.max());
			return DataResult.success(value);
		}
	}

	public record ResultCountRange(int min, int max) {
		public int roll(RandomSource random) {
			return min + random.nextInt(max - min + 1);
		}
	}
}
