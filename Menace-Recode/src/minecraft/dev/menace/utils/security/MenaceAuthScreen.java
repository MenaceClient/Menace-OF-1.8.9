package dev.menace.utils.security;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import dev.menace.Menace;
import dev.menace.utils.render.GLSLShader;
import dev.menace.utils.render.MenaceFontRenderer;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class MenaceAuthScreen
extends GuiScreen {
	private final GuiScreen previousScreen;
	private GuiTextField username;
	private String status = "§7Waiting...";
	private GLSLShader backgroundShader;
	private long initTime = System.currentTimeMillis();
	MenaceFontRenderer text = MenaceFontRenderer.getFontOnPC("Arial", 35);

	public MenaceAuthScreen(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
		try {
			this.backgroundShader = new GLSLShader("/assets/minecraft/menace/shaders/mainmenu.fsh");
		} catch (IOException var13) {
			throw new IllegalStateException("Failed to load background shader", var13);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.enableAlpha();
		GlStateManager.disableCull();
		ScaledResolution sr = new ScaledResolution(this.mc);
		this.backgroundShader.useShader(sr.getScaledWidth() * sr.getScaleFactor(), sr.getScaledHeight() * sr.getScaleFactor(), (float)mouseX, (float)mouseY, (float)(System.currentTimeMillis() - this.initTime) / 1000.0F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-1.0F, -1.0F);
		GL11.glVertex2f(-1.0F, 1.0F);
		GL11.glVertex2f(1.0F, 1.0F);
		GL11.glVertex2f(1.0F, -1.0F);
		GL11.glEnd();
		GL20.glUseProgram(0);
		this.username.drawTextBox();
		this.text.drawCenteredString("Menace Auth", width / 2, 20, -1);
		this.text.drawCenteredString(status, width / 2, 40, -1);
		if (this.username.getText().isEmpty()) {
			this.drawString(mc.fontRendererObj, "UID", width / 2 - 96, height / 4 + 30, -7829368);
		}
		this.drawExit(mouseX, mouseY);
		this.drawLogin(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawExit(int mouseX, int mouseY) {
		int l1 = height / 4 + 24;
		int w = 200;
		int h = 20;
		int x1 = width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1 + 108;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect((float)x1, (float)y1, (float)x2, (float)y2, 5.0F, hovered ? (new Color(203, 26, 26, 80)).getRGB() : (new Color(0, 0, 0, 80)).getRGB());
		this.text.drawCenteredString("Exit", (float)((x1 + x2) / 2), (float)((y1 + y2 - 22) / 2), Color.black.getRGB());
	}

	private void drawLogin(int mouseX, int mouseY) {
		int l1 = height / 4 + 24;
		int w = 200;
		int h = 20;
		int x1 = width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1 + 78;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect((float)x1, (float)y1, (float)x2, (float)y2, 5.0F, hovered ? (new Color(203, 26, 26, 80)).getRGB() : (new Color(0, 0, 0, 80)).getRGB());
		this.text.drawCenteredString("Login", (float)((x1 + x2) / 2), (float)((y1 + y2 - 22) / 2), Color.black.getRGB());
	}

	@Override
	public void initGui() {
		this.username = new GuiTextField(-1, this.mc.fontRendererObj, width / 2 - 100, height / 4 + 24, 200, 20);
		this.username.setFocused(true);
		this.initTime = System.currentTimeMillis();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(char character, int key) {
		try {
			super.keyTyped(character, key);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if (key == Keyboard.KEY_TAB) {
			if (!this.username.isFocused()) {
				this.username.setFocused(true);
			}
		} else if (key == Keyboard.KEY_RETURN) {
			login();
		}
		this.username.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int l1 = height / 4 + 24;
		this.username.mouseClicked(mouseX, mouseY, mouseButton);
		if (RenderUtils.hover(width / 2 - 100, l1 + 108, mouseX, mouseY, 200, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.shutdown();
		}
		if (RenderUtils.hover(width / 2 - 100, l1 + 78, mouseX, mouseY, 200, 20) && mouseButton == 0) {
			login();
		}
	}

	private void login() {
		this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		status = "§eLogging in...";

		if (username.getText() == null) {
			status = "§cPlease type in your UID.";
			return;
		}

		try {
			Integer.valueOf(username.getText());
		}
		catch (NumberFormatException e) {
			status = "§c" + username.getText() + " is not a number dumbass.";
			return;
		}

		try {
			URL url = new URL("http://menaceapi.cf/HWIDS.txt");
			URLConnection uc = url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
			bufferedReader.close();
		} catch (IOException e) {
			status = "§cConnection Failed, could not connect to the Menace Servers.";
			e.printStackTrace();
			return;
		}

		if (!HWIDManager.readHWIDURL().contains(HWIDManager.getHWID())) {
			status = "§cHWID not whitelisted.";
			return;
		}

		if (HWIDManager.getUID(HWIDManager.getHWID()) != Integer.parseInt(username.getText())) {
			status = "§cInvalid UID.";
			return;
		}

		Menace.instance.user = HWIDManager.getUser();
		Minecraft.getMinecraft().displayGuiScreen(previousScreen);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
}

