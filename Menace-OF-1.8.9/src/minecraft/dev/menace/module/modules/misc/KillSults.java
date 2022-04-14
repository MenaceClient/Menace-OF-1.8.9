package dev.menace.module.modules.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S42PacketCombatEvent;

public class KillSults extends Module {
	
	public KillSults() {
		super("KillSults", 0, Category.MISC);
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S02PacketChat) {
			S02PacketChat packet = (S02PacketChat) event.getPacket();
			String chat = packet.getChatComponent().getUnformattedTextForChat();
			if ((chat.contains("was slain by")  || chat.contains("was killed by")) && chat.contains(MC.thePlayer.getName())) {
				String killed;
				
				//if (chat.startsWith("§") || chat.startsWith("\u00a7")) {
					
					//chat = chat.substring(1);
					//killed = chat.split(" ")[0];
					
				//} else {
					killed = chat.split(" ")[0];
				//}
				
				insult(killed);
			}
		}
	}
	
	public void insult(String killed) {
		
		if (killed == MC.thePlayer.getName()) {
			return;
		}
		
		List <String> ascii = new ArrayList <String> (26);

		for (char c = 'A'; c <= 'Z'; c++) {
		    ascii.add (String.valueOf (c));
		}
		
		Random rand = new Random();
		String random = ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		
		ChatUtils.out("L [" + random + "]");
	}

}
