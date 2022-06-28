package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;

public class WatermarkElement extends BaseElement {

	public WatermarkElement() {
		super(2, 2, true);
	}
	
	@Override
	public void render() {
		this.drawString("Menace §01.8.9", this.getAbsoluteX(), this.getAbsoluteY());
	}

	@Override
	public void renderDummy() {
		this.drawString("Menace §01.8.9", this.getAbsoluteX(), this.getAbsoluteY());
	}

	@Override
	public int getWidth() {
		return this.getStringWidth("Menace 1.8.9");
	}

	@Override
	public int getHeight() {
		return this.getFontHeight();
	}

}
