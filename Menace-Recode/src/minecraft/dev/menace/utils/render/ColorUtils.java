package dev.menace.utils.render;

import dev.menace.utils.misc.MathUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

public class ColorUtils {

	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}

	public static Color cleanRainbow(float speed, float off) {
		double time = (double) System.currentTimeMillis() / speed;
		time += off;
		time %= 255.0f;
		return Color.getHSBColor((float) (time / 255.0f), 1.0f, 1.0f);
	}

	public static Color fade(Color color, Color color2, int off) {
		double time = (double) (Math.abs((System.currentTimeMillis()) / 10) + off) / 140;
		time %= 1.0; // Normalize time to the range [0, 1]

		// Interpolate between color1 and color2 based on time
		int red = (int) (color.getRed() * (1 - time) + color2.getRed() * time);
		int green = (int) (color.getGreen() * (1 - time) + color2.getGreen() * time);
		int blue = (int) (color.getBlue() * (1 - time) + color2.getBlue() * time);

		return new Color(red, green, blue);
	}

	private static int lerp(int start, int end, long time) {
		double t = (double) time / 1000; // Convert time to seconds
		double percentage = Math.min(t / 1.0, 1.0); // Limit the percentage to [0, 1]
		return (int) (start + percentage * (end - start));
	}

	public static void glColor(final int red, final int green, final int blue, final int alpha) {
		GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
	}

	public static void glColor(final Color color) {
		final float red = color.getRed() / 255F;
		final float green = color.getGreen() / 255F;
		final float blue = color.getBlue() / 255F;
		final float alpha = color.getAlpha() / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(final Color color, final int alpha) {
		glColor(color, alpha/255F);
	}

	public static void glColor(final Color color, final float alpha) {
		final float red = color.getRed() / 255F;
		final float green = color.getGreen() / 255F;
		final float blue = color.getBlue() / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(final int hex) {
		final float alpha = (hex >> 24 & 0xFF) / 255F;
		final float red = (hex >> 16 & 0xFF) / 255F;
		final float green = (hex >> 8 & 0xFF) / 255F;
		final float blue = (hex & 0xFF) / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(final int hex, final int alpha) {
		final float red = (hex >> 16 & 0xFF) / 255F;
		final float green = (hex >> 8 & 0xFF) / 255F;
		final float blue = (hex & 0xFF) / 255F;

		GlStateManager.color(red, green, blue, alpha / 255F);
	}

	public static void glColor(final int hex, final float alpha) {
		final float red = (hex >> 16 & 0xFF) / 255F;
		final float green = (hex >> 8 & 0xFF) / 255F;
		final float blue = (hex & 0xFF) / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static Color darker(Color color, float percentage) {
		//Make the color darker
		//Clamp the value to 0-255
		int red = (int) Math.max(color.getRed() * (1 - percentage), 0);
		int green = (int) Math.max(color.getGreen() * (1 - percentage), 0);
		int blue = (int) Math.max(color.getBlue() * (1 - percentage), 0);

		return new Color(red / 255f, green / 255f, blue / 255f, color.getAlpha() / 255f);
	}

	public static Color brighter(Color color, float percentage) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, percentage);
	}

	public static Color setAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public static Color setAlpha(Color color, float alpha) {
		return new Color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, alpha);
	}

}
