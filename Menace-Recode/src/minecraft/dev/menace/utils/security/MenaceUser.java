package dev.menace.utils.security;

import java.util.UUID;

public class MenaceUser {

	private final String username;
	private final String hwid;
	private final int uid;

	private final String discord;
	
	public MenaceUser(String username, String discord, String hwid, int uid) {
		this.username = username;
		this.discord = discord;
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

	public String getDiscord() {return discord;}
	
}
