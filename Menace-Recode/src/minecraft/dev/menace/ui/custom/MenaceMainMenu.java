package dev.menace.ui.custom;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.common.collect.Lists;

import dev.menace.Menace;
import dev.menace.ui.altmanager.DirectLoginScreen;
import dev.menace.utils.render.GLSLShader;
import dev.menace.utils.render.MenaceFontRenderer;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import viamcp.gui.GuiProtocolSelector;

public class MenaceMainMenu extends GuiScreen implements GuiYesNoCallback
{
	private static final AtomicInteger field_175373_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();

	/** Counts the number of screen updates. */
	private float updateCounter;

	/** The splash message. */
	private GuiButton buttonResetDemo;

	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;

	/**
	 * Texture allocated for the current viewport of the main menu's panorama background.
	 */
	private DynamicTexture viewportTexture;
	private boolean field_175375_v = true;

	/**
	 * The Object object utilized as a thread lock when performing non thread-safe operations
	 */
	private final Object threadLock = new Object();

	/** OpenGL graphics card warning. */
	private String openGLWarning1;

	/** OpenGL graphics card warning. */
	private String openGLWarning2;

	/** Link to the Mojang Support about minimum requirements */
	private String openGLWarningLink;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation backgroundTexture;

	/** Minecraft Realms button. */
	private GuiButton realmsButton;

	//Menace Shaders
	private GLSLShader backgroundShader;
	private long initTime = System.currentTimeMillis(); // Initialize as a failsafe
	MenaceFontRenderer title = MenaceFontRenderer.getFontOnPC("Arial", 130);
	MenaceFontRenderer text = MenaceFontRenderer.getFontOnPC("Arial", 35);

	private boolean L;
	private GuiScreen M;
	private GuiButton modButton;
	private GuiScreen modUpdateNotification;

	public MenaceMainMenu()
	{
		this.openGLWarning2 = field_96138_a;
		this.L = false;

		this.updateCounter = RANDOM.nextFloat();
		this.openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
		{
			this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
			this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
			this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}

		try {
			this.backgroundShader = new GLSLShader("/assets/minecraft/menace/shaders/mainmenu.fsh");
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load backgound shader", e);
		}
	}

	private boolean a()
	{
		return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.enumFloat) && this.M != null;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		++this.panoramaTimer;

		if (this.a())
		{
			this.M.updateScreen();
		}

