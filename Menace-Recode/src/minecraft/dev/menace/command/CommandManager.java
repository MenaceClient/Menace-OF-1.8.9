package dev.menace.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import dev.menace.Menace;
import dev.menace.command.commands.*;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventChatOutput;
import dev.menace.module.ModuleManager;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.crash.CrashReport;
import org.jetbrains.annotations.NotNull;

public class CommandManager {

	public static ArrayList<BaseCommand> cmds = new ArrayList<BaseCommand>();
	public String prefix = ".";
	public String ircPrefix = "#";
	
	//CMDS
	BindCmd bindCmd = new BindCmd();
	BindsCmd bindsCmd = new BindsCmd();
	ConfigCmd configCmd = new ConfigCmd();
	IRCCmd ircCmd = new IRCCmd();
	PrefixCmd prefixCmd = new PrefixCmd();
	VClipCmd vClipCmd = new VClipCmd();

	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public void end() {
		Menace.instance.eventManager.unregister(this);
	}
	
	@EventTarget
	public void onChat(@NotNull EventChatOutput event) {
		if (event.getMessage().startsWith(this.prefix)) {
			parse(event.getMessage().replaceFirst(this.prefix, ""));
			event.cancel();
		} else if (event.getMessage().startsWith(this.ircPrefix)) {
			Menace.instance.irc.sendMsg(event.getMessage().replaceFirst(this.ircPrefix, ""));
			event.cancel();
		}
	}
	
	private void parse(@NotNull String command) {
		String cmdName = command.split(" ")[0];
		cmds.stream().filter(c -> c.getCmd().equalsIgnoreCase(cmdName)).forEach(cmd -> {
			String[] args = command.split(" ");
			args = Arrays.copyOfRange(args, 1, args.length);
			cmd.call(args);
		});
	}
}
