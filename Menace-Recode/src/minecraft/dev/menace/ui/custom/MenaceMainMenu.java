package dev.menace.ui.custom;

import com.google.common.collect.Lists;
import dev.menace.Menace;
import dev.menace.ui.altmanager.DirectLoginScreen;
import dev.menace.utils.render.GLSLShader;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import dev.menace.utils.render.font.Fonts;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import viamcp.gui.GuiProtocolSelector;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

public class MenaceMainMenu extends GuiScreen implements GuiYesNoCallback {
   private static final AtomicInteger field_175373_f = new AtomicInteger(0);
   private static final Logger logger = LogManager.getLogger();
   private static final Random RANDOM = new Random();
   private float updateCounter;
   private String splashText;
   private GuiButton buttonResetDemo;
   private int panoramaTimer;
   private DynamicTexture viewportTexture;
   private boolean field_175375_v = true;
   private final Object threadLock = new Object();
   private String openGLWarning1;
   private String openGLWarning2;
   private String openGLWarningLink;
   private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
   public static final String field_96138_a;
   private int field_92024_r;
   private int field_92023_s;
   private int field_92022_t;
   private int field_92021_u;
   private int field_92020_v;
   private int field_92019_w;
   private ResourceLocation backgroundTexture;
   private GuiButton realmsButton;
   private final GLSLShader backgroundShader;
   private long initTime = System.currentTimeMillis();
   MenaceFontRenderer text;
   private boolean L;
   private GuiScreen M;
   private GuiButton modButton;
   private GuiScreen modUpdateNotification;

   static {
      field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   }

   public MenaceMainMenu() {
      this.openGLWarning2 = field_96138_a;
      this.L = false;
      this.splashText = "missingno";
      BufferedReader bufferedreader = null;
      text = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/SF-Pro.ttf"), 30, Font.PLAIN), true, true);
      try {
         List<String> list = Lists.newArrayList();
         bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

         String s;
         while((s = bufferedreader.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) {
               list.add(s);
            }
         }

         if (!list.isEmpty()) {
            do {
               this.splashText = list.get(RANDOM.nextInt(list.size()));
            } while(this.splashText.hashCode() == 125780783);
         }
      } catch (IOException ignored) {
      } finally {
         if (bufferedreader != null) {
            try {
               bufferedreader.close();
            } catch (IOException ignored) {
            }
         }

      }

      this.updateCounter = RANDOM.nextFloat();
      this.openGLWarning1 = "";
      if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
         this.openGLWarning1 = I18n.format("title.oldgl1");
         this.openGLWarning2 = I18n.format("title.oldgl2");
         this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

      try {
         this.backgroundShader = new GLSLShader("/assets/minecraft/menace/shaders/redflow.fsh");
      } catch (IOException var13) {
         throw new IllegalStateException("Failed to load backgound shader", var13);
      }
   }

   private boolean a() {
      return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(Options.enumFloat) && this.M != null;
   }

   public void updateScreen() {
      ++this.panoramaTimer;
      if (this.a()) {
         this.M.updateScreen();
      }

      Menace.instance.discordRP.update("In a menu.");
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void initGui() {
      this.viewportTexture = new DynamicTexture(256, 256);
      this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      this.mc.setConnectedToRealms(false);
      this.initTime = System.currentTimeMillis();
      if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(Options.enumFloat) && !this.L) {
         this.L = true;
      }

      if (this.a()) {
         this.M.a(width, height);
         this.M.initGui();
      }

   }

