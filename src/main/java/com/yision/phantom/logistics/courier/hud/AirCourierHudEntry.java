package com.yision.phantom.logistics.courier.hud;

import io.netty.handler.codec.DecoderException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record AirCourierHudEntry(
	AirCourierHudStatus status,
	int etaSeconds,
	List<ItemStack> displayStacks
) {
	public static final StreamCodec<RegistryFriendlyByteBuf, AirCourierHudEntry> STREAM_CODEC =
		StreamCodec.ofMember(AirCourierHudEntry::write, AirCourierHudEntry::read);

	public AirCourierHudEntry {
		displayStacks = AirCourierPackagePreview.copyDisplayStacks(displayStacks);
	}

	private static AirCourierHudEntry read(RegistryFriendlyByteBuf buffer) {
		AirCourierHudStatus status = AirCourierHudStatus.byId(buffer.readVarInt());
		int etaSeconds = buffer.readVarInt();
		int size = buffer.readVarInt();
		if (size < 0 || size > AirCourierPackagePreview.MAX_DISPLAY_STACKS) {
			throw new DecoderException("Invalid air courier HUD stack count: " + size);
		}
		List<ItemStack> stacks = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			stacks.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
		}
		return new AirCourierHudEntry(status, etaSeconds, stacks);
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeVarInt(status.ordinal());
		buffer.writeVarInt(etaSeconds);
		buffer.writeVarInt(displayStacks.size());
		for (ItemStack stack : displayStacks) {
			ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, stack);
		}
	}
}
