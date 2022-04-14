package dev.menace.gui.hud.element;

import java.lang.reflect.Field;
import dev.menace.gui.hud.HUDManager;
import dev.menace.gui.hud.IRenderer;
import dev.menace.gui.hud.element.elements.*;
import dev.menace.module.Module;
import dev.menace.module.ModuleManager;
import net.minecraft.crash.CrashReport;

public class ElementManager {
	
	public static java.util.ArrayList<Element> elements = new java.util.ArrayList<Element>();
	
	public final ArrayList arrayListElement = new ArrayList();
	public final BPS bpsElement = new BPS();
	public final ClientName clientNameElement = new ClientName();
	public final Coords coordsElement = new Coords();
	public final FPS fpsElement = new FPS();
	public final KeyStrokes keyStrokesElement = new KeyStrokes();
	public final TabGui tabGuiElement = new TabGui();
	public final TargetHud targetHudElement = new TargetHud();
	public final Notifications notifElement = new Notifications();
	
	public ElementManager(HUDManager api) {
		try
		{
			for(Field field : ElementManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Element"))
					continue;
				
				IRenderer renderer = (IRenderer)field.get(this);
				api.register(renderer);
				
				Element element = (Element)field.get(this);
				elements.add(element);
			}
			
		}catch(Exception e)
		{
			String message = "Initializing Menace modules";
			CrashReport report = CrashReport.makeCrashReport(e, message);
		}
	}
}
