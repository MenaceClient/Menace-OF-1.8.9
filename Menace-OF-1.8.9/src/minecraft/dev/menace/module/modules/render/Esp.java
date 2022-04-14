package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.Event2D;
import dev.menace.event.events.Event3D;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.combat.AntiBot;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.awt.Color;

public class Esp extends Module {

	public Esp() {
		super("Esp", 0, Category.RENDER);
	}
	
	@EventTarget
	public void onRender3D(Event3D event) {
		
		for (Entity e: MC.theWorld.loadedEntityList) {
			if (e != MC.thePlayer) {
				for (String id : AntiBot.bots) {
					if (String.valueOf(e.getEntityId()) == id) return;
				}
				
				RenderUtils.drawEntityBox(e, Color.GREEN, true, true, 5f);
			}
		}
	}
}
