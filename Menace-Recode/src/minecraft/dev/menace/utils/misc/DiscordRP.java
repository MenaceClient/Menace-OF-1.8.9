package dev.menace.utils.misc;

import dev.menace.Menace;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRP {

	private boolean running = true;
	private long created = 0;
	
	public void start() {
		this.created = System.currentTimeMillis();
		
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> update("Authenticating...")).build();
		
		DiscordRPC.discordInitialize("815961665652785172", handlers, true);
		
		new Thread("Discord RPC Callback") {
			
			@Override
			public void run() {
				
				while (running) {
					DiscordRPC.discordRunCallbacks();
				}
				
			}
			
		}.start();
		
	}
	
	public void stop() {
		this.running = false;
		DiscordRPC.discordShutdown();
	}
	
	public void update(String line) {
		DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(line);
		b.setBigImage("large", "https://menaceclient.ml/");
		if (Menace.instance.user != null) {
			b.setDetails(Menace.instance.user.getUsername() + " [" + Menace.instance.user.getUID() + "]");
		}
		b.setStartTimestamps(created);

		DiscordRPC.discordUpdatePresence(b.build());
	}
	
}
