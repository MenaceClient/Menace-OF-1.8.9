package dev.menace.ui.altmanager;

import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;

import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.AuthTokens;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginManager {

	private static AltService altService = new AltService();
	
	public static void crackedLogin(String username) {
		Minecraft.getMinecraft().session = new Session(username, UUID.nameUUIDFromBytes(username.getBytes(StandardCharsets.UTF_8)).toString(),
                "-", "legacy");
	}
	
	public static void mojangLogin(String username, String password) throws NoSuchFieldException, IllegalAccessException, AuthenticationException {
		altService.switchService(AltService.EnumAltService.MOJANG);
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
                .createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(username);
        authentication.setPassword(password);
        authentication.logIn();

        Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(),
                authentication.getAuthenticatedToken(), "mojang");
	}
	
	public static void alteningLogin(String username) throws NoSuchFieldException, IllegalAccessException, AuthenticationException {
		altService.switchService(AltService.EnumAltService.THEALTENING);
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
                .createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(username);
        authentication.setPassword("L");
        authentication.logIn();

        Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(),
                authentication.getAuthenticatedToken(), "mojang");
	}
	
	public static void alteningPremiumLoginV1(String apiKey) throws NoSuchFieldException, IllegalAccessException, AuthenticationException {
		TheAltening altening = new TheAltening(apiKey);
		AccountData acc = altening.getAccountData();
		
		if (acc == null) return;
		
		alteningLogin(acc.getToken());
	}
	
	public static void alteningPremiumLoginV2(String apiKey) throws NoSuchFieldException, IllegalAccessException, AuthenticationException {
		TheAltening altening = new TheAltening(apiKey);
		AccountData acc = altening.getAccountDataV2();
		
		if (acc == null) return;
		
		alteningLogin(acc.getToken());
	}
	
	public static void microsoftEmailLogin(String email, String password) throws MicrosoftAuthenticationException {
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