		Menace.instance.discordRP.update("Menace Client - Recode", "In a menu.");
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);

		this.mc.setConnectedToRealms(false);

		initTime = System.currentTimeMillis();

		if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.enumFloat) && !this.L)
		{
			RealmsBridge realmsbridge = new RealmsBridge();
			this.M = realmsbridge.getNotificationScreen(this);
			this.L = true;
		}

		if (this.a())
		{
			this.M.a(this.width, this.height);
			this.M.initGui();
		}
	}


	private void switchToRealms()
	{
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	public void confirmClicked(boolean result, int id)
	{
		if (result && id == 12)
		{
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		}
		else if (id == 13)
		{
			if (result)
			{
				try
				{
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
					oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(this.openGLWarningLink)});
				}
				catch (Throwable throwable)
				{
					logger.error("Couldn\'t open link", throwable);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.enableAlpha();
		GlStateManager.disableCull();
		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = 30;
		int l = -2130706433;
		int i1 = 16777215;
		int j1 = 0;
		int k1 = Integer.MIN_VALUE;
		int l1 = this.height / 4 + 48;

		ScaledResolution sr = new ScaledResolution(mc);
		this.backgroundShader.useShader(sr.getScaledWidth() * sr.getScaleFactor(), sr.getScaledHeight() * sr.getScaleFactor(), mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(-1f, -1f);
		GL11.glVertex2f(-1f, 1f);
		GL11.glVertex2f(1f, 1f);
		GL11.glVertex2f(1f, -1f);

		GL11.glEnd();

		// Unbind shader
		GL20.glUseProgram(0);
		
		//Menace Title
		title.drawCenteredString("Menace", width / 2, 29, Color.black.getRGB());
		
		//Exit
		this.drawExit(mouseX, mouseY);
		
		//Options
		this.drawOptions(mouseX, mouseY);
		
		//Altmanager TODO: make an altmanager
		this.drawAltmanager(mouseX, mouseY);
		
		//SinglePlayer
		this.drawSinglePlayer(mouseX, mouseY);
		
		//MultiPlayer
		this.drawMultiPlayer(mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.a())
		{
			this.M.drawScreen(mouseX, mouseY, partialTicks);
		}

	}
	
	private void drawExit(int mouseX, int mouseY) {
		int l1 = this.height / 4 + 48;
		int w = 98;
		int h = 20;
		int x1 = this.width / 2 + 2;
		int x2 = x1 + 2 + w;
		int y1 = l1 + 80 + 12;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect(x1, y1, x2, y2, 5, hovered ? new Color(203, 26, 26, 80).getRGB() : new Color(0, 0, 0, 80).getRGB());
		text.drawCenteredString("Exit", (x1 + x2) / 2,  (y1 + y2 - 22) / 2, Color.black.getRGB());
	}
	
	private void drawOptions(int mouseX, int mouseY) {
		int l1 = this.height / 4 + 48;
		int w = 98;
		int h = 20;
		int x1 = this.width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1 + 80 + 12;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect(x1, y1, x2, y2, 5, hovered ? new Color(203, 26, 26, 80).getRGB() : new Color(0, 0, 0, 80).getRGB());
		text.drawCenteredString("Settings", (x1 + x2) / 2,  (y1 + y2 - 22) / 2, Color.black.getRGB());
	}
	
	private void drawAltmanager(int mouseX, int mouseY) {
		int l1 = this.height / 4 + 48;
		int w = 200;
		int h = 20;
		int x1 = this.width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1 + 24 * 2;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect(x1, y1, x2, y2, 5, hovered ? new Color(203, 26, 26, 80).getRGB() : new Color(0, 0, 0, 80).getRGB());
		text.drawCenteredString("Login", (x1 + x2) / 2,  (y1 + y2 - 22) / 2, Color.black.getRGB());
	}
	
	public void drawSinglePlayer(int mouseX, int mouseY) {
		int l1 = this.height / 4 + 48;
		int w = 200;
		int h = 20;
		int x1 = this.width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect(x1, y1, x2, y2, 5, hovered ? new Color(203, 26, 26, 80).getRGB() : new Color(0, 0, 0, 80).getRGB());
		text.drawCenteredString("SinglePlayer", (x1 + x2) / 2,  (y1 + y2 - 22) / 2, Color.black.getRGB());
	}
	
	public void drawMultiPlayer(int mouseX, int mouseY) {
		int l1 = this.height / 4 + 48;
		int w = 200;
		int h = 20;
		int x1 = this.width / 2 - 100;
		int x2 = x1 + w;
		int y1 = l1 + 24;
		int y2 = y1 + h;
		boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
		RenderUtils.drawRoundedRect(x1, y1, x2, y2, 5, hovered ? new Color(203, 26, 26, 80).getRGB() : new Color(0, 0, 0, 80).getRGB());
		text.drawCenteredString("MultiPlayer", (x1 + x2) / 2,  (y1 + y2 - 22) / 2, Color.black.getRGB());
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		int l1 = this.height / 4 + 48;
		
		//Exit
		if (RenderUtils.hover(this.width / 2 + 2, l1 + 80 + 12, mouseX, mouseY, 98, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.shutdown();
		}
		
		//Options
		if (RenderUtils.hover(this.width / 2 + 2 - 100, l1 + 80 + 12, mouseX, mouseY, 98, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		
		//AltManager TODO add altmanager
		if (RenderUtils.hover(this.width / 2 - 100, l1 + 24 * 2, mouseX, mouseY, 200, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.displayGuiScreen(new DirectLoginScreen(this));
		}
		
		//SinglePlayer
		if (RenderUtils.hover(this.width / 2 - 100, l1, mouseX, mouseY, 200, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		
		//MultiPlayer
		if (RenderUtils.hover(this.width / 2 - 100, l1 + 24, mouseX, mouseY, 200, 20) && mouseButton == 0) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (this.a())
		{
			this.M.mouseClickedP(mouseX, mouseY, mouseButton);
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		if (this.M != null)
		{
			this.M.onGuiClosed();
		}
	}
}
