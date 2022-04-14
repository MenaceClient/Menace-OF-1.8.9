package dev.menace.module.modules.render;

import java.util.ArrayList;

import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.block.Block;

public class Xray extends Module {

	public ArrayList<Block> xrayBlocks = new ArrayList<Block>();
	
	public Xray() {
		super("Xray", 0, Category.RENDER);
	}
	
	@Override
	public void onEnable(){
		MC.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable(){
		MC.renderGlobal.loadRenderers();
	}

}
