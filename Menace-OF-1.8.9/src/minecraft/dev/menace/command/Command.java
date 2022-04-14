package dev.menace.command;

import java.util.Objects;

import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;

public abstract class Command {
	protected Minecraft MC = Minecraft.getMinecraft();
	
	private final String name;
	private final String description;
	private final String[] syntax;
	
	public Command(String name, String description, String... syntax)
	{
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
		
		Objects.requireNonNull(syntax);
		if(syntax.length > 0)
			syntax[0] = "Syntax: " + syntax[0];
		this.syntax = syntax;
	}
	
	public abstract void call(String[] args) throws CmdException;
	
	public final String getName()
	{
		return /*CommandManager.MENACE_PREFIX +*/ name;
	}
	
	public String getPrimaryAction()
	{
		return "";
	}
	
	public final String getDescription()
	{
		String description = this.description;
		
		description += "\n";

		if(syntax.length > 0) {
			for(String line : syntax) {
				description +=  line + "\n";
			}
		}
		
		return description;
	}
	
	public final String[] getSyntax()
	{
		return syntax;
	}
	
	public final void printHelp()
	{
		ChatUtils.message("---§3" + this.name.split("-")[0] + "§r---");
		
		for(String line : description.split("\n"))
			ChatUtils.message(line);
		
		for(String line : syntax)
			ChatUtils.message(line);
		
	}
}
