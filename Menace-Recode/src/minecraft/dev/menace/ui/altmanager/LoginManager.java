package dev.menace.ui.altmanager;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginManager {
	
	public static void crackedLogin(String username) {
		Minecraft.getMinecraft().session = new Session(username, UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8)).toString(),
                "-", "legacy");
	}
	
	public static void microsoftEmailLogin(String email, String password) throws MicrosoftAuthenticationException, NoSuchFieldException, IllegalAccessException {
		MicrosoftAuthenticator auth = new MicrosoftAuthenticator();
		MicrosoftAuthResult result = auth.loginWithCredentials(email, password);
		MinecraftProfile profile = result.getProfile();
		
		Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
	}
	
	public static void microsoftRefreshTokenLogin(String token) throws MicrosoftAuthenticationException {
		MicrosoftAuthenticator auth = new MicrosoftAuthenticator();
		MicrosoftAuthResult result = auth.loginWithRefreshToken(token);
		MinecraftProfile profile = result.getProfile();
		
		Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
	}
	
	public static void microsoftBrowserLogin() throws MicrosoftAuthenticationException {
		MicrosoftAuthenticator auth = new MicrosoftAuthenticator();
		MicrosoftAuthResult result = auth.loginWithWebview();
		MinecraftProfile profile = result.getProfile();
		
		Minecraft.getMinecraft().session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
	}
	
}
