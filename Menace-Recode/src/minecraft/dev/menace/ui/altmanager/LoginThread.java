package dev.menace.ui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;

import dev.menace.ui.altmanager.DirectLoginScreen.LoginMode;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.client.Minecraft;

public class LoginThread extends Thread {
	
	Minecraft MC = Minecraft.getMinecraft();
	private String status = "�7Waiting...�r";
	private String username;
	private String password;
	private LoginMode loginType;
	
	public LoginThread(String username, String password, LoginMode loginType) {
		this.username = username;
		this.password = password;
		this.loginType = loginType;
		this.status = "�eLogin pending...�r";
	}
	
	@Override
	public void run() {
		if (password.isEmpty() && loginType != loginType.ALTENING && loginType != loginType.ALTENINGPREMIUM && loginType != loginType.MICROSOFT_BROWSER) {
			LoginManager.crackedLogin(username);
			this.status = "�aLogged in as - " + MC.session.getUsername() + " (Cracked)�r";
			return;
		}
		switch (loginType) {
		case ALTENING:
			try {
				LoginManager.alteningLogin(username);
				this.status = "�aLogged in as - " + MC.session.getUsername() + "�r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "�cLogin Failed!�r";
				e.printStackTrace();
			}
			break;
		case ALTENINGPREMIUM:
			try {
				LoginManager.alteningPremiumLoginV2(username);
				this.status = "�aLogged in as - " + MC.session.getUsername() + "�r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "�cLogin Failed!�r";
				e.printStackTrace();
			}
			break;
		case MICROSOFT:
			try {
				LoginManager.microsoftEmailLogin(username, password);
				this.status = "�aLogged in as - " + MC.session.getUsername() + "�r";
			} catch (MicrosoftAuthenticationException e) {
				this.status = "�cLogin Failed!�r";
				e.printStackTrace();
			}
			break;
		case MICROSOFT_BROWSER:
			try {
				LoginManager.microsoftBrowserLogin();
				this.status = "�aLogged in as - " + MC.session.getUsername() + "�r";
			} catch (MicrosoftAuthenticationException e) {
				this.status = "�cLogin Failed!�r";
				e.printStackTrace();
			}
			break;
		case MOJANG:
			try {
				LoginManager.mojangLogin(username, password);
				this.status = "�aLogged in as - " + MC.session.getUsername() + "�r";
			} catch (NoSuchFieldException | IllegalAccessException | AuthenticationException e) {
				this.status = "�cLogin Failed!�r";
				e.printStackTrace();
			}
			break;
		default:
			this.status = "�cThis should not happen!�r";
			break;
		
		}
	}

	public String getStatus() {
		return status;
	}

}
