package dev.menace.utils.security;

import dev.menace.Menace;
import dev.menace.utils.misc.ChatUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

public class HWIDManager {

	public static String getHWIDNew() {
		String hwid = "";

		//figure out what os we are on
		String os = System.getProperty("os.name").toLowerCase();
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			String serial = "";
			String processorId = "";

			if (os.contains("win")) {
				ChatUtils.message("Windows OS detected");

				//Get the windows mac address because the serial number does not work on some peoples machines including mine :<
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface current = interfaces.nextElement();
					if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
					byte[] mac = current.getHardwareAddress();
					if (mac != null) {
						StringBuilder sb = new StringBuilder();
						for (byte b : mac) {
							sb.append(String.format("%02X", b));
						}
						serial = sb.toString();
						break;
					}
				}

				//Get the windows processor id
				Process processorIdProcess = Runtime.getRuntime().exec("wmic cpu get ProcessorId");
				processorIdProcess.getOutputStream().close();
				BufferedReader processorIdReader = new BufferedReader(new InputStreamReader(processorIdProcess.getInputStream()));
				processorIdReader.readLine();
				processorIdReader.readLine();
				processorId = processorIdReader.readLine().trim();
				processorIdReader.close();
				processorIdProcess.destroy();

			} else if (os.contains("mac")) {

				//Get the mac serial number
				Process serialNumProcess = Runtime.getRuntime().exec("system_profiler SPHardwareDataType | grep 'Serial Number (system):'");
				BufferedReader serialNumReader = new BufferedReader(new InputStreamReader(serialNumProcess.getInputStream()));
				serialNumReader.readLine();
				serial = serialNumReader.readLine().trim();
				serial = serial.split(" ")[2];
				serialNumReader.close();
				serialNumProcess.destroy();

				//Get the mac processor id
				Process processorIdProcess = Runtime.getRuntime().exec("system_profiler SPHardwareDataType | grep 'Processor Name:'");
				BufferedReader processorIdReader = new BufferedReader(new InputStreamReader(processorIdProcess.getInputStream()));
				processorId = processorIdReader.readLine().trim();
				processorId = processorId.split(" ")[2];
				processorIdReader.close();
				processorIdProcess.destroy();

			} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {

				//Get the linux serial number
				Process serialNumProcess = Runtime.getRuntime().exec("lshw -class baseboard | grep 'serial:'");
				BufferedReader serialNumReader = new BufferedReader(new InputStreamReader(serialNumProcess.getInputStream()));
				serial = serialNumReader.readLine().trim();
				serialNumReader.close();
				serialNumProcess.destroy();

				//Get the linux processor id
				Process processorIdProcess = Runtime.getRuntime().exec("lshw -class processor | grep 'product:'");
				BufferedReader processorIdReader = new BufferedReader(new InputStreamReader(processorIdProcess.getInputStream()));
				processorId = processorIdReader.readLine().trim();
				processorIdReader.close();
				processorIdProcess.destroy();

			} else {
				return "Unknown";
			}

			//byte[] bytes = (serial + processorId).getBytes(StandardCharsets.UTF_8);
			//byte[] digest = md.digest(bytes);
			//hwid = Base64.getEncoder().encodeToString(digest);
			return (serial + " : " + processorId);

		} catch (IOException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getHWID() {
		StringBuilder s = new StringBuilder();
		String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
		byte[] bytes;
		bytes = main.getBytes(StandardCharsets.UTF_8);
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert messageDigest != null;
		byte[] md5 = messageDigest.digest(bytes);

		int i = 0;

		for (byte b : md5) {
			s.append(Integer.toHexString(b & 255 | 768), 0, 3);
			if (i != md5.length - 1) {
				s.append("-");
			}

			++i;
		}

		return s.toString();
	}

	public static boolean isWhitelisted() {
		try {
			final URL url = new URL(Menace.instance.apiURL + "/isWhitelisted/" + getHWID());
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

				String pwd = getHWID() + "G1ZpUxclnWg7V5zWtEaFUo6rkCkb6iuwdZzGk5JB1CYGCZJe";

				byte[] decodedSalt = pwd.getBytes(StandardCharsets.UTF_8);

				MessageDigest md = MessageDigest.getInstance("SHA-512");

				byte[] hashed = md.digest(decodedSalt);

				StringBuilder s = new StringBuilder();
				for (byte b : hashed) {
					s.append(String.format("%02x", b));
				}

				if (response.toString().equals("cope")) {
					return false;
				} else if (!response.toString().equals(s.toString())) {
					AntiSkidUtils.terminate("We detected you attempting to intercept the HWID system if you think this is an error please contact the admins.", 0x03, "Someone tried to intercept the HWID system.");
					return false;
				} else return response.toString().equals(s.toString());
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String sendDiscordRequest(String id) {
		try {
			final URL url = new URL(Menace.instance.apiURL + "/getDiscordByID/" + id);
			HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			uc.setRequestMethod("GET");
			int responseCode = uc.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						uc.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				return response.toString();
			} else {
				return "null";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}

    public static int getUID() {
		try {
			final URL url = new URL(Menace.instance.apiURL + "/getUID/" + getHWID());
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

				return Integer.parseInt(response.toString());
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    public static String getUsername() {
		try {
			final URL url = new URL(Menace.instance.apiURL + "/getUsername/" + getHWID());
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

				return response.toString();
			} else {
				return "null";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
    }

	public static String getDiscord() {
		String discordID;
		try {
			final URL url = new URL(Menace.instance.apiURL + "/getDiscordID/" + getHWID());
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

				discordID = response.toString();
			} else {
				return "null";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}

		return sendDiscordRequest(discordID);
	}

	public static MenaceUser getUser() {
    	return new MenaceUser(getUsername(), getDiscord(), getHWID(), getUID());
    }

}
