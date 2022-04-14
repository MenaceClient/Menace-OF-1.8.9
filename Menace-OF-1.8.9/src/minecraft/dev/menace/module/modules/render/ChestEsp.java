package dev.menace.module.modules.render;

import java.util.ArrayList;

import dev.menace.event.EventTarget;
import dev.menace.event.events.Event3D;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.awt.Color;

public class ChestEsp extends Module {

	float xPos;
    float yPos;
    float zPos;
	ArrayList<BlockPos> chests = new ArrayList<BlockPos>();
	
	public ChestEsp() {
		super("ChestEsp", 0, Category.RENDER);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		for (int radius = 20, x = -radius; x < radius; ++x) {
			for (int y = radius; y > -radius; --y) {
				for (int z = -radius; z < radius; ++z) {
					this.xPos = (float)((int)MC.thePlayer.posX + x);
					this.yPos = (float)((int)MC.thePlayer.posY + y);
					this.zPos = (float)((int)MC.thePlayer.posZ + z);
					final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
					final net.minecraft.block.Block block = MC.theWorld.getBlockState(blockPos).getBlock();
					if (block.getBlockState().getBlock() == Blocks.chest || block.getBlockState().getBlock() == Blocks.ender_chest) {
						chests.add(blockPos);
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onRender3D(Event3D event) {
		
		if (chests == null) return;
		
		for (BlockPos pos : chests) {
			
			Block block = BlockUtils.getBlock(pos);
			
			RenderUtils.drawBlockBox(pos, Color.BLUE, true, false, 5f);
			
		}
		
	}
	
}