   private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
      this.buttonList.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
      this.buttonList.add(this.buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_, I18n.format("menu.resetdemo")));
      ISaveFormat isaveformat = this.mc.getSaveLoader();
      WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
      if (worldinfo == null) {
         this.buttonResetDemo.enabled = false;
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 11) {
         this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
      }

      if (button.id == 12) {
         ISaveFormat isaveformat = this.mc.getSaveLoader();
         WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
         if (worldinfo != null) {
            GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
            this.mc.displayGuiScreen(guiyesno);
         }
      }

      if (button.id == 69) {
         this.mc.displayGuiScreen(new GuiProtocolSelector(this));
      }

   }

   public void confirmClicked(boolean result, int id) {
      if (result && id == 12) {
         ISaveFormat isaveformat = this.mc.getSaveLoader();
         isaveformat.flushCache();
         isaveformat.deleteWorldDirectory("Demo_World");
         this.mc.displayGuiScreen(this);
      } else if (id == 13) {
         if (result) {
            try {
               Class<?> oclass = Class.forName("java.awt.Desktop");
               Object object = oclass.getMethod("getDesktop").invoke(null);
               oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
            } catch (Throwable var5) {
               logger.error("Couldn't open link", var5);
            }
         }

         this.mc.displayGuiScreen(this);
      }

   }

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

      this.text.drawCenteredString("Menace", (float)(width / 2), (height / 4 - 24), (new Color(199, 7, 7, 255)).getRGB());

      this.drawExit(mouseX, mouseY);
      this.drawOptions(mouseX, mouseY);
      this.drawAltmanager(mouseX, mouseY);
      this.drawSinglePlayer(mouseX, mouseY);
      this.drawMultiPlayer(mouseX, mouseY);
      this.text.drawString("Welcome back, " + Menace.instance.user.getUsername() + " [" + Menace.instance.user.getUID() + "]", 5, 5, Color.white.getRGB());
      this.text.drawString("Discord: " + Menace.instance.user.getDiscord(), 5, 25, Color.white.getRGB());
      super.drawScreen(mouseX, mouseY, partialTicks);
      if (this.a()) {
         this.M.drawScreen(mouseX, mouseY, partialTicks);
      }

   }

   private void drawExit(int mouseX, int mouseY) {
      int l1 = height / 4 + 48;
      int w = 98;
      int h = 20;
      int x1 = width / 2 + 2;
      int x2 = x1 + 2 + w;
      int y1 = l1 + 80 + 12;
      int y2 = y1 + h;
      boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
      RenderUtils.drawRect((float)x1, (float)y1, (float)x2, (float)y2, hovered ? (new Color(203, 26, 26, 255)).getRGB() : Color.black.getRGB());
      RenderUtils.drawRect((float)x1, (float)y1 + 17, (float)x2, (float)y2, (new Color(200, 0, 0)).getRGB());
      this.text.drawCenteredString("Exit", (float)((x1 + x2) / 2), (float)((y1 + y2 - 15) / 2), Color.white.getRGB());
   }

   private void drawOptions(int mouseX, int mouseY) {
      int l1 = height / 4 + 48;
      int w = 98;
      int h = 20;
      int x1 = width / 2 - 100;
      int x2 = x1 + w;
      int y1 = l1 + 80 + 12;
      int y2 = y1 + h;
      boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
      RenderUtils.drawRect((float)x1, (float)y1, (float)x2, (float)y2, hovered ? (new Color(203, 26, 26, 255)).getRGB() : Color.black.getRGB());
      RenderUtils.drawRect((float)x1, (float)y1 + 17, (float)x2, (float)y2, (new Color(200, 0, 0)).getRGB());
      this.text.drawCenteredString("Settings", (float)((x1 + x2) / 2), (float)((y1 + y2 - 15) / 2), Color.white.getRGB());
   }

   private void drawAltmanager(int mouseX, int mouseY) {
      int l1 = height / 4 + 48;
      int w = 200;
      int h = 20;
      int x1 = width / 2 - 100;
      int x2 = x1 + w;
      int y1 = l1 + 48;
      int y2 = y1 + h;
      boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
      RenderUtils.drawRect((float)x1, (float)y1, (float)x2, (float)y2, hovered ? (new Color(203, 26, 26, 255)).getRGB() : Color.black.getRGB());
      RenderUtils.drawRect((float)x1, (float)y1 + 17, (float)x2, (float)y2, (new Color(200, 0, 0)).getRGB());
      this.text.drawCenteredString("Login", (float)((x1 + x2) / 2), (float)((y1 + y2 - 15) / 2), Color.white.getRGB());
   }

   public void drawSinglePlayer(int mouseX, int mouseY) {
      int l1 = height / 4 + 48;
      int w = 200;
      int h = 20;
      int x1 = width / 2 - 100;
      int x2 = x1 + w;
      int y2 = l1 + h;
      boolean hovered = RenderUtils.hover(x1, l1, mouseX, mouseY, w, h);
      RenderUtils.drawRect((float)x1, (float)l1, (float)x2, (float)y2, hovered ? (new Color(203, 26, 26, 255)).getRGB() : Color.black.getRGB());
      RenderUtils.drawRect((float)x1, (float)l1 + 17, (float)x2, (float)y2, (new Color(200, 0, 0)).getRGB());
      this.text.drawCenteredString("Singleplayer", (float)((x1 + x2) / 2), (float)((l1 + y2 - 15) / 2), Color.white.getRGB());
   }

   public void drawMultiPlayer(int mouseX, int mouseY) {
      int l1 = height / 4 + 48;
      int w = 200;
      int h = 20;
      int x1 = width / 2 - 100;
      int x2 = x1 + w;
      int y1 = l1 + 24;
      int y2 = y1 + h;
      boolean hovered = RenderUtils.hover(x1, y1, mouseX, mouseY, w, h);
      RenderUtils.drawRect((float)x1, (float)y1, (float)x2, (float)y2, hovered ? (new Color(203, 26, 26, 255)).getRGB() : Color.black.getRGB());
      RenderUtils.drawRect((float)x1, (float)y1 + 17, (float)x2, (float)y2, (new Color(200, 0, 0)).getRGB());
      this.text.drawCenteredString("Multiplayer", (float)((x1 + x2) / 2), (float)((y1 + y2 - 15) / 2), Color.white.getRGB());
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int l1 = height / 4 + 48;
      if (RenderUtils.hover(width / 2 + 2, l1 + 80 + 12, mouseX, mouseY, 98, 20) && mouseButton == 0) {
         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.mc.shutdown();
      }

      if (RenderUtils.hover(width / 2 + 2 - 100, l1 + 80 + 12, mouseX, mouseY, 98, 20) && mouseButton == 0) {
         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if (RenderUtils.hover(width / 2 - 100, l1 + 48, mouseX, mouseY, 200, 20) && mouseButton == 0) {
         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.mc.displayGuiScreen(new DirectLoginScreen(this));
      }

      if (RenderUtils.hover(width / 2 - 100, l1, mouseX, mouseY, 200, 20) && mouseButton == 0) {
         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
      }

      if (RenderUtils.hover(width / 2 - 100, l1 + 24, mouseX, mouseY, 200, 20) && mouseButton == 0) {
         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if (this.a()) {
         this.M.mouseClickedP(mouseX, mouseY, mouseButton);
      }

   }

   public void onGuiClosed() {
      if (this.M != null) {
         this.M.onGuiClosed();
      }

   }
}