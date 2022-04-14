package dev.menace.utils.misc;

import java.awt.Color;

public class ColorUtils {

	private static long startTime = System.currentTimeMillis();

	public static String parse(String message) {
		message = message.replaceAll("§0", black());
		message = message.replaceAll("§1", darkBlue());
		message = message.replaceAll("§2", darkGreen());
		message = message.replaceAll("§3", turquiose());
		message = message.replaceAll("§4", darkRed());
		message = message.replaceAll("§5", purple());
		message = message.replaceAll("§6", gold());
		message = message.replaceAll("§7", gray());
		message = message.replaceAll("§8", darkGray());
		message = message.replaceAll("§9", lightBlue());
		message = message.replaceAll("§a", lime());
		message = message.replaceAll("§b", aqua());
		message = message.replaceAll("§c", lightRed());
		message = message.replaceAll("§d", pink());
		message = message.replaceAll("§e", yellow());
		message = message.replaceAll("§f", white());
		message = message.replaceAll("§k", obfuscated());
		message = message.replaceAll("§l", bold());
		message = message.replaceAll("§m", strikethrough());
		message = message.replaceAll("§n", underline());
		message = message.replaceAll("§o", italic());
		message = message.replaceAll("§r", reset());
		message = message.replaceAll("§n", newLine());
		return message;
	}

	public static String black() {
		return "\u00a70";
	}

	public static String darkBlue() {
		return "\u00a71";
	}

	public static String darkGreen() {
		return "\u00a72";
	}

	public static String turquiose() {
		return "\u00a73";
	}

	public static String darkRed() {
		return "\u00a74";
	}

	public static String purple() {
		return "\u00a75";
	}

	public static String gold() {
		return "\u00a76";
	}

	public static String gray() {
		return "\u00a77";
	}

	public static String darkGray() {
		return "\u00a78";
	}

	public static String lightBlue() {
		return "\u00a79";
	}

	public static String lime() {
		return "\u00a7a";
	}

	public static String aqua() {
		return "\u00a7b";
	}

	public static String lightRed() {
		return "\u00a7c";
	}

	public static String pink() {
		return "\u00a7d";
	}

	public static String yellow() {
		return "\u00a7e";
	}

	public static String white() {
		return "\u00a7f";
	}

	public static String obfuscated() {
		return "\u00a7k";
	}

	public static String bold() {
		return "\u00a7l";
	}

	public static String strikethrough() {
		return "\u00a7m";
	}

	public static String underline() {
		return "\u00a7n";
	}

	public static String italic() {
		return "\u00a7o";
	}

	public static String reset() {
		return "\u00a7r";
	}

	public static String newLine() {
		return "\n";
	}

	public static Color skyRainbow(int var2, float bright, float st, double speed) {
		double v1 = Math.ceil(System.currentTimeMillis() / speed + var2 * 109L) / 5;
		return Color.getHSBColor((float) ((v1 %= 360.0) / 360.0 < 0.5 ? -v1 / 360.0 : v1 / 360.0), st, bright);
	}

	public static Color reAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public static Color reAlpha(Color color, float alpha) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
	}

	public static Color healthColor(float hp, float maxHP) {
		int alpha = 255;
		int pct = (int) ((hp / maxHP) * 255F);
		return new Color(Math.max(Math.min(255 - pct, 255), 0), Math.max(Math.min(pct, 255), 0), 0, alpha);
	}

	public static Color darker(Color color, float percentage) {
		return new Color((int) (color.getRed() * percentage), (int) (color.getGreen() * percentage), (int) (color.getBlue() * percentage), (int) (color.getAlpha() * percentage));
	}

	public static Color hslRainbow(
			int index
			) {
		float lowest = 1f;
		float bigest = 1f;
		int indexOffset = 300;
		int timeSplit = 7000;
		float saturation = 1f;
		float brightness = 1f;
		return Color.getHSBColor((Math.abs((((((int)(System.currentTimeMillis() - startTime)) + index * indexOffset) / (float)timeSplit) % 2) - 1) * (bigest - lowest)) + lowest, saturation, brightness);
	}

	public static Color hslRainbow(
			int index, int indexOffset
			) {
		float lowest = 1f;
		float bigest = 1f;
		int timeSplit = 7000;
		float saturation = 1f;
		float brightness = 1f;
		return Color.getHSBColor((Math.abs((((((int)(System.currentTimeMillis() - startTime)) + index * indexOffset) / (float)timeSplit) % 2) - 1) * (bigest - lowest)) + lowest, saturation, brightness);
	}

	public static Color rainbow() {
		return hslRainbow(1);
	}

	public static Color rainbow(int index) {
		return hslRainbow(index);
	}

	public static Color rainbow(float alpha) {return reAlpha(hslRainbow(1), alpha);}

	public static Color rainbowWithAlpha(int alpha) {return reAlpha(hslRainbow(1), alpha);}

	public static Color rainbow(int index, int alpha) {return reAlpha(hslRainbow(index), alpha);}

	public static Color rainbow(int index, float alpha) {return reAlpha(hslRainbow(index), alpha);}

}

