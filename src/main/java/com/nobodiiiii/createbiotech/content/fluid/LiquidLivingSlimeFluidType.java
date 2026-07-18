package com.nobodiiiii.createbiotech.content.fluid;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class LiquidLivingSlimeFluidType extends FluidType {

	private static final float MOVE_SCALE = 0.011F;
	private static final float BASE_DRAG = 0.72F;
	private static final float SPRINT_DRAG = 0.78F;
	private static final double VERTICAL_DRAG = 0.75D;
	private static final double COLLISION_ASCENT = 0.2D;

	public LiquidLivingSlimeFluidType(Properties properties) {
		super(properties);
	}

	@Override
	public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
		double startY = entity.getY();
		float horizontalDrag = entity.isSprinting() ? SPRINT_DRAG : BASE_DRAG;
		float movementScale = MOVE_SCALE * (float) entity.getAttributeValue(NeoForgeMod.SWIM_SPEED);

		entity.moveRelative(movementScale, movementVector);
		entity.move(MoverType.SELF, entity.getDeltaMovement());

		Vec3 movement = entity.getDeltaMovement();
		if (entity.horizontalCollision && entity.onClimbable()) {
			movement = new Vec3(movement.x, COLLISION_ASCENT, movement.z);
		}

		entity.setDeltaMovement(movement.multiply(horizontalDrag, VERTICAL_DRAG, horizontalDrag));
		Vec3 adjustedMovement =
			entity.getFluidFallingAdjustedMovement(gravity, entity.getDeltaMovement().y <= 0.0D, entity.getDeltaMovement());
		entity.setDeltaMovement(adjustedMovement);

		if (entity.horizontalCollision
			&& entity.isFree(adjustedMovement.x, adjustedMovement.y + 0.6D - entity.getY() + startY,
				adjustedMovement.z)) {
			entity.setDeltaMovement(adjustedMovement.x, COLLISION_ASCENT, adjustedMovement.z);
		}

		return true;
	}

}
