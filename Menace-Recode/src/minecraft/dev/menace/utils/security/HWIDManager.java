package dev.menace.utils.security;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

	public static String getHWID() {
		String s = "";
		String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
		byte[] bytes = null;
		try {
			bytes = main.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		byte[] var6 = md5;
		int var7 = md5.length;

		for(int var8 = 0; var8 < var7; ++var8) {
			byte b = var6[var8];
			s = s + Integer.toHexString(b & 255 | 768).substring(0, 3);
			if (i != md5.length - 1) {
				s = s + "-";
			}

			++i;
		}

		return s;
	}
	
	/**
     * Opens and reads the URL.
     */

    public static @NotNull List<String> readHWIDURL() {
        List<String> s = new ArrayList<>();
        try {
            final URL url = new URL("http://menaceapi.cf/HWIDS.txt");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
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
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
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
    
    public static int getUID(String HWID) {
    	String UID = null;
    	
    	for (String info : readInfoURL()) {
    		if (info.split(":")[0].equals(getHWID())) {
				UID = info.split(":")[2];
			}
    	}
    	
    	return Integer.valueOf(UID);
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
    	return new MenaceUser(getUsername(), getHWID(), getUID(getHWID()));
    }

}
