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

public class CommandManager {

	public ArrayList<BaseCommand> cmds = new ArrayList<BaseCommand>();
	public String prefix = ".";
	
	//CMDS
	BindCmd bindCmd = new BindCmd();
	BindsCmd bindsCmd = new BindsCmd();
	ConfigCmd configCmd = new ConfigCmd();
	PrefixCmd prefixCmd = new PrefixCmd();
	VClipCmd vClipCmd = new VClipCmd();
	
	public CommandManager() {
		try
		{
			for(Field field : CommandManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Cmd"))
					continue;

				BaseCommand cmd = (BaseCommand)field.get(this);
				cmds.add(cmd);
			}

		}catch(Exception e)
		{
			String message = "Initializing Menace commands";
			CrashReport report = CrashReport.makeCrashReport(e, message);
		}
	}
	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public void end() {
		Menace.instance.eventManager.unregister(this);
	}
	
	@EventTarget
	public void onChat(EventChatOutput event) {
		if (event.getMessage().startsWith(this.prefix)) {
			parse(event.getMessage().replaceFirst(this.prefix, ""));
			event.cancel();
		}
	}
	
	private void parse(String command) {
		String cmdName = command.split(" ")[0];
		cmds.stream().filter(c -> c.getCmd().equalsIgnoreCase(cmdName)).forEach(cmd -> {
			String[] args = command.split(" ");
			args = Arrays.copyOfRange(args, 1, args.length);
			cmd.call(args);
		});
	}
}
