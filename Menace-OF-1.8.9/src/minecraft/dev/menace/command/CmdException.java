package dev.menace.command;

public abstract class CmdException extends Exception
{
	public CmdException()
	{
		super();
	}
	
	public CmdException(String message)
	{
		super(message);
	}
	
	public abstract void printToChat(Command cmd);
}
