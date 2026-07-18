package com.nobodiiiii.createbiotech.content.evokerenchantingchamber;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nobodiiiii.createbiotech.foundation.render.OversizedBlockItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class EvokerEnchantingChamberItemRenderer extends OversizedBlockItemRenderer<EvokerEnchantingChamberBlockEntity> {

	private static final float ITEM_Y_OFFSET = -0.25f;

	@Override
	protected float getRenderYOffset() {
		return ITEM_Y_OFFSET;
	}

	@Override
	protected EvokerEnchantingChamberBlockEntity createBlockEntity() {
		return new EvokerEnchantingChamberBlockEntity(BlockPos.ZERO, createRenderState());
	}

	@Override
	protected void renderTransformed(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
		ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay,
		EvokerEnchantingChamberBlockEntity blockEntity) {
		boolean guiLighting = transformType == ItemDisplayContext.GUI;
		if (guiLighting)
			Lighting.setupForEntityInInventory();
		try {
			renderBlockEntity(blockEntity, ms, buffer, light, overlay);
		} finally {
			if (guiLighting)
				Lighting.setupFor3DItems();
		}
	}

	private static BlockState createRenderState() {
		return com.nobodiiiii.createbiotech.registry.CBBlocks.EVOKER_ENCHANTING_CHAMBER.get()
			.defaultBlockState()
			.setValue(EvokerEnchantingChamberBlock.FACING, Direction.NORTH)
			.setValue(EvokerEnchantingChamberBlock.HALF, DoubleBlockHalf.LOWER);
	}
}
