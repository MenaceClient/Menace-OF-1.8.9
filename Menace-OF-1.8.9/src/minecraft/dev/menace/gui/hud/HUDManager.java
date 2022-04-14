package dev.menace.gui.hud;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.Event2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

public class HUDManager {

	private Set<IRenderer> registeredRenderers = Sets.newHashSet();
	private Minecraft MC = Minecraft.getMinecraft();
	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public void register(IRenderer... renderers) {
		for (IRenderer render : renderers) {
			this.registeredRenderers.add(render);
		}
	}
	
	public void unregister(IRenderer... renderers) {
		for (IRenderer render : renderers) {
			this.registeredRenderers.remove(render);
		}
	}
	
	public Collection<IRenderer> getRegisteredRenderers() {
		return Sets.newHashSet(registeredRenderers);
	}
	
	public void openConfigScreen() {
		MC.displayGuiScreen(new HUDConfigScreen(this));
	}
	
	@EventTarget
	public void onRender(Event2D event) {
		if (MC.currentScreen == null || MC.currentScreen instanceof GuiContainer || MC.currentScreen instanceof GuiChat) {
			for (IRenderer renderer : registeredRenderers) {
				callRenderer(renderer);
			}
		}
	}

	private void callRenderer(IRenderer renderer) {
		if (!renderer.isEnabled()) {
			return;
		}
		
		ScreenPosition pos = renderer.load();
		
		if (pos == null) {
			pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
		}
		
		renderer.render(pos);
	}
	
}
