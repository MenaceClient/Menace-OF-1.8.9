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

			String[] firstNames = new String[]{"Time", "Past", "Future", "Dev", "Fly", "Flying", "Soar", "Soaring", "Power", "Falling", "Fall", "Jump", "Cliff", "Mountain", "Rend", "Red", "Blue", "Green", "Yellow", "Gold", "Demon", "Demonic", "Panda", "Cat", "Kitty", "Kitten", "Zero", "Memory", "Trooper", "XX", "Bandit", "Fear", "Light", "Glow", "Tread", "Deep", "Deeper", "Deepest", "Mine", "Your", "Worst", "Enemy", "Hostile", "Force", "Video", "Game", "Donkey", "Mule", "Colt", "Cult", "Cultist", "Magnum", "Gun", "Assault", "Recon", "Trap", "Trapper", "Redeem", "Code", "Script", "Writer", "Near", "Close", "Open", "Cube", "Circle", "Geo", "Genome", "Germ", "Spaz", "Sped", "Skid", "Shot", "Echo", "Beta", "Alpha", "Gamma", "Omega", "Seal", "Squid", "Money", "Cash", "Lord", "King", "Ominous", "Flow", "Skull", "Submissive"};
			String[] lastNames = new String[]{"Duke","Rest","Fire","Flame","Morrow","Break","Breaker","Numb","Ice","Cold","Rotten","Sick","Sickly","Janitor","Camel","Rooster","Sand","Desert","Dessert","Hurdle","Racer","Eraser","Erase","Big","Small","Short","Tall","Sith","Bounty","Hunter","Cracked","Broken","Sad","Happy","Joy","Joyful","Crimson","Destiny","Deceit","Lies","Lie","Honest","Destined","Bloxxer","Hawk","Eagle","Hawker","Walker","Zombie","Sarge","Capt","Captain","Punch","One","Two","Uno","Slice","Slash","Melt","Melted","Melting","Fell","Wolf","Hound","Legacy","Sharp","Dead","Mew","Chuckle","Bubba","Bubble","Sandwich","Smasher","Extreme","Multi","Universe","Ultimate","Death","Ready","Monkey","Elevator","Wrench","Grease","Head","Theme","Grand","Cool","Kid","Boy","Girl","Vortex","Paradox","Omen", "Bussy"};

			name = firstNames[MathUtils.randInt(0, firstNames.length)] + lastNames[MathUtils.randInt(0, lastNames.length)] + MathUtils.randInt(0, 9999);

			name = name.length() > 16 ? name.substring(0, 16) : name;

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
