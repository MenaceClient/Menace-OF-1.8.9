package dev.menace.utils.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

	private static final Map<Integer, Boolean> glCapMap = new HashMap<>();

	private static final Minecraft MC = Minecraft.getMinecraft();

	public static void drawHorizontalGradient(double left, double top, double right, double bottom, int col1, int col2) {
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_LINE_SMOOTH);
		glShadeModel(GL_SMOOTH);

		drawHorizontalGradient(left, top, right, bottom, col1, col2);

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_LINE_SMOOTH);
		glShadeModel(GL_FLAT);
	}

	public static void quickDrawHorizontalGradient(double left, double top, double right, double bottom, int col1, int col2) {
		glBegin(GL_QUADS);

		glColor(col1);
		glVertex2d(left, top);
		glVertex2d(left, bottom);
		glColor(col2);
		glVertex2d(right, bottom);
		glVertex2d(right, top);

		glEnd();
	}

	public static void drawVerticalGradient(double left, double top, double right, double bottom, int col1, int col2) {
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_LINE_SMOOTH);
		glShadeModel(GL_SMOOTH);

		quickDrawVerticalGradient(left, top, right, bottom, col1, col2);

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_LINE_SMOOTH);
		glShadeModel(GL_FLAT);
	}

	public static void quickDrawVerticalGradient(double left, double top, double right, double bottom, int col1, int col2) {
		glBegin(GL_QUADS);

		glColor(col1);
		glVertex2d(right, top);
		glVertex2d(left, top);
		glColor(col2);
		glVertex2d(left, bottom);
		glVertex2d(right, bottom);

		glEnd();
	}

	public static void drawBlock(BlockPos blockPos, Color color, float thickness) {
		double x = (double) blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y = (double) blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z = (double) blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(770, 771);
		glEnable(3042);
		glLineWidth(thickness);
		glEnable(GL_LINE_SMOOTH);
		glDisable(3553);
		glDisable(2929);
		glDepthMask(false);
		glColor4d(color.getRed(), color.getGreen(), color.getBlue(), 13);
		glColor4d(color.getRed(), color.getGreen(), color.getBlue(), 1);
		drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		glEnable(3553);
		glEnable(2929);
		glDepthMask(true);
	}

	public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawRect(int left, int top, int right, int bottom, int color) {
		// default
		Gui.drawRect(left, top, right, bottom, color);

		// replacement
		//		LWJGLUtil.drawRect(left, top, right - left, bottom - top, color);
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		// default
		Gui.drawRect(left, top, right, bottom, color);

		// replacement
		//		LWJGLUtil.drawRect(left, top, right - left, bottom - top, color);
	}

	public static void drawHorizontalLine(int startX, int endX, int y, int color) {
		if (endX < startX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		drawRect(startX, y, endX + 1, y + 1, color);
	}
	public static void drawVerticalLine(int x, int startY, int endY, int color) {
		if (endY < startY) {
			int i = startY;
			startY = endY;
			endY = i;
		}

		drawRect(x, startY + 1, x + 1, endY, color);
	}

	public static void originalRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		float z = 0;
		if (paramXStart > paramXEnd) {
			z = paramXStart;
			paramXStart = paramXEnd;
			paramXEnd = z;
		}

		if (paramYStart > paramYEnd) {
			z = paramYStart;
			paramYStart = paramYEnd;
			paramYEnd = z;
		}

		double x1 = paramXStart + radius;
		double y1 = paramYStart + radius;
		double x2 = paramXEnd - radius;
		double y2 = paramYEnd - radius;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		worldrenderer.begin(GL_POLYGON, DefaultVertexFormats.POSITION);

		double degree = Math.PI / 180;
		for (double i = 0; i <= 90; i += 1)
			worldrenderer.pos(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius, 0.0D).endVertex();
		for (double i = 90; i <= 180; i += 1)
			worldrenderer.pos(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius, 0.0D).endVertex();
		for (double i = 180; i <= 270; i += 1)
			worldrenderer.pos(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius, 0.0D).endVertex();
		for (double i = 270; i <= 360; i += 1)
			worldrenderer.pos(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius, 0.0D).endVertex();

		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		float z = 0;
		if (paramXStart > paramXEnd) {
			z = paramXStart;
			paramXStart = paramXEnd;
			paramXEnd = z;
		}

		if (paramYStart > paramYEnd) {
			z = paramYStart;
			paramYStart = paramYEnd;
			paramYEnd = z;
		}

		double x1 = paramXStart + radius;
		double y1 = paramYStart + radius;
		double x2 = paramXEnd - radius;
		double y2 = (double)(paramYEnd - radius);

		glPushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_LINE_SMOOTH);
		glLineWidth(1);

		glColor4f(red, green, blue, alpha);
		glBegin(GL_POLYGON);

		double degree = Math.PI / 180;
		for (double i = 0; i <= 90; i += 1)
			glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
		for (double i = 90; i <= 180; i += 1)
			glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
		for (double i = 180; i <= 270; i += 1)
			glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
		for (double i = 270; i <= 360; i += 1)
			glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_LINE_SMOOTH);
		glPopMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	// rTL = radius top left, rTR = radius top right, rBR = radius bottom right, rBL = radius bottom left
	public static void customRounded(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float rTL, float rTR, float rBR, float rBL, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		float z = 0;
		if (paramXStart > paramXEnd) {
			z = paramXStart;
			paramXStart = paramXEnd;
			paramXEnd = z;
		}

		if (paramYStart > paramYEnd) {
			z = paramYStart;
			paramYStart = paramYEnd;
			paramYEnd = z;
		}

		double xTL = paramXStart + rTL;
		double yTL = paramYStart + rTL;

		double xTR = paramXEnd - rTR;
		double yTR = paramYStart + rTR;

		double xBR = paramXEnd - rBR;
		double yBR = paramYEnd - rBR;

		double xBL = paramXStart + rBL;
		double yBL = paramYEnd - rBL;

		glPushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_LINE_SMOOTH);
		glLineWidth(1);

		glColor4f(red, green, blue, alpha);
		glBegin(GL_POLYGON);

		double degree = Math.PI / 180;
		for (double i = 0; i <= 90; i += 1)
			glVertex2d(xBR + Math.sin(i * degree) * rBR, yBR + Math.cos(i * degree) * rBR);
		for (double i = 90; i <= 180; i += 1)
			glVertex2d(xTR + Math.sin(i * degree) * rTR, yTR + Math.cos(i * degree) * rTR);
		for (double i = 180; i <= 270; i += 1)
			glVertex2d(xTL + Math.sin(i * degree) * rTL, yTL + Math.cos(i * degree) * rTL);
		for (double i = 270; i <= 360; i += 1)
			glVertex2d(xBL + Math.sin(i * degree) * rBL, yBL + Math.cos(i * degree) * rBL);
		glEnd();

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_LINE_SMOOTH);
		glPopMatrix();
	}

	public static void fastRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius) {
		float z = 0;
		if (paramXStart > paramXEnd) {
			z = paramXStart;
			paramXStart = paramXEnd;
			paramXEnd = z;
		}

		if (paramYStart > paramYEnd) {
			z = paramYStart;
			paramYStart = paramYEnd;
			paramYEnd = z;
		}

		double x1 = (double)(paramXStart + radius);
		double y1 = (double)(paramYStart + radius);
		double x2 = (double)(paramXEnd - radius);
		double y2 = (double)(paramYEnd - radius);

		glEnable(GL_LINE_SMOOTH);
		glLineWidth(1);

		glBegin(GL_POLYGON);

		double degree = Math.PI / 180;
		for (double i = 0; i <= 90; i += 1)
			glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
		for (double i = 90; i <= 180; i += 1)
			glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
		for (double i = 180; i <= 270; i += 1)
			glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
		for (double i = 270; i <= 360; i += 1)
			glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
		glEnd();
		glDisable(GL_LINE_SMOOTH);
	}

	public static void renderTag(String name, double pX, double pY, double pZ, int color) {
		float scale = (float) (MC.thePlayer.getDistance(pX + MC.getRenderManager().renderPosX, pY + MC.getRenderManager().renderPosY, pZ + MC.getRenderManager().renderPosZ) / 8.0D);
		if (scale < 1.6F) {
			scale = 1.6F;
		}
		scale /= 50;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-MC.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(MC.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		int width = MC.fontRendererObj.getStringWidth(name) / 2;
		GL11.glPushMatrix();
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		Gui.drawRect(-width - 1, -(MC.fontRendererObj.FONT_HEIGHT + 8), (-width - 1) + 2 + width * 2, -(MC.fontRendererObj.FONT_HEIGHT - 1), 0);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		MC.fontRendererObj.drawStringWithShadow(name, -(width), -(MC.fontRendererObj.FONT_HEIGHT + 7), color);
		GL11.glScalef(1f, 1f, 1f);
		GlStateManager.enableTexture2D();
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public static void drawEntityBox(final Entity entity, final Color color, final boolean outline) {
		final RenderManager renderManager = MC.getRenderManager();
		final Timer timer = MC.timer;

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		enableGlCap(GL_BLEND);
		disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
		glDepthMask(false);

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
				- renderManager.renderPosX;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
				- renderManager.renderPosY;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
				- renderManager.renderPosZ;

		final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
		final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
				entityBox.minX - entity.posX + x - 0.05D,
				entityBox.minY - entity.posY + y,
				entityBox.minZ - entity.posZ + z - 0.05D,
				entityBox.maxX - entity.posX + x + 0.05D,
				entityBox.maxY - entity.posY + y + 0.15D,
				entityBox.maxZ - entity.posZ + z + 0.05D
		);

		if (outline) {
			glLineWidth(1F);
			enableGlCap(GL_LINE_SMOOTH);
			glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
			drawSelectionBoundingBox(axisAlignedBB);
		}

		glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
		drawFilledBox(axisAlignedBB);
		GlStateManager.resetColor();
		glDepthMask(true);
		resetCaps();
	}

	public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
		worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawImage(float x, float y, final int width, final int height, final ResourceLocation image, Color color) {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0.0f, 0.0f, width, height, (float) width, (float) height);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static void circle(final Entity entity, final double rad, Color color) {
		GL11.glPushMatrix();
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glEnable(2832);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glHint(3153, 4354);
		GL11.glDepthMask(false);
		GlStateManager.disableCull();
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosX;
		final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosY) + 0.01;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosZ;

		for (int i = 0; i <= 90; ++i) {
			GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 0.5);

			GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
		}

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		GlStateManager.enableCull();
		GL11.glDisable(2848);
		GL11.glDisable(2848);
		GL11.glEnable(2832);
		GL11.glEnable(3553);
		GL11.glPopMatrix();
		GL11.glColor3f(255, 255, 255);
	}

	public static void drawCircle(double x, double y, double radius, int c) {
		GL11.glEnable(GL_MULTISAMPLE);
		GL11.glEnable(GL_POLYGON_SMOOTH);
		float alpha = (float) (c >> 24 & 255) / 255.0f;
		float red = (float) (c >> 16 & 255) / 255.0f;
		float green = (float) (c >> 8 & 255) / 255.0f;
		float blue = (float) (c & 255) / 255.0f;
		boolean blend = GL11.glIsEnabled((int) 3042);
		boolean line = GL11.glIsEnabled((int) 2848);
		boolean texture = GL11.glIsEnabled((int) 3553);
		if (!blend) {
			GL11.glEnable(3042);
		}
		if (!line) {
			GL11.glEnable(2848);
		}
		if (texture) {
			GL11.glDisable(3553);
		}
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(9);
		int i = 0;
		while (i <= 360) {
			GL11.glVertex2d(
					x + Math.sin((double) i * 3.141526 / 180.0) * radius,
					y + Math.cos((double) i * 3.141526 / 180.0) * radius);
			++i;
		}
		GL11.glEnd();
		if (texture) {
			GL11.glEnable((int) 3553);
		}
		if (!line) {
			GL11.glDisable((int) 2848);
		}
		if (!blend) {
			GL11.glDisable((int) 3042);
		}
		GL11.glDisable(GL_POLYGON_SMOOTH);
		GL11.glClear(0);
	}

	public static void draw2DImage(ResourceLocation image, float x, float y, int width, int height, Color c) {

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(c.getRed() / 255f, c.getGreen()/255f, c.getBlue() / 255f, c.getAlpha());
		MC.getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0.0f, 0.0f, width, height, width, height);

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		MC.getTextureManager().bindTexture(skin);
		RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height, 64F, 64F);
		RenderUtils.drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height, 64F, 64F);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
		MC.getTextureManager().bindTexture(skin);
		RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
				64F, 64F);
		RenderUtils.drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height,
				64F, 64F);
	}

	public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + (float) vHeight) * f1).endVertex();
		worldrenderer.pos(x + width, y + height, 0.0D).tex((u + (float) uWidth) * f, (v + (float) vHeight) * f1).endVertex();
		worldrenderer.pos(x + width, y, 0.0D).tex((u + (float) uWidth) * f, v * f1).endVertex();
		worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	public static void resetCaps() {
		glCapMap.forEach(RenderUtils::setGlState);
	}

	public static void enableGlCap(final int cap) {
		setGlCap(cap, true);
	}

	public static void enableGlCap(final int ... caps) {
		for (final int cap : caps)
			setGlCap(cap, true);
	}

	public static void disableGlCap(final int cap) {
		setGlCap(cap, true);
	}

	public static void disableGlCap(final int... caps) {
		for (final int cap : caps)
			setGlCap(cap, false);
	}

	public static void setGlCap(final int cap, final boolean state) {
		glCapMap.put(cap, glGetBoolean(cap));
		setGlState(cap, state);
	}

	public static void setGlState(final int cap, final boolean state) {
		if (state)
			glEnable(cap);
		else
			glDisable(cap);
	}

	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void lineWidth(float width) {
		GL11.glLineWidth(width);
	}

	public static void glEnd() {
		GL11.glEnd();
	}

	public static void glBegin(int mode) {
		GL11.glBegin(mode);
	}

	public static void color4f(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void putVertex3d(double x, double y, double z) {
		GL11.glVertex3d(x, y, z);
	}

	public static void putVertex3d(Vec3 vec) {
		GL11.glVertex3d(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public static Vec3 getRenderPos(double x, double y, double z) {

		x = x - MC.getRenderManager().renderPosX;
		y = y - MC.getRenderManager().renderPosY;
		z = z - MC.getRenderManager().renderPosZ;

		return new Vec3(x, y, z);
	}

	/**
	 * Checks if mouse is over rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width RELATIVE to x.
	 * @param height RELATIVE to y.
	 * @param mouseX
	 * @param mouseY
	 * @return true if rectangle is hovered
	 */
	public static boolean hover(int x, int y, int mouseX, int mouseY, int width, int height) {
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
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
}
