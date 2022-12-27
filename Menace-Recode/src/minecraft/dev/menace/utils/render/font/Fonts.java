package dev.menace.utils.render.font;

import net.minecraft.util.*;
import java.awt.*;
import net.minecraft.client.*;

public class Fonts
{
    public static Font fontFromTTF(final ResourceLocation fontLocation, final float fontSize, final int fontType) {
        Font output = null;
        try {
            output = Font.createFont(Font.TRUETYPE_FONT, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontType, fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}