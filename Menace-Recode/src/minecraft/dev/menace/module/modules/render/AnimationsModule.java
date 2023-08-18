package dev.menace.module.modules.render;

import java.util.ArrayList;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;

public class AnimationsModule extends BaseModule {

	//Settings
	public ListSetting blockMode;
	SliderSetting transX;
	SliderSetting transY;
	SliderSetting transZ;
	SliderSetting itemposX;
	SliderSetting itemposY;
	SliderSetting itemposZ;
	SliderSetting itemscale;
	SliderSetting itemrotate;
	ToggleSetting swinganim;
	ToggleSetting anythingblock;
	ToggleSetting onedotseven;
	
	public AnimationsModule() {
		super("Animations", "Sword go spinnnnnnnn", Category.RENDER, 0);
	}
	
	@Override
	public void setup() {
		String[] o = {"Akrien", "Avatar", "ETB", "Exhibition", "Push", "Reverse",
	            "Shield", "SigmaNew", "SigmaOld", "Slide", "SlideDown", "HSlide", "Swong", "VisionFX",
	            "Swank", "Jello", "1.7", "None", "Rotate"};
		blockMode = new ListSetting("Blocking Mode", true, "Jello", o);
		transX = new SliderSetting("TranslateX", true, 0, 0, 1.5, 0.05, false);
		transY = new SliderSetting("TranslateY", true, 0, 0, 0.5, 0.05, false);
		transZ = new SliderSetting("TranslateZ", true, 0, 0, -2, 0.05, false);
		itemposX = new SliderSetting("ItemPosX", true, 0.56, -1, 1, 0.05, false);
		itemposY = new SliderSetting("ItemPosY", true, -0.52, -1, 1, 0.05, false);
		itemposZ = new SliderSetting("ItemPosZ", true, -0.71999997, -1, 1, 0.05, false);
		itemscale = new SliderSetting("ItemScale", true, 0.4, 0, 2, 0.05, false);
		itemrotate = new SliderSetting("ItemRotation", true, 45, 0, 360, true);
		swinganim = new ToggleSetting("SwingAnim", true, false);
		anythingblock = new ToggleSetting("AnythingBlock", true, false);
		onedotseven = new ToggleSetting("1.7", true, false);
		this.rSetting(blockMode);
		this.rSetting(transX);
		this.rSetting(transY);
		this.rSetting(transZ);
		this.rSetting(itemposX);
		this.rSetting(itemposY);
		this.rSetting(itemposZ);
		this.rSetting(itemscale);
		this.rSetting(itemrotate);
		this.rSetting(swinganim);
		this.rSetting(anythingblock);
		this.rSetting(onedotseven);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName(blockMode.getValue());
		if (onedotseven.getValue() && (mc.thePlayer.isBlocking() && mc.gameSettings.keyBindPickBlock.pressed || mc.thePlayer.isEating() && mc.gameSettings.keyBindPickBlock.pressed)) {
			mc.clickMouse();
		}
	}
	
	public boolean getAnythingBlock() {
		return anythingblock.getValue();
	}
	
	public boolean getSwingAnim() {
		return swinganim.getValue();
	}
	
	public double getTranslateX() {
		return transX.getValue();
	}
	
	public double getTranslateY() {
		return transY.getValue();
	}
	
	public double getTranslateZ() {
		return transZ.getValue();
	}
	
	public double getItemPosX() {
		return itemposX.getValue();
	}
	
	public double getItemPosY() {
		return itemposY.getValue();
	}
	
	public double getItemPosZ() {
		return itemposZ.getValue();
	}
	
	public double getItemScale() {
		return itemscale.getValue();
	}
	
	public double getItemRotation() {
		return itemrotate.getValue();
	}

}
