package dev.menace.ui.hud;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class HUDConfigScreen extends GuiScreen {
	
	HUDManager api;
	
	private Optional<BaseElement> selectedRenderer = Optional.empty();
	
	private int prevX, prevY;
	
	public HUDConfigScreen(HUDManager api) {
		
		this.api = api;
		
		Collection<BaseElement> registeredRenderers = HUDManager.hudElements;
		
		for (BaseElement ren : registeredRenderers) {
			//if (!ren.isEnabled()) continue;
			
			adjustBounds(ren);
		}
		 
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		
		final float zBackup = this.zLevel;
		this.zLevel = 200;
		
		this.drawHollowRect(0, 0, width - 1, height - 1, 0xFFFF0000);
		
		for (BaseElement renderer : HUDManager.hudElements) {
			
			renderer.renderDummy();
			
			this.drawHollowRect(renderer.getAbsoluteX() - 1, renderer.getAbsoluteY() - 1, renderer.getWidth(), renderer.getHeight(), 0xFF00FFFF);
		}
		
		this.zLevel = zBackup;
		
	}

	private void drawHollowRect(int x, int y, int w, int h, int color) {
		this.drawHorizontalLine(x, x + w, y, color);
		this.drawHorizontalLine(x, x + w, y + h, color);
		
		this.drawVerticalLine(x, y + h, y, color);
		this.drawVerticalLine(x + w, y + h, y, color);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			HUDManager.save();
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void mouseClickMove(int x, int y, int button, long time) {
		if (selectedRenderer.isPresent()) {
			moveSelectedRenderBy(x - prevX, y - prevY);
		}
		
		this.prevX = x;
		this.prevY = y;
	}

	private void moveSelectedRenderBy(int offsetX, int offsetY) {
		BaseElement renderer = selectedRenderer.get();
		
		renderer.setAbsolute(renderer.getAbsoluteX() + offsetX, renderer.getAbsoluteY() + offsetY);
		
		adjustBounds(renderer);
	}
	
	 @Override
	public void onGuiClosed() {
		 
		 HUDManager.save();
		 
	}
	 
	 @Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	 
	private void adjustBounds(BaseElement renderer) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int screenWidth = sr.getScaledWidth();
		int screenHeight = sr.getScaledHeight();
		
		int absoluteX = Math.max(0, Math.min(renderer.getAbsoluteX(), Math.max(screenWidth - renderer.getWidth(), 0)));
		int absoluteY = Math.max(0, Math.min(renderer.getAbsoluteY(), Math.max(screenHeight - renderer.getHeight(), 0)));
		
		renderer.setAbsolute(absoluteX, absoluteY);
		
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		this.prevX = x;
		this.prevY = y;
		
		loadMouseOver(x, y);
	}

	private void loadMouseOver(int x, int y) {
		this.selectedRenderer = HUDManager.hudElements.stream().filter(new MouseOverFinder(x, y)).findFirst();
	}
	
	private static class MouseOverFinder implements Predicate<BaseElement> {

		private final int mouseX;
		private final int mouseY;
		
		public MouseOverFinder(int x, int y) {
			this.mouseX = x;
			this.mouseY = y;
		}

		@Override
		public boolean test(BaseElement renderer) {
			
			int absoluteX = renderer.getAbsoluteX();
			int absoluteY = renderer.getAbsoluteY();
			
			if (mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) {
				return mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight();
			}
			
			return false;
		}

	}
	
 }
