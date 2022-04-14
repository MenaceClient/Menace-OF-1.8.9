package dev.menace.utils.misc;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordRP {

	private boolean running = true;
	private long created = 0;
	
	public void start() {
		this.created = System.currentTimeMillis();
		
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			
			@Override
			public void apply(DiscordUser user) {
				update("Menace Client", "");
			}
			
		}).build();
		
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
	
	public void update(String firstLine, String secondLine) {
		DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
		b.setBigImage("large", "");
		b.setDetails(firstLine);
		b.setStartTimestamps(created);
		
		DiscordRPC.discordUpdatePresence(b.build());
	}
	
}
