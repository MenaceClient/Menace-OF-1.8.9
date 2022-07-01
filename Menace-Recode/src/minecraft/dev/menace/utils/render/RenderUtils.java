package dev.menace.utils.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class RenderUtils {

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

	public static void drawBoundingBox(final AxisAlignedBB aa, Color color) {
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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
		worldRenderer.color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha());
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

	public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
		drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
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

	public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color, boolean popPush) {
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

		double x1 = (double)(paramXStart + radius);
		double y1 = (double)(paramYStart + radius);
		double x2 = (double)(paramXEnd - radius);
		double y2 = (double)(paramYEnd - radius);

		if (popPush) glPushMatrix();
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
		if (popPush) glPopMatrix();
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

	public static Color fade(float speed, float off) {

		double time = (double) System.currentTimeMillis() / speed;
		time += off;
		time %= 255.0f;
		return Color.getHSBColor((float) (time / 255.0f), 1.0f, 1.0f);
	}
}
