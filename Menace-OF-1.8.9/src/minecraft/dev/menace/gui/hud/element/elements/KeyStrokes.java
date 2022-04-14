package dev.menace.gui.hud.element.elements;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;

public class KeyStrokes extends ElementDraggable{

	@Override
	public int getWidth() {
		return 101;
	}

	@Override
	public int getHeight() {
		return 80;
	}

	@Override
	public void render(ScreenPosition pos) {
		int wColor = (Keyboard.isKeyDown(MC.gameSettings.keyBindForward.getKeyCode())? 255 : 0);
		int aColor = (Keyboard.isKeyDown(MC.gameSettings.keyBindLeft.getKeyCode())? 255 : 0);
		int sColor = (Keyboard.isKeyDown(MC.gameSettings.keyBindBack.getKeyCode())? 255 : 0);
		int dColor = (Keyboard.isKeyDown(MC.gameSettings.keyBindRight.getKeyCode())? 255 : 0);
		int jColor = (Keyboard.isKeyDown(MC.gameSettings.keyBindJump.getKeyCode())? 255 : 0);

		RenderUtils.drawRect(pos.getAbsoluteX() + 35, pos.getAbsoluteY(), pos.getAbsoluteX() + 65, pos.getAbsoluteY() + 30, new Color(wColor, 0, 0, 125).getRGB());//W
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 35, pos.getAbsoluteX() + 30, pos.getAbsoluteY() + 65, new Color(aColor, 0, 0, 125).getRGB());//A
		RenderUtils.drawRect(pos.getAbsoluteX() + 35, pos.getAbsoluteY() + 35, pos.getAbsoluteX() + 65, pos.getAbsoluteY() + 65, new Color(sColor, 0, 0, 125).getRGB());//S
		RenderUtils.drawRect(pos.getAbsoluteX() + 70, pos.getAbsoluteY() + 35, pos.getAbsoluteX() + 100, pos.getAbsoluteY() + 65, new Color(dColor, 0, 0, 125).getRGB());//D
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 70, pos.getAbsoluteX() + 100, pos.getAbsoluteY() + 80, new Color(jColor, 0, 0, 125).getRGB());//SPACE
		
		font.drawString("W", pos.getAbsoluteX() + 48, pos.getAbsoluteY() + 12, Color.WHITE.getRGB());
		font.drawString("D", pos.getAbsoluteX() + 82, pos.getAbsoluteY() + 47, Color.WHITE.getRGB());
		font.drawString("S", pos.getAbsoluteX() + 48, pos.getAbsoluteY() + 47, Color.WHITE.getRGB());
		font.drawString("A", pos.getAbsoluteX() + 12, pos.getAbsoluteY() + 47, Color.WHITE.getRGB());
		GL11.glPushMatrix();
		RenderUtils.drawLine(pos.getAbsoluteX() + 3, pos.getAbsoluteY() + 75, pos.getAbsoluteX() + 97, pos.getAbsoluteY() + 75, 3);
		GL11.glPopMatrix();
	}

}
