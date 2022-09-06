package dev.menace.utils.security;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

	public static @NotNull String getHWID() {
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
	
	/**
     * Opens and reads the URL.
     */

    public static @NotNull List<String> readHWIDURL() {
        List<String> s = new ArrayList<>();
        try {
            final URL url = new URL("http://menaceapi.cf/HWIDS.txt");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return s;
    }

    /**
     * Opens and reads the URL.
     */

    public static List<String> readInfoURL() {
        List<String> s = new ArrayList<>();
        try {
            final URL url = new URL("http://menaceapi.cf/USERINFO.txt");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return s;
    }
    
    public static int getUID() {
    	String UID = null;
    	
    	for (String info : readInfoURL()) {
    		if (info.split(":")[0].equals(getHWID())) {
				UID = info.split(":")[2];
			}
    	}

		assert UID != null;
		return Integer.parseInt(UID);
    }
    
    public static String getUsername() {
    	String username = null;
    	
    	for (String info : readInfoURL()) {
    		if (info.split(":")[0].equals(getHWID())) {
				username = info.split(":")[1];
			}
    	}
    	
    	return username;
    }
    
    public static MenaceUser getUser() {
    	return new MenaceUser(getUsername(), getHWID(), getUID());
    }

}
