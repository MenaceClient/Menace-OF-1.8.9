package dev.menace.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class ColorUtils {

	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}

	public static Color fade(float speed, float off) {
		double time = (double) System.currentTimeMillis() / speed;
		time += off;
		time %= 255.0f;
		return Color.getHSBColor((float) (time / 255.0f), 1.0f, 1.0f);
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
		return new Color((color.getRed() * percentage), (color.getGreen() * percentage), (color.getBlue() * percentage), (color.getAlpha() * percentage));
	}

	public static Color brighter(Color color, float percentage) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, percentage);
	}

	public static Color setAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

}
