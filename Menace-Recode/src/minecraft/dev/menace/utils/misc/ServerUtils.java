package dev.menace.utils.misc;

import dev.menace.Menace;
import dev.menace.ui.custom.MenaceMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class ServerUtils {

	private static Minecraft MC = Menace.instance.MC;
	
    public static ServerData serverData;

    public static void connectToLastServer(GuiScreen parent) {
        if(serverData == null)
            return;

        MC.displayGuiScreen(new GuiConnecting(parent, MC, serverData));
    }

    public static String getRemoteIp() {
        String serverIp = "SinglePlayer";

        if (MC.theWorld!=null && MC.theWorld.isRemote) {
            final ServerData serverData = MC.getCurrentServerData();
            if(serverData != null)
                serverIp = serverData.serverIP;
        }

        return serverIp;
    }
}
