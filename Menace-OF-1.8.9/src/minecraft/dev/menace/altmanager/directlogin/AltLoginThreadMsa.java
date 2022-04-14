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

public final class AltLoginThreadMsa
extends Thread {
    private final String password;
    private String status;
    private final String username;
    private String name;
    private Minecraft mc = Minecraft.getMinecraft();

    public AltLoginThreadMsa(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + this.username + " - offline name)";
            return;
        }
        
    	MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
    	try {
			MicrosoftAuthResult result = authenticator.loginWithCredentials(username, password);
			this.name = result.getProfile().getName();
			this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + this.name + ")";
		} catch (MicrosoftAuthenticationException e) {
			this.status = (Object)((Object)EnumChatFormatting.RED) + "Login failed!";
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

