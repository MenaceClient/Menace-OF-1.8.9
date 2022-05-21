package com.thealtening;

import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author trol
 * @since 10/31/18
 */
public class AltService {

    private EnumAltService currentService;

    public void switchService(EnumAltService enumAltService) throws NoSuchFieldException, IllegalAccessException {

        if (currentService == enumAltService)
            return;
        reflectionFields(enumAltService.hostname);

        currentService = enumAltService;
    }

    private void reflectionFields(String authServer) throws NoSuchFieldException, IllegalAccessException {
		final String useSecureStart = authServer.contains("thealtening") ? "http" : "https";
		YggdrasilUserAuthentication.ROUTE_AUTHENTICATE = constantURL(useSecureStart+"://authserver." + authServer + ".com/authenticate");
		YggdrasilUserAuthentication.ROUTE_INVALIDATE = constantURL(useSecureStart+"://authserver" + authServer + "com/invalidate");
		YggdrasilUserAuthentication.ROUTE_REFRESH = constantURL(useSecureStart+"://authserver." + authServer + ".com/refresh");
		YggdrasilUserAuthentication.ROUTE_VALIDATE = constantURL(useSecureStart+"://authserver." + authServer + ".com/validate");
		YggdrasilUserAuthentication.ROUTE_SIGNOUT = constantURL(useSecureStart+"://authserver." + authServer + ".com/signout");
		YggdrasilUserAuthentication.BASE_URL = useSecureStart+ "://authserver." + authServer + ".com/";
		YggdrasilMinecraftSessionService.BASE_URL = useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/";
		YggdrasilMinecraftSessionService.JOIN_URL = constantURL(useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/join");
		YggdrasilMinecraftSessionService.CHECK_URL = constantURL(useSecureStart+"://sessionserver." + authServer + ".com/session/minecraft/hasJoined");
		YggdrasilMinecraftSessionService.WHITELISTED_DOMAINS = new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"};
    }

    private URL constantURL(final String url) {
        try {
            return new URL(url);
        } catch (final MalformedURLException ex) {
            throw new Error("Couldn't create constant for " + url, ex);
        }
    }

    public EnumAltService getCurrentService() {
        if (currentService == null) currentService = EnumAltService.MOJANG;

        return currentService;
    }

    public enum EnumAltService {

        MOJANG("mojang"), THEALTENING("thealtening");
        String hostname;

        EnumAltService(String hostname) {
            this.hostname = hostname;
        }
    }
}
