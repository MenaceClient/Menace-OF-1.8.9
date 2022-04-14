package dev.menace.command.commands;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.TickTimer;
import joptsimple.internal.Strings;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

public class PluginsCmd extends Command {

	private boolean toggled = false;
	private TickTimer timer = new TickTimer();
	
	public PluginsCmd() {
		super("Plugins", "Get a servers plugins.", ".plugins");
	}

	@Override
	public void call(String[] args) throws CmdException {
		if (!toggled) {
	        if (MC.thePlayer == null) return;
	        MC.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
	        timer.reset();
			Menace.instance.eventManager.register(this);
			toggled = !toggled;
		} else {
			Menace.instance.eventManager.unregister(this);
			toggled = !toggled;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
        timer.update();
        if (timer.hasTimePassed(20)) {
            ChatUtils.message("§cPlugins check timed out...");
            timer.reset();
            Menace.instance.eventManager.unregister(this);
        }
	}
	
	@EventTarget
	public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S3APacketTabComplete) {
        	ArrayList<String> plugins = new ArrayList<String>();
            String[] commands = ((S3APacketTabComplete) event.getPacket()).func_149630_c();

            for (String command1 : commands) {
                String[] command = command1.split(":");
                if (command.length > 1) {
                    String pluginName = command[0].replace("/", "");
                    if (!plugins.contains(pluginName)) plugins.add(pluginName);
                }
            }

            if (!plugins.isEmpty()) {
            	ChatUtils.message("§aPlugins §7(§8" + plugins.size() + "§7): §c" + Strings.join(plugins, "§7, §c"));
            } else {
            	ChatUtils.message("§cNo plugins found.");
            }

            Menace.instance.eventManager.unregister(this);
            timer.reset();
        }
	}

}
