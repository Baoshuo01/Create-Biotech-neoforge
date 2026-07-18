package com.nobodiiiii.createbiotech.registry;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.content.cardboardbox.CardboardBoxEntity;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.yision.phantom.entity.courier.AirCourierEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CBEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
		DeferredRegister.create(Registries.ENTITY_TYPE, CreateBiotech.MOD_ID);

	public static final DeferredHolder<EntityType<?>, EntityType<CardboardBoxEntity>> CARDBOARD_BOX =
		ENTITY_TYPES.register("cardboard_box", () -> EntityType.Builder
			.<CardboardBoxEntity>of(CardboardBoxEntity::new, MobCategory.MISC)
			.setTrackingRange(10)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(true)
			.sized(1, 1)
			.build("cardboard_box"));



	public static final DeferredHolder<EntityType<?>, EntityType<AirCourierEntity>> AIR_COURIER =
		ENTITY_TYPES.register("air_courier", () -> EntityType.Builder
			.<AirCourierEntity>of(AirCourierEntity::createEmpty, MobCategory.MISC)
			.sized(0.6F, 0.6F)
			.setTrackingRange(96)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.build("air_courier"));

	private CBEntityTypes() {}

	public static void register(IEventBus modEventBus) {
		ENTITY_TYPES.register(modEventBus);
		modEventBus.addListener(CBEntityTypes::registerEntityAttributes);
	}

	private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(CARDBOARD_BOX.get(), PackageEntity.createPackageAttributes()
			.build());
	}
}
