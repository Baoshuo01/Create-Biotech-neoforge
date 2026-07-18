package com.nobodiiiii.createbiotech.client;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.registry.CBFluids;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = CreateBiotech.MOD_ID, value = Dist.CLIENT)
public final class LiquidLivingSlimePickHandler {

	private LiquidLivingSlimePickHandler() {}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		Entity entity = minecraft.getCameraEntity();
		if (entity == null || minecraft.level == null || minecraft.player == null)
			return;

		double reach = minecraft.player.blockInteractionRange();
		Vec3 start = entity.getEyePosition(1.0F);
		Vec3 direction = entity.getViewVector(1.0F);
		Vec3 end = start.add(direction.scale(reach));
		HitResult fluidHit =
			minecraft.level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity));
		if (!(fluidHit instanceof BlockHitResult blockHitResult))
			return;

		FluidState fluidState = minecraft.level.getFluidState(blockHitResult.getBlockPos());
		if (!isLiquidLivingSlime(fluidState))
			return;

		HitResult currentHit = minecraft.hitResult;
		if (currentHit == null || currentHit.getType() == HitResult.Type.MISS) {
			minecraft.hitResult = blockHitResult;
			return;
		}

		double fluidDistance = start.distanceToSqr(blockHitResult.getLocation());
		double currentDistance = start.distanceToSqr(currentHit.getLocation());
		if (fluidDistance <= currentDistance)
			minecraft.hitResult = blockHitResult;
	}

	private static boolean isLiquidLivingSlime(FluidState fluidState) {
		return !fluidState.isEmpty() && fluidState.getFluidType() == CBFluids.LIQUID_LIVING_SLIME_TYPE.get();
	}
}