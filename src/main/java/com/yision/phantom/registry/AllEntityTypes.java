package com.yision.phantom.registry;

import com.nobodiiiii.createbiotech.registry.CBEntityTypes;
import com.yision.phantom.entity.courier.AirCourierEntity;

import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class AllEntityTypes {
	public static final DeferredHolder<EntityType<?>, EntityType<AirCourierEntity>> AIR_COURIER =
		CBEntityTypes.AIR_COURIER;

	private AllEntityTypes() {}
}
