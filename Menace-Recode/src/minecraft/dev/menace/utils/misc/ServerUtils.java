package dev.menace.utils.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.menace.Menace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class ServerUtils {

	private static final Minecraft MC = Menace.instance.MC;
	
    public static ServerData serverData;

    public static void connectToLastServer(GuiScreen parent) {
        if(serverData == null)
            return;

        MC.displayGuiScreen(new GuiConnecting(parent, MC, serverData));
    }

    public static String getLastServerIp() {
        if (serverData == null)
            return "SinglePlayer";
        return serverData.serverIP;
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
