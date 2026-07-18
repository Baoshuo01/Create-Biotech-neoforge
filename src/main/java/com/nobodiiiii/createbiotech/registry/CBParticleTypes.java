package com.nobodiiiii.createbiotech.registry;

import com.nobodiiiii.createbiotech.CreateBiotech;

import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CBParticleTypes {

	public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLE_TYPES =
		DeferredRegister.create(Registries.PARTICLE_TYPE, CreateBiotech.MOD_ID);

	public static final DeferredHolder<net.minecraft.core.particles.ParticleType<?>, SimpleParticleType> STRAIGHT_ENCHANT =
		PARTICLE_TYPES.register("straight_enchant", () -> new SimpleParticleType(false));

	private CBParticleTypes() {
	}

	public static void register(IEventBus modEventBus) {
		PARTICLE_TYPES.register(modEventBus);
	}
}
