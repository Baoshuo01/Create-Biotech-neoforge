package com.nobodiiiii.createbiotech.content.cardboardbox;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nobodiiiii.createbiotech.registry.CBItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

public final class CapturedEntityBoxIngredient implements ICustomIngredient {
	public static final MapCodec<CapturedEntityBoxIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BuiltInRegistries.ENTITY_TYPE.byNameCodec()
			.fieldOf("entity")
			.forGetter(CapturedEntityBoxIngredient::getEntityType),
		BuiltInRegistries.ITEM.byNameCodec()
			.optionalFieldOf("item")
			.forGetter(CapturedEntityBoxIngredient::serializedSingleItem),
		BuiltInRegistries.ITEM.byNameCodec()
			.listOf()
			.optionalFieldOf("items", List.of())
			.forGetter(CapturedEntityBoxIngredient::serializedItems)
	).apply(instance, CapturedEntityBoxIngredient::fromSerialized));
	public static final StreamCodec<RegistryFriendlyByteBuf, CapturedEntityBoxIngredient> STREAM_CODEC =
		ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
	public static final IngredientType<CapturedEntityBoxIngredient> TYPE = new IngredientType<>(CODEC, STREAM_CODEC);

	private final Set<Item> items;
	private final EntityType<?> entityType;

	private CapturedEntityBoxIngredient(Set<Item> items, EntityType<?> entityType) {
		if (items.isEmpty())
			throw new IllegalArgumentException("Captured entity box ingredient must accept at least one item");
		for (Item item : items)
			requireBoxItem(item);
		this.items = Collections.unmodifiableSet(new LinkedHashSet<>(items));
		this.entityType = Objects.requireNonNull(entityType, "entityType");
	}

	public static CapturedEntityBoxIngredient of(EntityType<?> entityType) {
		return new CapturedEntityBoxIngredient(defaultItems(), entityType);
	}

	public static CapturedEntityBoxIngredient of(EntityType<?> entityType, Item... items) {
		return new CapturedEntityBoxIngredient(Arrays.stream(items)
			.collect(Collectors.toCollection(LinkedHashSet::new)), entityType);
	}

	private static CapturedEntityBoxIngredient fromSerialized(EntityType<?> entityType, Optional<Item> item,
		List<Item> items) {
		if (item.isPresent() && !items.isEmpty())
			throw new IllegalArgumentException("Captured entity box ingredient cannot define both 'item' and 'items'");
		if (item.isPresent())
			return new CapturedEntityBoxIngredient(Set.of(item.get()), entityType);
		if (!items.isEmpty())
			return new CapturedEntityBoxIngredient(new LinkedHashSet<>(items), entityType);
		return of(entityType);
	}

	public EntityType<?> getEntityType() {
		return entityType;
	}

	private static Set<Item> defaultItems() {
		return Set.of(CBItems.CARDBOARD_BOX.get(), CBItems.LARGE_CARDBOARD_BOX.get());
	}

	private Optional<Item> serializedSingleItem() {
		return items.size() == 1 ? Optional.of(items.iterator().next()) : Optional.empty();
	}

	private List<Item> serializedItems() {
		return items.size() > 1 ? List.copyOf(items) : List.of();
	}

	@Override
	public boolean test(ItemStack stack) {
		return !stack.isEmpty() && items.contains(stack.getItem())
			&& CapturedEntityBoxHelper.containsEntityType(stack, entityType);
	}

	@Override
	public Stream<ItemStack> getItems() {
		return items.stream()
			.map(Item::getDefaultInstance)
			.map(stack -> CapturedEntityBoxHelper.createDisplayStack(stack, entityType));
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IngredientType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof CapturedEntityBoxIngredient other))
			return false;
		return items.equals(other.items) && entityType == other.entityType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(items, entityType);
	}

	private static Item requireBoxItem(Item item) {
		if (!(item instanceof CapturedEntityBoxItem)) {
			throw new IllegalArgumentException("Item " + BuiltInRegistries.ITEM.getKey(item)
				+ " is not a captured entity box item");
		}
		return item;
	}
}