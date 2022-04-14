package dev.menace.utils.misc;

import com.google.gson.JsonObject;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventChatOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class ChatUtils {

	static Minecraft MC = Menace.instance.MC;
	
	private static final String MENACE_PREFIX =
			ColorUtils.parse("§0[§4Menace§0]§r ");
	private static final String WARNING_PREFIX =
			ColorUtils.parse("§0[§6§lWARNING§0]§r ");
	private static final String ERROR_PREFIX =
			ColorUtils.parse("§0[§4§lERROR§0]§r ");
	private static final String SYNTAX_ERROR_PREFIX =
			ColorUtils.parse("§4Syntax error:§r ");
	private static final String ANTICHEAT_PREFIX =
			ColorUtils.parse("§0[§4§lMENACE ANTICHEAT§0]§r ");
	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public static void message(String message) {
		
		if (MC.thePlayer == null) return;
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", MENACE_PREFIX + message);
		
		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}
	
	public static void warning(String warning) {
		
		if (MC.thePlayer == null) return;
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", WARNING_PREFIX + warning);
		
		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}
	
	public static void error(String error) {
		
		if (MC.thePlayer == null) return;
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", ERROR_PREFIX + error);
		
		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}
	
	public static void ac(String message) {
		
		if (MC.thePlayer == null) return;
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", ANTICHEAT_PREFIX + message);
		
		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}
	
	public static void out(String message) {
		MC.thePlayer.sendChatMessage(message);
	}
	
	@EventTarget
	public void onChat(EventChatOutput event) {
		String message = event.getOriginalMessage().trim();
		
		if (message.contains("$HEALTH$")) {
			message = message.replace("$HEALTH$", String.valueOf(MC.thePlayer.getHealth()));
		}
		
		event.setMessage(message);
	}
	
}
