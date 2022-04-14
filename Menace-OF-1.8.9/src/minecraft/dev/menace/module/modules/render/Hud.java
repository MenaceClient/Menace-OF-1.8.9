package dev.menace.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.Event2D;
import dev.menace.event.events.EventUpdate;
import dev.menace.gui.hud.HUDConfigScreen;
import dev.menace.gui.hud.element.Element;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class Hud extends Module {
	
	//Settings
	public StringSetting targetHudMode;
	public StringSetting arrayAlign;
	public StringSetting notifAlign;
	BoolSetting fps;
	BoolSetting coords;
	BoolSetting arraylist;
	BoolSetting keystrokes;
	BoolSetting tabgui;
	BoolSetting bps;
	BoolSetting targethud;
	BoolSetting notifs;
	
	public Hud() {
		super("Hud", 0, Category.RENDER);
	}
	
	@Override
	public void setup() {
		ArrayList<String> op = new ArrayList<String>();
		ArrayList<String> alignArray = new ArrayList<String>();
		ArrayList<String> alignNotif = new ArrayList<String>();
		op.add("Menace");
		op.add("Flux");
		//op.add("Rise");
		op.add("Arris");
		op.add("Astolfo");
		op.add("Tenacity");
		op.add("Novoline");
		op.add("Zamorozka");
		
		alignArray.add("Left");
		alignArray.add("Right");
		
		alignNotif.add("Left");
		alignNotif.add("Right");
		
		targetHudMode = new StringSetting("TargetHud Mode", this, "Menace", op);
		arrayAlign = new StringSetting("Arraylist Align", this, "Right", alignArray);
		notifAlign = new StringSetting("Notification Align", this, "Right", alignNotif);
		fps = new BoolSetting("FPS", this, true);
		coords = new BoolSetting("Coords", this, true);
		arraylist = new BoolSetting("Arraylist", this, true);
		keystrokes = new BoolSetting("Keystrokes", this, true);
		tabgui = new BoolSetting("TabGUI", this, false);
		bps = new BoolSetting("BPS", this, true);
		targethud = new BoolSetting("TargetHud", this, true);
		notifs = new BoolSetting("Notifications", this, true);
		
		this.rSetting(fps);
		this.rSetting(coords);
		this.rSetting(arraylist);
		this.rSetting(keystrokes);
		this.rSetting(tabgui);
		this.rSetting(bps);
		this.rSetting(targethud);
		this.rSetting(notifs);
		
		this.rSetting(targetHudMode);
		this.rSetting(arrayAlign);
		this.rSetting(notifAlign);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		Menace.instance.elementManager.arrayListElement.setEnabled(arraylist.isChecked());
		Menace.instance.elementManager.bpsElement.setEnabled(bps.isChecked());
		Menace.instance.elementManager.coordsElement.setEnabled(coords.isChecked());
		Menace.instance.elementManager.fpsElement.setEnabled(fps.isChecked());
		Menace.instance.elementManager.keyStrokesElement.setEnabled(keystrokes.isChecked());
		Menace.instance.elementManager.targetHudElement.setEnabled(targethud.isChecked());
		Menace.instance.elementManager.tabGuiElement.setEnabled(tabgui.isChecked());
		Menace.instance.elementManager.notifElement.setEnabled(notifs.isChecked());
		Menace.instance.elementManager.clientNameElement.setEnabled(true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		Menace.instance.elementManager.arrayListElement.setEnabled(arraylist.isChecked());
		Menace.instance.elementManager.bpsElement.setEnabled(bps.isChecked());
		Menace.instance.elementManager.coordsElement.setEnabled(coords.isChecked());
		Menace.instance.elementManager.fpsElement.setEnabled(fps.isChecked());
		Menace.instance.elementManager.keyStrokesElement.setEnabled(keystrokes.isChecked());
		Menace.instance.elementManager.targetHudElement.setEnabled(targethud.isChecked());
		Menace.instance.elementManager.tabGuiElement.setEnabled(tabgui.isChecked());
		Menace.instance.elementManager.notifElement.setEnabled(notifs.isChecked());
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		for (Element e : Menace.instance.elementManager.elements) {
			e.setEnabled(false);
		}
	}
}
