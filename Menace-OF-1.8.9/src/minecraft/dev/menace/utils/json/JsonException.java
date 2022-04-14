package dev.menace.utils.json;

public final class JsonException extends Exception
{
	public JsonException()
	{}
	
	public JsonException(String message)
	{
		super(message);
	}
	
	public JsonException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public JsonException(Throwable cause)
	{
		super(cause);
	}
}
