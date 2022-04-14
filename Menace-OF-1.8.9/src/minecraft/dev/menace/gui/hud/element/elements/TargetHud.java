package dev.menace.gui.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import dev.menace.gui.hud.HUDConfigScreen;
import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.utils.entity.EntityUtils;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ColorUtils;
import dev.menace.utils.misc.RandomUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;

public class TargetHud extends ElementDraggable {

	AbstractClientPlayer target;
	int width;
	private float easingHP = 0f;
	DecimalFormat decimalFormat = new DecimalFormat("0.0");

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return 34;
	}

	@Override
	public void render(ScreenPosition pos) {
		if (!(Menace.instance.moduleManager.killAuraModule.attacked instanceof AbstractClientPlayer)) return;
		target = (AbstractClientPlayer) Menace.instance.moduleManager.killAuraModule.attacked;
		//target = MC.thePlayer;
		if (target == null) return;

		float health = Math.round(EntityUtils.getHealth(target));

		width = target.getName().length() + 80;

		easingHP = target.getHealth();
		
		if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Flux")) {
			drawFlux(health);
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Astolfo")) {
			drawAstolfo();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Novoline")) {
			drawNovo();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Zamorozka")) {
			drawZamorozka();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Arris")) {
			drawArris();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Tenacity")) {
			drawTenacity();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Rise")) {
			drawRise();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Menace")) {
			drawMenace(health);
		}

	}

	@Override
	public void renderDummy(ScreenPosition pos) {
		if (MC.currentScreen instanceof HUDConfigScreen) target = MC.thePlayer;
		if (target == null) return;

		float health = Math.round(EntityUtils.getHealth(target));

		width = target.getName().length() + 80;

		easingHP = target.getHealth();

		if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Flux")) {
			drawFlux(health);
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Astolfo")) {
			drawAstolfo();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Novoline")) {
			drawNovo();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Zamorozka")) {
			drawZamorozka();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Arris")) {
			drawArris();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Tenacity")) {
			drawTenacity();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Rise")) {
			drawRise();
		} else if (Menace.instance.moduleManager.hudModule.targetHudMode.getString().equalsIgnoreCase("Menace")) {
			drawMenace(health);
		}
	}

	public void drawFlux(float health) {
		// draw background
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + width, pos.getAbsoluteY() + 34F, new Color(40, 40, 40).getRGB());

		// draw bars
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 20f, 2 + (health / target.getMaxHealth()) * (pos.getAbsoluteX() + width - 4), pos.getAbsoluteY() + 24f, new Color(231, 182, 0).getRGB());
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 20f, 2 + (EntityUtils.getHealth(target) / target.getMaxHealth()) * (pos.getAbsoluteX() + width - 4), pos.getAbsoluteY() + 24F, new Color(0, 224, 84).getRGB());
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 28F, 2 + (target.getTotalArmorValue() / 20F) * (pos.getAbsoluteX() + width - 4), pos.getAbsoluteY() + 30F, new Color(77, 128, 255).getRGB());


		// draw text
		MC.fontRendererObj.drawString(target.getName(), pos.getAbsoluteX() + 20, pos.getAbsoluteY() + 1, Color.WHITE.getRGB());
		GL11.glPushMatrix();
		GL11.glScaled(0.7, 0.7, 0.7);
		MC.fontRendererObj.drawString("Health: " + health, (pos.getAbsoluteX() + 22) / 0.7, (pos.getAbsoluteY() + 4 + MC.fontRendererObj.FONT_HEIGHT) / 0.7, Color.WHITE.getRGB());
		GL11.glPopMatrix();

		// Draw head
		RenderUtils.drawHead(target.getLocationSkin(), pos.getAbsoluteX(), pos.getAbsoluteY(), 16, 16);
	}

	public void drawAstolfo() {
		Color color = ColorUtils.skyRainbow(1, 1F, 0.9F, 5.0);
		float hpPct = easingHP / target.getMaxHealth();

		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 140F, pos.getAbsoluteY() + 60F, new Color(0, 0, 0, 110).getRGB());

		// health rect
		RenderUtils.drawRect(pos.getAbsoluteX() + 3F, pos.getAbsoluteY() + 55F, pos.getAbsoluteX() + 137F, pos.getAbsoluteY() + 58F, ColorUtils.reAlpha(color, 100).getRGB());
		RenderUtils.drawRect(pos.getAbsoluteX() + 3F, pos.getAbsoluteY() + 55F, pos.getAbsoluteX() + 3 + (hpPct * 134F), pos.getAbsoluteY() + 58F, color.getRGB());
		GL11.glColor4f(1f, 1f, 1f, 1f);
		RenderUtils.drawEntityOnScreen(pos.getAbsoluteX() + 18, pos.getAbsoluteY() + 46, 20, target);

		font.drawStringWithShadow(target.getName(), pos.getAbsoluteX() + 37F, pos.getAbsoluteY() + 6F, -1);
		GL11.glPushMatrix();
		font.drawString(String.valueOf(target.getHealth()) + " ❤", pos.getAbsoluteX() + 40, pos.getAbsoluteY() + 25, color.getRGB());
		GL11.glPopMatrix();
	}

	public void drawNovo() {
		Color color = ColorUtils.healthColor(target.getHealth(), target.getMaxHealth());
		Color darkColor = ColorUtils.darker(color, 0.6F);
		float hpPos = 33F + (Math.round(target.getHealth() / target.getMaxHealth() * 10000) / 100);

		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 140F, pos.getAbsoluteY() + 40F, new Color(40, 40, 40).getRGB());
		font.drawString(target.getName(), pos.getAbsoluteX() + 33, pos.getAbsoluteY() + 5, Color.WHITE.getRGB());
		RenderUtils.drawEntityOnScreen(pos.getAbsoluteX() + 20, pos.getAbsoluteY() + 35, 15, target);
		RenderUtils.drawRect(pos.getAbsoluteX() + hpPos, pos.getAbsoluteY() + 18F, pos.getAbsoluteX() + 33F + (Math.round(easingHP / target.getMaxHealth() * 10000) / 100), pos.getAbsoluteY() + 25F, darkColor);
		RenderUtils.drawRect(pos.getAbsoluteX() + 33F, pos.getAbsoluteY() + 18F, pos.getAbsoluteX() + hpPos, pos.getAbsoluteY() + 25F, color);
		font.drawString("❤", pos.getAbsoluteX() + 33, pos.getAbsoluteY() + 30, Color.RED.getRGB());
		font.drawString(decimalFormat.format(target.getHealth()), pos.getAbsoluteX() + 43, pos.getAbsoluteY() + 30, Color.WHITE.getRGB());
	}

	public void drawZamorozka() {
		// Frame
		RenderUtils.drawCircleRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 150f, pos.getAbsoluteY() + 55f, 5f, new Color(0, 0, 0, 70).getRGB());
		RenderUtils.drawRect(pos.getAbsoluteX() + 7f, pos.getAbsoluteY() + 7f, pos.getAbsoluteX() + 35f, pos.getAbsoluteY() + 40f, new Color(0, 0, 0, 70).getRGB());
		GL11.glColor4f(1f, 1f, 1f, 1f);
		RenderUtils.drawEntityOnScreen(pos.getAbsoluteX() + 21, pos.getAbsoluteY() + 38, 15, target);

		// Healthbar
		float barLength = 143 - 7f;
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 7f, pos.getAbsoluteY() + 45f, pos.getAbsoluteX() + 143f, pos.getAbsoluteY() + 50f, 2.5f, new Color(0, 0, 0, 70).getRGB());
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 7f, pos.getAbsoluteY() + 45f, pos.getAbsoluteX() + 7 + ((easingHP / target.getMaxHealth()) * barLength), pos.getAbsoluteY() + 50f, 2.5f, ColorUtils.rainbowWithAlpha(90).getRGB());
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 7f, pos.getAbsoluteY() + 45f, pos.getAbsoluteX() + 7 + ((target.getHealth() / target.getMaxHealth()) * barLength), pos.getAbsoluteY() + 50f, 2.5f, ColorUtils.rainbow().getRGB());

		// Info
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 43f, pos.getAbsoluteY() + 15f - font.FONT_HEIGHT, pos.getAbsoluteX() + 143f, pos.getAbsoluteY() + 17f, (font.FONT_HEIGHT + 1) * 0.45f, new Color(0, 0, 0, 70).getRGB());
		font.drawCenteredString(target.getName(), pos.getAbsoluteX() + 93f, pos.getAbsoluteY() + 17f - font.FONT_HEIGHT, ColorUtils.rainbow().getRGB());
		font.drawString("Health: " + decimalFormat.format(easingHP) + " §7/ " + decimalFormat.format(target.getMaxHealth()), pos.getAbsoluteX() + 43, pos.getAbsoluteY() + 11 + font.FONT_HEIGHT, Color.WHITE.getRGB());
		font.drawString("Distance: " + decimalFormat.format(MC.thePlayer.getDistanceToEntity(target)), pos.getAbsoluteX() + 43, pos.getAbsoluteY() + 11 + font.FONT_HEIGHT * 2, Color.WHITE.getRGB());
	}

	private void drawArris() {

		String hp = decimalFormat.format(easingHP);
		int additionalWidth = font.getStringWidth(target.getName() + decimalFormat.format(target.getHealth()) + "   hp");
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 45f + additionalWidth, pos.getAbsoluteY() + 1f, ColorUtils.rainbow());
		RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + 1f, pos.getAbsoluteX() + 45f + additionalWidth, pos.getAbsoluteY() + 40f, new Color(0, 0, 0, 110).getRGB());

		RenderUtils.quickDrawHead(target.getLocationSkin(), pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 5, 30, 30);

		// info text
		font.drawString(target.getName(), pos.getAbsoluteX() + 40, pos.getAbsoluteY() + 5, Color.WHITE.getRGB());
		font.drawString(decimalFormat.format(target.getHealth()) + " hp", pos.getAbsoluteX() + 40 + additionalWidth - font.getStringWidth(decimalFormat.format(target.getHealth()) + " hp"), pos.getAbsoluteY() + 5, Color.LIGHT_GRAY.getRGB());

		// hp bar
		float yPos = 5 + font.FONT_HEIGHT + 3f;
		RenderUtils.drawRect(pos.getAbsoluteX() + 40f, pos.getAbsoluteY() + yPos, pos.getAbsoluteX() + 40 + (easingHP / target.getMaxHealth()) * additionalWidth, pos.getAbsoluteY() + yPos + 4, Color.GREEN.getRGB());
		RenderUtils.drawRect(pos.getAbsoluteX() + 40f, pos.getAbsoluteY() + yPos + 9, pos.getAbsoluteX() + 40 + (target.getTotalArmorValue() / 20F) * additionalWidth, pos.getAbsoluteY() + yPos + 13, new Color(77, 128, 255).getRGB());
	}

	private void drawTenacity() {

		int additionalWidth = font.getStringWidth(target.getName());
		RenderUtils.drawCircleRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 45f + additionalWidth, pos.getAbsoluteY() + 40f, 7f, new Color(0, 0, 0, 110).getRGB());

		// circle player avatar
		MC.getTextureManager().bindTexture(target.getLocationSkin());
		RenderUtils.drawScaledCustomSizeModalCircle(pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 5, pos.getAbsoluteX() + 8f, pos.getAbsoluteY() + 8f, 8, 8, 30, 30, 64f, 64f);
		RenderUtils.drawScaledCustomSizeModalCircle(pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 5, pos.getAbsoluteX() + 40f, pos.getAbsoluteY() + 8f, 8, 8, 30, 30, 64f, 64f);

		// info text
		font.drawCenteredString(target.getName(), pos.getAbsoluteX() + 40 + (additionalWidth / 2f), pos.getAbsoluteY() + 5f, Color.WHITE.getRGB(), false);
		font.drawString(decimalFormat.format((easingHP / target.getMaxHealth()) * 100) + "%", pos.getAbsoluteX() + (40f + (easingHP / target.getMaxHealth()) * additionalWidth - font.getStringWidth(decimalFormat.format((easingHP / target.getMaxHealth()) * 100) + "%")), pos.getAbsoluteY() + 28f - font.FONT_HEIGHT, Color.WHITE.getRGB(), false);


		// hp bar
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 40f, pos.getAbsoluteY() + 28f, pos.getAbsoluteX() + 40f + additionalWidth, pos.getAbsoluteY() + 33f, 2.5f, new Color(0, 0, 0, 70).getRGB());
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 40f, pos.getAbsoluteY() + 28f, pos.getAbsoluteX() + 40f + (easingHP / target.getMaxHealth()) * additionalWidth, pos.getAbsoluteY() + 33f, 2.5f, ColorUtils.rainbow().getRGB());
	}

	class RiseParticle {

		int colorR;
		int colorG;
		int colorB;
		int alpha;
		long time;
		int x;
		int y;

		public RiseParticle() {
			colorR = RandomUtils.nextInt(0, 255);
			colorG = RandomUtils.nextInt(0, 255);
			colorB = RandomUtils.nextInt(0, 255);
			alpha = RandomUtils.nextInt(150, 255);
			time = System.currentTimeMillis();
			x = RandomUtils.nextInt(-50, 50);
			y = RandomUtils.nextInt(-50, 50);
		} 

	}

	private java.util.ArrayList<RiseParticle> riseParticleList = new java.util.ArrayList<RiseParticle>();

	private void drawRise() {

		RenderUtils.drawCircleRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + 150f, pos.getAbsoluteY() + 50f, 5f, new Color(0, 0, 0, 130).getRGB());

		int hurtPercent = target.hurtTime;
		float scale = 0f;
		if (hurtPercent == 0f) { scale = 1f; } else if (hurtPercent < 0.5f) {
			scale = 1 - (0.2f * hurtPercent * 2);
		} else {
			scale = 0.8f + (0.2f * (hurtPercent - 0.5f) * 2);
		}

		int size = 30;

		GL11.glPushMatrix();
		GL11.glTranslatef(5f, 5f, 0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f);
		GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f);
		RenderUtils.quickDrawHead(target.getLocationSkin(), pos.getAbsoluteX(), pos.getAbsoluteY(), size, size);
		GL11.glPopMatrix();

		font.drawString("Name " + target.getName(), pos.getAbsoluteX() + 40, pos.getAbsoluteY() + 11, Color.WHITE.getRGB());
		font.drawString("Distance " + decimalFormat.format(MC.thePlayer.getDistanceToEntity(target)) + " Hurt " + target.hurtTime, pos.getAbsoluteX() + 40, pos.getAbsoluteY() + 11 + font.FONT_HEIGHT, Color.WHITE.getRGB());

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		int stopPos = (int) (5 + ((135 - font.getStringWidth(decimalFormat.format(target.getMaxHealth()))) * (easingHP / target.getMaxHealth())));
		for (int i = 5; i!=stopPos;  i+=5) {
			int x1 = (i + 5);
			RenderUtils.quickDrawGradientSideways(pos.getAbsoluteX() + i, pos.getAbsoluteY() + 39.0, pos.getAbsoluteX() + x1, pos.getAbsoluteY() + 45.0,
					ColorUtils.hslRainbow(i, 10).getRGB(), ColorUtils.hslRainbow((int) x1, 10).getRGB());
		}
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
		GL11.glColor4f(1f, 1f, 1f, 1f);

		font.drawString(decimalFormat.format(easingHP), pos.getAbsoluteX() + stopPos + 5, pos.getAbsoluteY() + 43 - font.FONT_HEIGHT / 2, Color.WHITE.getRGB());

		if(target.hurtTime >= 9) {
			for(int i=0; i!=20; i++) {
				riseParticleList.add(new RiseParticle());
			}
		}

		long curTime = System.currentTimeMillis();
		riseParticleList.forEach(rp -> {
			if ((curTime - rp.time) > ((20 + 20) * 50)) {
				riseParticleList.remove(rp);
			}
			float movePercent = 0f;
			if((curTime - rp.time) < 20 * 50) {
				movePercent = (curTime - rp.time) / (20 * 50f);
			} else {
				movePercent = 1f;
			}
			float x = (movePercent * rp.x * 0.5f * 1f) + 20;
			float y = (movePercent * rp.y * 0.5f * 1f) + 20;
			float alpha = 0f;
			if((curTime - rp.time) > 20 * 50) {
				alpha = 1f - ((curTime - rp.time - 20 * 50) / (20 * 50f));
			} else {
				alpha = 1f;
			} 
			alpha = alpha * 0.7f;
			RenderUtils.drawCircle(pos.getAbsoluteX() + x, pos.getAbsoluteY() + y, 1f * 2, new Color(rp.colorR, rp.colorG, rp.colorB, (int) (alpha * 255)).getRGB());
		});
	}
	
	private void drawMenace(float health) {
		int length = font.getStringWidth(target.getName());
		//background
		RenderUtils.drawCircleRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + length + 55, pos.getAbsoluteY() + 50, 2, new Color(40, 40, 40).getRGB());
		//Entity
		RenderUtils.drawEntityOnScreen(pos.getAbsoluteX() + 11, pos.getAbsoluteY() + 40, 17, target);
		//target name
		font.drawString(target.getName(), pos.getAbsoluteX() + 25, pos.getAbsoluteY() + 5, -1);
		//hp
		font.drawString(String.valueOf(Math.round(target.getHealth())) + " hp", pos.getAbsoluteX() + length + 27, pos.getAbsoluteY() + 5, Color.GRAY.getRGB());
		//hp bar
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 25f, pos.getAbsoluteY() + 15f, pos.getAbsoluteX() + 40f + length, pos.getAbsoluteY() + 20f, 2.5f, new Color(0, 0, 0, 70).getRGB());
		RenderUtils.drawCircleRect(pos.getAbsoluteX() + 25f, pos.getAbsoluteY() + 15f, pos.getAbsoluteX() + 40f + (easingHP / target.getMaxHealth()) * length, pos.getAbsoluteY() + 20f, 2.5f, ColorUtils.healthColor(health, target.getMaxHealth()).getRGB());

		GL11.glPushMatrix();
		GL11.glScalef(0.7f, 0.7f, 0.7f);
		//XYZ
		font.drawString(String.format("X: %.1f", target.posX) + String.format(" Y: %.1f", target.posY - 1) + String.format(" Z: %.1f", target.posZ), (pos.getAbsoluteX() + 25) / 0.7, (pos.getAbsoluteY() + 25) / 0.7, -1);
		//OnGround
		String onGround = target.onGround ? "On Ground" : "In Air";
		onGround = target.isWet() ? "In water" : onGround;
		font.drawString(onGround, (pos.getAbsoluteX() + 25) / 0.7, (pos.getAbsoluteY() + 33) / 0.7, -1);
		GL11.glPopMatrix();
	}

}
