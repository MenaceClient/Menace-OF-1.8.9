package dev.menace.utils.render;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class ColorUtils {

	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}

	public static @NotNull Color fade(float speed, float off) {
		double time = (double) System.currentTimeMillis() / speed;
		time += off;
		time %= 255.0f;
		return Color.getHSBColor((float) (time / 255.0f), 1.0f, 1.0f);
	}

}
