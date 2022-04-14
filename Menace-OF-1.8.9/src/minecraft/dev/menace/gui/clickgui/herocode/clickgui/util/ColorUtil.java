package dev.menace.gui.clickgui.herocode.clickgui.util;

import java.awt.Color;

import dev.menace.Menace;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int)Menace.instance.moduleManager.clickGuiModule.red.getValue(), (int)Menace.instance.moduleManager.clickGuiModule.green.getValue(), (int)Menace.instance.moduleManager.clickGuiModule.blue.getValue());
	}
}
