package dev.menace.utils.player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import org.jetbrains.annotations.NotNull;

public class InventoryUtils {

	private static final Minecraft MC = Minecraft.getMinecraft();
	public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(
            Blocks.enchanting_table, 
            Blocks.chest, 
            Blocks.ender_chest, 
            Blocks.trapped_chest,
            Blocks.anvil, 
            Blocks.sand, 
            Blocks.web, 
            Blocks.torch, 
            Blocks.crafting_table, 
            Blocks.furnace, 
            Blocks.waterlily, 
            Blocks.dispenser,
            Blocks.stone_pressure_plate, 
            Blocks.wooden_pressure_plate, 
            Blocks.noteblock, 
            Blocks.dropper, 
            Blocks.tnt, 
            Blocks.standing_banner, 
            Blocks.wall_banner,
            Blocks.redstone_torch,
            Blocks.gravel,
            Blocks.cactus,
            Blocks.bed,
            Blocks.lever,
            Blocks.standing_sign,
            Blocks.wall_sign,
            Blocks.jukebox,
            Blocks.oak_fence,
            Blocks.spruce_fence,
            Blocks.birch_fence,
            Blocks.jungle_fence,
            Blocks.dark_oak_fence,
            Blocks.oak_fence_gate,
            Blocks.spruce_fence_gate,
            Blocks.birch_fence_gate,
            Blocks.jungle_fence_gate,
            Blocks.dark_oak_fence_gate,
            Blocks.nether_brick_fence,
            Blocks.cake,
            Blocks.trapdoor,
            Blocks.melon_block,
            Blocks.brewing_stand,
            Blocks.cauldron,
            Blocks.skull,
            Blocks.hopper,
            Blocks.carpet,
            Blocks.redstone_wire,
            Blocks.light_weighted_pressure_plate,
            Blocks.heavy_weighted_pressure_plate,
            Blocks.daylight_detector,
			Blocks.wooden_slab
    );
	public static final List<Item> TRASH = Arrays.asList(
			Items.egg,
			Items.snowball,
			Items.experience_bottle,
			Items.lava_bucket,
			Items.cooked_fish,
			Items.fishing_rod,
			Items.wheat_seeds
	);

	public static void shiftClick(int slot, int windowID) {
		MC.playerController.windowClick(windowID, slot, 0, 1, MC.thePlayer);
	}

	public static void swap(int slot1, int hotbarSlot) {
		MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, MC.thePlayer);
	}

	public static void drop(int slot) {
		MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot, 1, 4, MC.thePlayer);
	}

	public static float getDamage(ItemStack stack) {
		float damage = 0.0F;
		if (stack == null) return damage;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			ItemTool tool = (ItemTool)item;
			damage += (float)tool.getMaxDamage();
		}

		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword)item;
			damage += sword.getDamageVsEntity();
		}

		damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
		return damage;
	}

	public static float getToolEffect(ItemStack stack) {
		if (stack == null) return 0.0F;
		Item item = stack.getItem();
		if (!(item instanceof ItemTool)) {
			return 0.0F;
		} else {
			String name = item.getUnlocalizedName();
			ItemTool tool = (ItemTool)item;
			float value;
			if (item instanceof ItemPickaxe) {
				value = tool.getStrVsBlock(stack, Blocks.stone);
				if (name.toLowerCase().contains("gold")) {
					value -= 5.0F;
				}
			} else if (item instanceof ItemSpade) {
				value = tool.getStrVsBlock(stack, Blocks.dirt);
				if (name.toLowerCase().contains("gold")) {
					value -= 5.0F;
				}
			} else {
				if (!(item instanceof ItemAxe)) {
					return 1.0F;
				}

				value = tool.getStrVsBlock(stack, Blocks.log);
				if (name.toLowerCase().contains("gold")) {
					value -= 5.0F;
				}
			}

			value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D);
			value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D);
			return value;
		}
	}

	public static float getArmorStrength(final ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor)) return -1;
		float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
		Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
		if (enchantments.containsKey(Enchantment.protection.effectId)) {
			int level = enchantments.get(Enchantment.protection.effectId);
			damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
		}
		return damageReduction;
	}

}