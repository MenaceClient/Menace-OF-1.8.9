package dev.menace.command;

import dev.menace.Menace;
import dev.menace.command.commands.*;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventChatOutput;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
	
	public final AdvertiseCmd advertiseCmd = new AdvertiseCmd();
	public final AnnoyCmd annoyCmd = new AnnoyCmd();
	public final BindCmd bindCmd = new BindCmd();
	public final BindsCmd bindsCmd = new BindsCmd();
	public final DamageCmd damageCmd = new DamageCmd();
	public final HelpCmd helpCmd = new HelpCmd();
	public final PluginsCmd pluginsCmd = new PluginsCmd();
	public final TeleportCmd teleportCmd = new TeleportCmd();
	public final ToggleCmd toggleCmd = new ToggleCmd();
	public final SettingCmd settingCmd = new SettingCmd();
	public final SpamCmd spamCmd = new SpamCmd();
	public final VClipCmd vClipCmd = new VClipCmd();
	
	public ArrayList<Command> cmds = new ArrayList<Command>();
	
	public static String MENACE_PREFIX = ".";
	
	public CommandManager() {
		
		try
		{
			for(Field field : CommandManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Cmd"))
					continue;
				
				Command cmd = (Command)field.get(this);
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
	
	@EventTarget
	public void onSendChatMessage(EventChatOutput event) {
		String message = event.getOriginalMessage().trim();
		
		if(!message.startsWith(MENACE_PREFIX))
			return;
		
		if (message.startsWith(MENACE_PREFIX + "say")) {
			C01PacketChatMessage packet = new C01PacketChatMessage(message.replace(MENACE_PREFIX + "say", ""));
			Menace.instance.MC.thePlayer.sendQueue.addToSendQueue(packet);
			event.setCancelled(true);
			return;
		}
		
		event.setCancelled(true);
		process(message.substring(1));
	}
	
	public void process(String input)
	{
		try
		{
			Command cmd = parseCmd(input);
			
			runCmd(cmd, input);
			
		}catch(CmdNotFoundException e)
		{
			e.printToChat();
		}
	}
	
	private Command parseCmd(String input) throws CmdNotFoundException
	{
		String cmdName = input.split(" ")[0];
		Command cmd = null;
		
		for (Command cmdd: cmds) {
			
			String[] name = cmdd.getName().split("-");
			
			for (String namee: name) {
				
				if (namee.equalsIgnoreCase(cmdName)) {
					cmd = cmdd;
					return cmd;
				}
				
			}
		}
		throw new CmdNotFoundException(input);
	}
	
	private void runCmd(Command cmd, String input)
	{
		String[] args = input.split(" ");
		args = Arrays.copyOfRange(args, 1, args.length);
		
		try
		{
			cmd.call(args);
			
		}catch(CmdException e)
		{
			e.printToChat(cmd);
			
		}catch(Throwable e)
		{
			CrashReport report = CrashReport.makeCrashReport(e, "Running Menace command");
		}
	}
	
	private static class CmdNotFoundException extends Exception
	{
		private final String input;
		
		public CmdNotFoundException(String input)
		{
			super();
			this.input = input;
		}
		
		public void printToChat()
		{
			String cmdName = input.split(" ")[0];
			ChatUtils.error("Unknown command: " + MENACE_PREFIX + cmdName);
			
			StringBuilder helpMsg = new StringBuilder();
			
			if(input.startsWith("/"))
			{
				helpMsg.append("Use \".say " + input + "\"");
				helpMsg.append(" to send it as a chat command.");
				
			}else
			{
				helpMsg.append("Type \"" + MENACE_PREFIX +"help\" for a list of commands or ");
				helpMsg.append("\"" + MENACE_PREFIX + "say " + MENACE_PREFIX + input + "\"");
				helpMsg.append(" to send it as a chat message.");
			}
			
			ChatUtils.message(helpMsg.toString());
		}
	}
	
}
