package dev.menace.module.modules.world;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.modules.player.InvManagerModule;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.InventoryUtils;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChestStealerModule extends BaseModule {

	int slotAmt;
	List<Slot> slotList = new ArrayList<>();
	MSTimer delayTimer = new MSTimer();
	long nextDelay;
	boolean isInChest;
	GuiChest guiChest;
	private final MenaceFontRenderer fr = Menace.instance.sfPro;

	public SliderSetting minDelay;
	public SliderSetting maxDelay;
	ToggleSetting closeScreen;
	ToggleSetting randomize;
	ToggleSetting chestOnly;
	ToggleSetting noGui;

	public ChestStealerModule() {
		super("ChestStealer", Category.WORLD, 0);
	}

	@Override
	public void setup() {
		minDelay = new SliderSetting("Delay Min", true, 90, 0, 1000, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue() < this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue());
				}
			}
		};
		maxDelay = new SliderSetting("Delay Max", true, 100, 0, 1000, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.minDelay.getValue() > this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.minDelay.getValue());
				}
			}
		};
		closeScreen = new ToggleSetting("CloseScreen", true, false);
		randomize = new ToggleSetting("Randomize", true, true);
		chestOnly = new ToggleSetting("OnlyChests", true, true);
		noGui = new ToggleSetting("NoGui", true, true);
		this.rSetting(minDelay);
		this.rSetting(maxDelay);
		this.rSetting(closeScreen);
		this.rSetting(randomize);
		this.rSetting(chestOnly);
		this.rSetting(noGui);
		super.setup();
	}

	@Override
	public void onEnable() {
		reset();
		super.onEnable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

		if (mc.currentScreen instanceof GuiChest && isInChest) {
			guiChest = (GuiChest) mc.currentScreen;
			if (isEmpty(guiChest) || isInvFull()) {
				reset();
				mc.thePlayer.closeScreen();
				return;
			}
			if (noGui.getValue()) {
				mc.displayGuiScreen(null);
			}
		}

		if (guiChest == null) {
			reset();
			return;
		}

		if (!isInChest || !delayTimer.hasTimePassed(nextDelay)) {
			return;
		}

		if (slotList.isEmpty()) {
			for (int i = 0; i < guiChest.inventoryRows * 9; i++) {
				Slot slot = guiChest.inventorySlots.getSlot(i);
				if (slot.getHasStack()) {
					slotList.add(slot);
				}
			}

			if (!slotList.isEmpty()) {
				slotAmt = slotList.size();
			}
		}

		int i = randomize.getValue() ? MathUtils.randInt(0, slotList.size()) : 0;

		Slot slot = slotList.get(i);
		slotList.remove(slot);

		InventoryUtils.shiftClick(slot.slotNumber, guiChest.inventorySlots.windowId);

		if ((slotList.isEmpty() || isInvFull()) && closeScreen.getValue()) {
			reset();
			mc.thePlayer.closeScreen();
			return;
		}

		delayTimer.reset();
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());

	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C0DPacketCloseWindow) {
			reset();
		}
	}

	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S2DPacketOpenWindow) {
			S2DPacketOpenWindow packet = (S2DPacketOpenWindow) event.getPacket();
			if (!chestOnly.getValue() || packet.getWindowTitle().getUnformattedText().equalsIgnoreCase(I18n.format("container.chest"))) {
				isInChest = true;
			}
		}
	}


	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (!isInChest || !noGui.getValue()) {return;}

		ScaledResolution sr = new ScaledResolution(mc);
		drawString("Stealing chest...", sr.getScaledWidth() / 2, (sr.getScaledHeight() / 2) + 10);

		//Progress bar
		int width = 100;
		int height = 10;
		int x = (sr.getScaledWidth() / 2) - width + 50;
		int y = (sr.getScaledHeight() / 2) - height + 30;

		double minPercent = getPercent(slotAmt - slotList.size(), slotAmt);

		double percent = minPercent + ((getPercent(delayTimer.timePassed(), nextDelay) / 10));

		RenderUtils.drawRect(x, y, x + width, y + height, 0x99000000);
		RenderUtils.drawRect(x, y, x + percent, y + height, 0x99FFFFFF);

	}

	private double getPercent(double currentValue, double totalValue) {
		return ((currentValue / totalValue) * 100);
	}

	private boolean isEmpty(GuiChest gui) {
		for (int i = 0; i < gui.inventoryRows * 9; i++) {
			Slot slot = gui.inventorySlots.getSlot(i);
			if (slot.getHasStack()) {
				return false;
			}
		}
		return true;
	}

	private boolean isInvFull() {
		for(int index = 9; index <= 44; ++index) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (stack == null) {
				return false;
			}
		}

		return true;
	}

	private void drawString(String string, int x, int y) {
		int color;
		HUDModule hudModule = Menace.instance.moduleManager.hudModule;
		switch (hudModule.color.getValue()) {

			case "Fade" :
				color = ColorUtils.fade(hudModule.rainbowSpeed.getValueF(), -y).getRGB();
				break;

			case "Custom" :
				color = new Color(hudModule.red.getValueI(), hudModule.green.getValueI(), hudModule.blue.getValueI(), hudModule.alpha.getValueI()).getRGB();
				break;

			default :
				color = Color.WHITE.getRGB();
				break;
		}

		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			fr.drawCenteredString(string, x, y, color);
		} else {
			mc.fontRendererObj.drawCenteredString(string, x, y, color);
		}
	}

	private int getStringWidth(String string) {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.getStringWidth(string);
		} else {
			return mc.fontRendererObj.getStringWidth(string);
		}
	}

	private int getFontHeight() {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.getHeight();
		} else {
			return mc.fontRendererObj.FONT_HEIGHT;
		}
	}

	private void reset() {
		delayTimer.reset();
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
		slotList.clear();
		isInChest = false;
		guiChest = null;
		slotAmt = 0;
	}

}