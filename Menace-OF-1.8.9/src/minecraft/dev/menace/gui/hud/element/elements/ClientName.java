package dev.menace.gui.hud.element.elements;

import java.awt.Color;
import java.io.File;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.utils.misc.file.FileManager;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

public class ClientName extends ElementDraggable {
	
	@Override
	public int getWidth() {
		return font.getStringWidth("Menace 1.8.9");
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		font.drawString("Menace §0" + ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion()).getName().replace("1.8.x", "1.8.9"), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString("§0M§renace " + ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion()).getName().replace("1.8.x", "1.8.9"), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	protected double defaultX() {
		return 0.00468384074941452;
	}
	
	@Override
	protected double defaultY() {
		return 0.008333333333333333;
	}
}
