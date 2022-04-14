package dev.menace.utils.misc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.network.Packet;

public class Changelog {
	
	public static Queue<String> getChangelog() {
		
		Queue<String> queue = new ConcurrentLinkedDeque<>();
		
		queue.add("This Release:");
		
		queue.add("[/] BugFixes");
		queue.add("[+] Verus Boat Disabler");
		queue.add("[+] Verus Autoblock");
		queue.add("[+] NCP Autoblock");
		queue.add("[+] NCP Crits");
		queue.add("[+] Another ClickGui");
		queue.add("[+] AntiWeb");
		
		return queue;
	}
	
}
