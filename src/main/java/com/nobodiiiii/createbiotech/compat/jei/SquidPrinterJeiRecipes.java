package com.nobodiiiii.createbiotech.compat.jei;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.content.squidprinter.EnchantmentBookCopyItem;
import com.nobodiiiii.createbiotech.content.squidprinter.SquidPrinterRecipe;
import com.nobodiiiii.createbiotech.registry.CBItems;
import com.nobodiiiii.createbiotech.registry.CBRecipeTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public final class SquidPrinterJeiRecipes {

	private static final String ITEM_APPLICATION_PREFIX = "item_application/squid_printer/";
	private static final String SPOUT_FILLING_PREFIX = "spout_filling/squid_printer/";

	private SquidPrinterJeiRecipes() {
	}

	public static List<SquidPrinterJeiRecipe> create() {
		List<RecipeHolder<SquidPrinterRecipe>> recipes = getRecipes();
		if (recipes.isEmpty())
			return List.of();

		List<EnchantmentEntry> entries = createEnchantmentEntries();
		List<SquidPrinterJeiRecipe> displays = new ArrayList<>(recipes.size() * entries.size());
		for (RecipeHolder<SquidPrinterRecipe> recipeHolder : recipes) {
			SquidPrinterRecipe recipe = recipeHolder.value();
			for (EnchantmentEntry entry : entries) {
				ResourceLocation recipeId = recipeHolder.id();
				ResourceLocation id = ResourceLocation.fromNamespaceAndPath(recipeId.getNamespace(),
					recipeId.getPath() + "/" + entry.idSegment());
				displays.add(new SquidPrinterJeiRecipe(id, new ItemStack(Items.BOOK), recipe.getRequiredFluid(),
					entry.templateBooks(), entry.outputCopies()));
			}
		}
		return displays;
	}

	public static boolean isSquidPrinterItemApplication(ResourceLocation id) {
		return id.getNamespace()
			.equals(CreateBiotech.MOD_ID) && id.getPath()
				.startsWith(ITEM_APPLICATION_PREFIX);
	}

	public static boolean isSquidPrinterSpoutFilling(ResourceLocation id) {
		return id.getNamespace()
			.equals(CreateBiotech.MOD_ID) && id.getPath()
				.startsWith(SPOUT_FILLING_PREFIX);
	}

	private static List<RecipeHolder<SquidPrinterRecipe>> getRecipes() {
		ClientPacketListener connection = Minecraft.getInstance()
			.getConnection();
		if (connection == null)
			return List.of();
		return connection.getRecipeManager()
			.getAllRecipesFor(CBRecipeTypes.SQUID_PRINTER_TYPE.get());
	}

	private static List<EnchantmentEntry> createEnchantmentEntries() {
		ClientLevel level = Minecraft.getInstance().level;
		List<EnchantmentEntry> entries = new ArrayList<>();
		if (level != null) {
			Registry<Enchantment> enchantments = level.registryAccess()
				.registryOrThrow(Registries.ENCHANTMENT);
			for (Holder.Reference<Enchantment> enchantmentHolder : enchantments.holders()
				.sorted(Comparator.comparing(holder -> holder.key().location()))
				.toList()) {
				ResourceLocation enchantmentId = enchantmentHolder.key().location();
				int maxLevel = Math.max(1, enchantmentHolder.value().getMaxLevel());
				List<ItemStack> templates = new ArrayList<>(maxLevel);
				for (int enchantmentLevel = 1; enchantmentLevel <= maxLevel; enchantmentLevel++)
					templates.add(enchantedBook(enchantmentHolder, enchantmentLevel));
				List<ItemStack> outputs = templates.stream()
					.map(template -> EnchantmentBookCopyItem.fromTemplate(template, CBItems.ENCHANTMENT_BOOK_COPY.get()))
					.toList();
				entries.add(new EnchantmentEntry(
					enchantmentId.getNamespace() + "_" + enchantmentId.getPath(), templates, outputs));
			}
		}
		if (entries.isEmpty()) {
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
			ItemStack copy = new ItemStack(CBItems.ENCHANTMENT_BOOK_COPY.get());
			entries.add(new EnchantmentEntry("empty", List.of(book), List.of(copy)));
		}
		return entries;
	}

	private static ItemStack enchantedBook(Holder<Enchantment> enchantment, int enchantmentLevel) {
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		EnchantmentHelper.updateEnchantments(book, mutable -> mutable.set(enchantment, enchantmentLevel));
		return book;
	}

	private record EnchantmentEntry(String idSegment, List<ItemStack> templateBooks, List<ItemStack> outputCopies) {
	}
}
