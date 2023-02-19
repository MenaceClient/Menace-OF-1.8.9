package dev.menace.ui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;

import dev.menace.ui.altmanager.DirectLoginScreen.LoginMode;
import dev.menace.utils.misc.MathUtils;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginThread extends Thread {
	
	Minecraft MC = Minecraft.getMinecraft();
	private String status = "§7Waiting...§r";
	private final String username;
	private final String password;
	private final LoginMode loginType;
	
	public LoginThread(String username, String password, LoginMode loginType) {
		this.username = username;
		this.password = password;
		this.loginType = loginType;
		this.status = "§eLogin pending...§r";
	}
	
	@Override
	public void run() {
		if (password.isEmpty() && loginType != LoginMode.MICROSOFT_BROWSER && loginType != LoginMode.RNG) {
			LoginManager.crackedLogin(username);
			this.status = "§aLogged in as - " + MC.session.getUsername() + " (Cracked)§r";
			return;
		}
		switch (loginType) {
		case MICROSOFT:
			try {
				LoginManager.microsoftEmailLogin(username, password);
				this.status = "§aLogged in as - " + MC.session.getUsername() + "§r";
			} catch (MicrosoftAuthenticationException | NoSuchFieldException | IllegalAccessException e) {
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
		case RNG:
			String name = "";
			try {
				final URL url = new URL("https://menacenamegen.pxzlz.repl.co/name");
				HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
				uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
				uc.setRequestMethod("GET");
				int responseCode = uc.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
					String inputLine;
					StringBuilder response = new StringBuilder();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					name = response.toString();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (name.equals("")) {
				String whitelisted_letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678901234567890__";
				while (name.length() < 14) {
					name = name.concat(String.valueOf(whitelisted_letters.charAt(MathUtils.randInt(0, whitelisted_letters.length()))));
				}
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
