package dev.menace.module.modules.render;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRenderNametag;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NametagsModule extends BaseModule {

    ToggleSetting customFont;
    ToggleSetting onlyPlayers;

    public NametagsModule() {
        super("Nametags", "Enhances nametags", Category.RENDER, 0);
    }

    @Override
    public void setup() {
        customFont = new ToggleSetting("Custom Font", true, false);
        onlyPlayers = new ToggleSetting("Only Players", true, false);
        this.rSetting(customFont);
        this.rSetting(onlyPlayers);
        super.setup();
    }

    @EventTarget
    public void onRenderNametag(EventRenderNametag event) {
        if (onlyPlayers.getValue() && !(event.getEntity() instanceof EntityPlayer)) return;

        event.cancel();

        renderNametag(event.getEntity(), event.getX(), event.getY(), event.getZ());

    }

    public void renderNametag(Entity entity, double x, double y, double z) {
        if (entity == mc.thePlayer || entity == null || entity.isDead) return;

        //GlStateManager.alphaFunc(516, 0.1F);

        final String[] s = {entity.getDisplayName().getFormattedText()};
        Menace.instance.onlineMenaceUsers.forEach((username, ign) -> {
            if (ign != null && s[0].contains(ign)) {
                s[0] = s[0].replace(ign, ign + " §r(§b" + username + "§r)");
            }
        });
        String displayName = s[0];

        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + entity.height + 0.5F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        float distance = mc.thePlayer.getDistanceToEntity(entity);
        float scale = MathHelper.clamp_float(f1 * (distance / 8), f1, f1 * 5);

        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        if (displayName.equals("deadmau5"))
        {
            i = -10;
        }

        float angleDiff = MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
        Color opacity = new Color(255, 255, 255, angleDiff > 90 ? 5 : (int) (255 - (angleDiff / 90) * 255));

        int j = getStringWidth(displayName) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 2, -2 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 2, 9 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 2, 9 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 2, -2 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        drawString(displayName, (double) -getStringWidth(displayName) / 2, i, opacity.getRGB());
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        drawString(displayName, (double) -getStringWidth(displayName) / 2, i, opacity.getRGB());
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

    }

    private void drawString(String str, double x, double y, int color) {
        if (customFont.getValue()) {
            Menace.instance.sfPro.drawString(str, x, y - 1, color);
        } else {
            mc.fontRendererObj.drawString(str, x, y, color);
        }
    }

    private int getStringWidth(String str) {
        if (customFont.getValue()) {
            return Menace.instance.sfPro.getStringWidth(str);
        } else {
            return mc.fontRendererObj.getStringWidth(str);
        }
    }

}
