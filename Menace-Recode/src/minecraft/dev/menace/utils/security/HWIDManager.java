package dev.menace.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

}
