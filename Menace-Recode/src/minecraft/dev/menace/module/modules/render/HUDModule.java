package dev.menace.module.modules.render;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender2D;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.HUDConfigScreen;
import dev.menace.ui.hud.HUDManager;

public class HUDModule extends BaseModule {

	public ToggleSetting watermark;
	public ListSetting wmMode;
	public ToggleSetting array;
	public ListSetting arrayAlign;
	public SliderSetting arrayAlpha;
	public ToggleSetting pos;
	public ToggleSetting ping;
	public ToggleSetting armour;
	public ListSetting posMode;
	public ToggleSetting gameStats;
	public ToggleSetting targetHud;
	public ToggleSetting notifications;
	public ToggleSetting tabGui;
	public ToggleSetting spotify;
	public ToggleSetting customFont;
	public ListSetting color;
	public SliderSetting rainbowSpeed;
	public SliderSetting red;
	public SliderSetting green;
	public SliderSetting blue;
	public SliderSetting alpha;
	public ToggleSetting hudEditor;
	
	public HUDModule() {
		super("HUD", Category.RENDER, 0);
	}
	
	@Override
	public void setup() {
		
		watermark = new ToggleSetting("Watermark", true, true);
		wmMode = new ListSetting("WaterMark Mode", true, "Text", new String[] {"Text", "Image"}) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.watermark.getValue());
			}
		};
		array = new ToggleSetting("ArrayList", true, true);
		arrayAlign = new ListSetting("ArrayAlign", true, "Right", new String[] {"Left", "Right"}) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.array.getValue());
			}
		};
		arrayAlpha = new SliderSetting("ArrayAlpha", true, 100, 0, 255, 5, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.array.getValue());
			}
		};
		pos = new ToggleSetting("Position", true, true);
		posMode = new ListSetting("Position Mode", true, "Single", new String[] {"Single", "Multi"}) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.pos.getValue());
			}
		};
		ping = new ToggleSetting("Ping", true, true);
		armour = new ToggleSetting("Armour", true, true);
		gameStats = new ToggleSetting("GameStats", true, true);
		targetHud = new ToggleSetting("TargetHUD", true, true);
		notifications = new ToggleSetting("Notifications", true, true);
		tabGui = new ToggleSetting("TabGui", true, false);
		spotify = new ToggleSetting("Spotify", true, false);
		customFont = new ToggleSetting("Custom Font", true, false);
		color = new ListSetting("Color", true, "Custom", new String[] {"Fade", "Custom"});
		rainbowSpeed = new SliderSetting("RBW Speed", true, 10, 1, 100, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.color.getValue().equalsIgnoreCase("Fade"));
			}
		};
		red = new SliderSetting("Red", true, 255, 0, 255, 5, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.color.getValue().equalsIgnoreCase("Custom"));
			}
		};
		green = new SliderSetting("Green", true, 0, 0, 255, 5, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.color.getValue().equalsIgnoreCase("Custom"));
			}
		};
		blue = new SliderSetting("Blue", true, 0, 0, 255, 5, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.color.getValue().equalsIgnoreCase("Custom"));
			}
		};
		alpha = new SliderSetting("Alpha", true, 255, 0, 255, 5, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.hudModule.color.getValue().equalsIgnoreCase("Custom"));
			}
		};
		hudEditor = new ToggleSetting("HUD Editor", true, false) {
			@Override
			public void constantCheck() {
				if (this.getValue()) {
					this.setValue(false);
					mc.displayGuiScreen(new HUDConfigScreen(Menace.instance.hudManager));
				}
			}
		};
		
		this.rSetting(watermark);
		this.rSetting(array);
		this.rSetting(arrayAlign);
		this.rSetting(arrayAlpha);
		this.rSetting(pos);
		this.rSetting(posMode);
		this.rSetting(ping);
		this.rSetting(armour);
		this.rSetting(gameStats);
		this.rSetting(targetHud);
		this.rSetting(notifications);
		this.rSetting(tabGui);
		this.rSetting(spotify);
		this.rSetting(customFont);
		this.rSetting(color);
		this.rSetting(rainbowSpeed);
		this.rSetting(red);
		this.rSetting(green);
		this.rSetting(blue);
		this.rSetting(alpha);
		this.rSetting(hudEditor);
		super.setup();
	}

	@Override
	public void onEnable() {
		HUDManager.load();
		super.onEnable();
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		Menace.instance.hudManager.watermarkElement.setVisible(watermark.getValue());
		Menace.instance.hudManager.arrayElement.setVisible(array.getValue());
		Menace.instance.hudManager.posElement.setVisible(pos.getValue());
		Menace.instance.hudManager.gameStatsElement.setVisible(gameStats.getValue());
		Menace.instance.hudManager.targetHudElement.setVisible(targetHud.getValue());
		Menace.instance.hudManager.notificationElement.setVisible(notifications.getValue());
		Menace.instance.hudManager.pingElement.setVisible(ping.getValue());
		Menace.instance.hudManager.tabGuiElement.setVisible(tabGui.getValue());
		Menace.instance.hudManager.spotifyElement.setVisible(spotify.getValue());
		Menace.instance.hudManager.armourElement.setVisible(armour.getValue());
		
		HUDManager.hudElements.stream().filter(BaseElement::isVisible).forEach(BaseElement::render);
		
	}
	
}
