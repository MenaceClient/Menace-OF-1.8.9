package dev.menace.module.modules.render;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;

public class Animations extends Module {

	//Settings
	public StringSetting blockMode;
	DoubleSetting transX;
	DoubleSetting transY;
	DoubleSetting transZ;
	DoubleSetting itemposX;
	DoubleSetting itemposY;
	DoubleSetting itemposZ;
	DoubleSetting itemscale;
	DoubleSetting itemrotate;
	BoolSetting swinganim;
	BoolSetting anythingblock;
	
	public Animations() {
		super("Animations", 0, Category.RENDER);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		String[] o = {"Akrien", "Avatar", "ETB", "Exhibition", "Push", "Reverse",
	            "Shield", "SigmaNew", "SigmaOld", "Slide", "SlideDown", "HSlide", "Swong", "VisionFX",
	            "Swank", "Jello", "1.7", "None", "Rotate"};
		for (String s : o) {
			options.add(s);
		}
		blockMode = new StringSetting("Blocking Mode", this, "Jello", options);
		transX = new DoubleSetting("TranslateX", this, 0, 0, 1.5);
		transY = new DoubleSetting("TranslateY", this, 0, 0, 0.5);
		transZ = new DoubleSetting("TranslateZ", this, 0, 0, -2);
		itemposX = new DoubleSetting("ItemPosX", this, 0.56, -1, 1);
		itemposY = new DoubleSetting("ItemPosY", this, -0.52, -1, 1);
		itemposZ = new DoubleSetting("ItemPosZ", this, -0.71999997, -1, 1);
		itemscale = new DoubleSetting("ItemScale", this, 0.4, 0, 2);
		itemrotate = new DoubleSetting("ItemRotation", this, 45, 0, 360);
		swinganim = new BoolSetting("SwingAnim", this, false);
		anythingblock = new BoolSetting("AnythingBlock", this, false);
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
	}
	
	public boolean getAnythingBlock() {
		return anythingblock.isChecked();
	}
	
	public boolean getSwingAnim() {
		return swinganim.isChecked();
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
