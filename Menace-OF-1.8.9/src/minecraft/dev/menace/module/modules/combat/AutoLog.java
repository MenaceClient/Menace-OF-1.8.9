package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C69PacketMenaceOutbound;
import net.minecraft.realms.RealmsBridge;

public class AutoLog extends Module {

	//Settings
	StringSetting mode;
	DoubleSetting health;
	
	public AutoLog() {
		super("AutoLog", 0, Category.COMBAT);
	}

	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		options.add("Disconnect");
		options.add("Invalid Packet");
		options.add("Crash Game");
		mode = new StringSetting("Mode", this, "Disconnect", options);
		health = new DoubleSetting("Health", this, 5, 1, 10);
		this.rSetting(mode);
		this.rSetting(health);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.thePlayer.getHealth() <= health.getValue() && !MC.thePlayer.isDead) {
			switch (mode.getString()) {
				case "Disconnect":
					boolean flag = MC.isIntegratedServerRunning();
					boolean flag1 = MC.isConnectedToRealms();
					MC.theWorld.sendQuittingDisconnectingPacket();
					MC.loadWorld((WorldClient)null);

					if (flag)
					{
						MC.displayGuiScreen(new GuiMainMenu());
					}
					else if (flag1)
					{
						RealmsBridge realmsbridge = new RealmsBridge();
						realmsbridge.switchToRealms(new GuiMainMenu());
					}	
					else
					{
						MC.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
					}
					break;

				case "Invalid Packet":
					MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C69PacketMenaceOutbound());
					break;
					
				case "Crash Game":
					MC.shutdownMinecraftApplet();
					break;

			}
		}
	}

}
