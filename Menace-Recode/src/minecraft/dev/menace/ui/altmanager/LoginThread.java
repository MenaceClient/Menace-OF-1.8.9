package dev.menace.ui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;

import dev.menace.ui.altmanager.DirectLoginScreen.LoginMode;
import dev.menace.utils.misc.MathUtils;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.client.Minecraft;

public class LoginThread extends Thread {
	
	Minecraft MC = Minecraft.getMinecraft();
	private String status = "§7Waiting...§r";
	private String username;
	private String password;
	private LoginMode loginType;
	
	public LoginThread(String username, String password, LoginMode loginType) {
		this.username = username;
		this.password = password;
		this.loginType = loginType;
		this.status = "§eLogin pending...§r";
	}
	
	@Override
	public void run() {
		if (password.isEmpty() && loginType != loginType.ALTENING && loginType != loginType.ALTENINGPREMIUM && loginType != loginType.MICROSOFT_BROWSER && loginType != loginType.RNG) {
			LoginManager.crackedLogin(username);
			this.status = "§aLogged in as - " + MC.session.getUsername() + " (Cracked)§r";
			return;
		}
		switch (loginType) {
		case ALTENING:
			try {
				LoginManager.alteningLogin(username);
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "§cLogin Failed!§r";
				e.printStackTrace();
			}
			break;
		case ALTENINGPREMIUM:
			try {
				LoginManager.alteningPremiumLogin(username);
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "§cLogin Failed!§r";
				e.printStackTrace();
			}
			break;
		case MICROSOFT:
			try {
				LoginManager.microsoftEmailLogin(username, password);
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (MicrosoftAuthenticationException e) {
				this.status = "§cLogin Failed!§r";
				e.printStackTrace();
			}
			break;
		case MICROSOFT_BROWSER:
			try {
				LoginManager.microsoftBrowserLogin();
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (MicrosoftAuthenticationException e) {
				this.status = "§cLogin Failed!§r";
				e.printStackTrace();
			}
			break;
		case MOJANG:
			try {
				LoginManager.mojangLogin(username, password);
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "§cLogin Failed!§r";
				e.printStackTrace();
			}
			break;
		case RNG:
			String whitelisted_letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678901234567890__";
			String name = "";
			while (name.length() < 14) {
				name = name.concat(String.valueOf(whitelisted_letters.charAt(MathUtils.randInt(0, whitelisted_letters.length()))));
			}
			LoginManager.crackedLogin(name);
			this.status = "§aLogged in as - " + MC.session.getUsername() + " (Cracked)§r";
			break;
		default:
			this.status = "§cThis should not happen!§r";
			break;
		
		}
	}

	public String getStatus() {
		return status;
	}

}
