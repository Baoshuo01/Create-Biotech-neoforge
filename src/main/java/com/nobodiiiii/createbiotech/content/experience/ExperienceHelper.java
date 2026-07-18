package com.nobodiiiii.createbiotech.content.experience;

import com.nobodiiiii.createbiotech.content.squidprinter.EnchantmentBookCopyItem;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class ExperienceHelper {

	private ExperienceHelper() {
	}

	public static int nuggetsToXp(int nuggets) {
		return Math.max(0, nuggets) * ExperienceConstants.xpPerNugget();
	}

	public static int xpToNuggets(int xp) {
		return Math.max(0, xp) / ExperienceConstants.xpPerNugget();
	}

	public static int sumStoredEnchantmentLevels(ItemStack copyStack, HolderLookup.Provider registries) {
		int total = 0;
		ItemEnchantments enchantments = EnchantmentBookCopyItem.getStoredEnchantments(copyStack);
		for (var entry : enchantments.entrySet())
			total += Math.max(1, entry.getIntValue());
		return total;
	}

	public static int drainPlayerExperience(Player player, int maxAmount) {
		if (maxAmount <= 0 || player.isCreative() || player.isSpectator())
			return 0;
		int drained = Math.min(maxAmount, Math.max(0, player.totalExperience));
		if (drained <= 0)
			return 0;
		player.giveExperiencePoints(-drained);
		return drained;
	}

	public static void spawnExperience(Level level, Vec3 pos, int amount) {
		if (amount <= 0 || level.isClientSide || !(level instanceof ServerLevel serverLevel))
			return;
		ExperienceOrb.award(serverLevel, pos, amount);
	}
}
