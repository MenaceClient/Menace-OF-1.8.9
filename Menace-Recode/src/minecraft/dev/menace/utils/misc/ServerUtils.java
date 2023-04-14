package dev.menace.utils.misc;

import dev.menace.Menace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerUtils {

	private static final Minecraft MC = Menace.instance.MC;
	
    public static ServerData serverData;

    public static void connectToLastServer(GuiScreen parent) {
        if(serverData == null)
            return;

        Menace.instance.discordRP.update("Bypassing " + serverData.serverIP);
        Menace.instance.hudManager.gameStatsElement.reset();

        new Thread() {
            @Override
            public void run() {
                //Send Menace servers the server ip
                try {
                    final URL url = new URL("https://menaceapi.cf/updateUser/" + serverData.serverIP + "/" + Menace.instance.user.getUsername() + "/" + MC.session.getUsername() + "/false");
                    HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
                    uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                    uc.setRequestMethod("GET");
                    int responseCode = uc.getResponseCode();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Menace.instance.updateOnline();
                super.run();
            }
        }.start();

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
