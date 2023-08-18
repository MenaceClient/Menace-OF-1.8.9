package dev.menace.ui.custom;

import dev.menace.utils.render.font.Fonts;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MenaceSplashScreen {

    private static final int MAX_ACTIONS = 11;
    private static int PROGRESS = 0;
    private static String currentAction = "Init";
    private static final ResourceLocation splashScreen = new ResourceLocation("menace/splash.png");
    private static final MenaceFontRenderer fontRenderer = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/SF-Pro.ttf"), 20, Font.PLAIN));

    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }

        TextureManager tm = Minecraft.getMinecraft().getTextureManager();
        drawSplashScreen(tm);
    }

    public static void setProgress(int progress, String action) {
        PROGRESS = progress;
        currentAction = action;
        update();
    }

    public static void drawSplashScreen(TextureManager tm) {
        GlStateManager.pushAttrib();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = sr.getScaleFactor();

        Framebuffer fb = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
        fb.bindFramebuffer(false);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        tm.bindTexture(splashScreen);

        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, 1920, 1080, sr.getScaledWidth(), sr.getScaledHeight(), 1920, 1080);
        drawProgress();

        fb.unbindFramebuffer();
        fb.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        Minecraft.getMinecraft().updateDisplay();

        GlStateManager.popAttrib();

    }

    private static void drawProgress() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        double nProgress = PROGRESS;
        double calc = nProgress / MAX_ACTIONS * sr.getScaledWidth();

        Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());

        GlStateManager.resetColor();
        resetTextureState();

        fontRenderer.drawString(currentAction, 20, sr.getScaledHeight() - 25, 0xFFFFFFFF);

        String progress = PROGRESS + "/" + MAX_ACTIONS;
        fontRenderer.drawString(progress, sr.getScaledWidth() - fontRenderer.getStringWidth(progress) - 20, sr.getScaledHeight() - 25, 0xe1e1e1FF);

        GlStateManager.resetColor();
        resetTextureState();

        Gui.drawRect(0, sr.getScaledHeight() - 2, calc, sr.getScaledHeight(), new Color(255, 255, 255).getRGB());

        Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());

    }

    private static void resetTextureState() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }

}
