package com.nobodiiiii.createbiotech.infrastructure;

import com.nobodiiiii.createbiotech.CreateBiotech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers aliases for IDs used by Create: Biotech before its experience transport blocks were removed. */
public final class CBRemapHelper {
	private static final ResourceLocation EXPERIENCE_PIPE = CreateBiotech.asResource("experience_pipe");
	private static final ResourceLocation ENCASED_EXPERIENCE_PIPE =
		CreateBiotech.asResource("encased_experience_pipe");
	private static final ResourceLocation EXPERIENCE_TANK = CreateBiotech.asResource("experience_tank");

	private static final ResourceLocation FLUID_PIPE = ResourceLocation.fromNamespaceAndPath("create", "fluid_pipe");
	private static final ResourceLocation ENCASED_FLUID_PIPE =
		ResourceLocation.fromNamespaceAndPath("create", "encased_fluid_pipe");
	private static final ResourceLocation FLUID_TANK = ResourceLocation.fromNamespaceAndPath("create", "fluid_tank");

	private CBRemapHelper() {}

	public static void addBlockAliases(DeferredRegister<Block> blocks) {
		addTransportAliases(blocks);
	}

	public static void addItemAliases(DeferredRegister<Item> items) {
		addTransportAliases(items);
	}

	public static void addBlockEntityAliases(DeferredRegister<BlockEntityType<?>> blockEntityTypes) {
		addTransportAliases(blockEntityTypes);
	}

	private static <T> void addTransportAliases(DeferredRegister<T> register) {
		register.addAlias(EXPERIENCE_PIPE, FLUID_PIPE);
		register.addAlias(ENCASED_EXPERIENCE_PIPE, ENCASED_FLUID_PIPE);
		register.addAlias(EXPERIENCE_TANK, FLUID_TANK);
	}
}
