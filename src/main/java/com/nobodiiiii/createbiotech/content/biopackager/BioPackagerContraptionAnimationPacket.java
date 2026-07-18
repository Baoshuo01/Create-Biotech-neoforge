package com.nobodiiiii.createbiotech.content.biopackager;

import com.nobodiiiii.createbiotech.CreateBiotech;
import com.nobodiiiii.createbiotech.client.BioPackagerContraptionClientAnimationHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BioPackagerContraptionAnimationPacket implements CustomPacketPayload {

	public static final Type<BioPackagerContraptionAnimationPacket> TYPE =
		new Type<>(CreateBiotech.asResource("bio_packager_contraption_animation"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BioPackagerContraptionAnimationPacket> STREAM_CODEC =
		StreamCodec.ofMember(BioPackagerContraptionAnimationPacket::write,
			BioPackagerContraptionAnimationPacket::decode);

	private final int entityId;
	private final BlockPos localPos;
	private final ItemStack heldBox;
	private final ItemStack previouslyUnwrapped;
	private final boolean animationInward;

	public BioPackagerContraptionAnimationPacket(int entityId, BlockPos localPos, ItemStack heldBox,
		ItemStack previouslyUnwrapped, boolean animationInward) {
		this.entityId = entityId;
		this.localPos = localPos.immutable();
		this.heldBox = heldBox.copy();
		this.previouslyUnwrapped = previouslyUnwrapped.copy();
		this.animationInward = animationInward;
	}

	private static BioPackagerContraptionAnimationPacket decode(RegistryFriendlyByteBuf buffer) {
		return new BioPackagerContraptionAnimationPacket(buffer.readVarInt(), buffer.readBlockPos(),
			ItemStack.STREAM_CODEC.decode(buffer), ItemStack.STREAM_CODEC.decode(buffer), buffer.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeVarInt(entityId);
		buffer.writeBlockPos(localPos);
		ItemStack.STREAM_CODEC.encode(buffer, heldBox);
		ItemStack.STREAM_CODEC.encode(buffer, previouslyUnwrapped);
		buffer.writeBoolean(animationInward);
	}

	public void handle(IPayloadContext context) {
		BioPackagerContraptionClientAnimationHandler.startAnimation(entityId, localPos, heldBox, previouslyUnwrapped,
			animationInward);
	}

	@Override
	public Type<BioPackagerContraptionAnimationPacket> type() {
		return TYPE;
	}
}
