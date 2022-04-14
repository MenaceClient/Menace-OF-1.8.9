package dev.menace.altmanager.directlogin;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThreadBrowser
extends Thread {
    private String status;
    private String name;
    private Minecraft mc = Minecraft.getMinecraft();

    public AltLoginThreadBrowser() {
        super("Alt Login Thread");
        this.status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {    
    	MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
		try {
			MicrosoftAuthResult result = authenticator.loginWithWebview();
			//System.out.println(result.getRefreshToken());
			MinecraftProfile profile = result.getProfile();
			this.mc.session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + profile.getName() + ")";
            this.name = profile.getName();
		} catch (MicrosoftAuthenticationException e) {
			e.printStackTrace();
		}
    }
    
    public String getUsername() {
    	return this.name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
