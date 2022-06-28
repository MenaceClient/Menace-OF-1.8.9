package dev.menace.ui.hud.elements;

import java.awt.Color;
import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.ui.hud.BaseElement;

public class ArrayElement extends BaseElement {

	public ArrayElement() {
		super(911, 2, true);
	}
	
	@Override
	public void render() {
		ArrayList<BaseModule> enabledModules = Menace.instance.moduleManager.getActiveModules();
		enabledModules.sort((m1, m2) -> this.getStringWidth(m2.getDisplayName()) - this.getStringWidth(m1.getDisplayName()));
		
		int y = this.getAbsoluteY();
		
		for (BaseModule m : enabledModules) {
			if (!m.isVisible()) {
				continue;
			}
			if (Menace.instance.moduleManager.hudModule.arrayAlign.getValue().equalsIgnoreCase("Left")) {
				//Align left
				this.drawString(m.getDisplayName(), this.getAbsoluteX(), y);
			} else {
				//Align right
				this.drawString(m.getDisplayName(), this.getAbsoluteX() - this.getStringWidth(m.getDisplayName()) + this.getStringWidth("ArrayList"), y);
			}
			
			y += 10;
		}
	}

	@Override
	public void renderDummy() {
		this.drawString("ArrayList", this.getAbsoluteX(), this.getAbsoluteY());
	}

	@Override
	public int getWidth() {
		return this.getStringWidth("ArrayList");
	}

	@Override
	public int getHeight() {
		return this.getFontHeight();
	}

}
