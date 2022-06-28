package dev.menace.utils.security;

public class MenaceUser {

	private String username;
	private String hwid;
	private int uid;
	
	public MenaceUser(String username, String hwid, int uid) {
		this.username = username;
		this.hwid = hwid;
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public String getHwid() {
		return hwid;
	}

	public int getUID() {
		return uid;
	}
	
}
