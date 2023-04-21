package dev.menace.command;

import net.minecraft.client.Minecraft;

public abstract class BaseCommand {

	{
		CommandManager.cmds.add(this);
	}

	String cmd;
	String description;
	String[] syntax;
	protected Minecraft mc = Minecraft.getMinecraft();
	
	public BaseCommand(String cmd, String description, String... syntax) {
		this.cmd = cmd;
		this.description = description;
		this.syntax = syntax;
	}

	public abstract void call(String[] args);

	public String getCmd() {
		return cmd;
	}

	public String getDescription() {
		return description;
	}

	public String[] getSyntax() {
		return syntax;
	}
	
}
