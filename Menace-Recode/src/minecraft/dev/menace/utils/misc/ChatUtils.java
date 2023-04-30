package dev.menace.utils.misc;

import com.google.gson.JsonObject;
import dev.menace.Menace;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

@JSMapping(111)
public class ChatUtils {

	static Minecraft MC = Menace.instance.MC;

	@MappedName(76)
	public static void message(String message) {

		if (MC.thePlayer == null) return;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", "§0[§4Menace§0]§r " + message);

		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}

	public static void noPrefix(String message) {

		if (MC.thePlayer == null) return;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", message);

		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}

	public static void anticheat(String message) {

		if (MC.thePlayer == null) return;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", "§0[§4MenaceNCP§0]§r " + message);

		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}

	public static void irc(String message) {

		if (MC.thePlayer == null) return;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("text", "§0[§4MenaceIRC§0]§r " + message);

		MC.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
	}

}
