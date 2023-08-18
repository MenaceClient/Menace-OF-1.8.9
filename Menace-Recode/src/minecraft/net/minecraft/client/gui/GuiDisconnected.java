package net.minecraft.client.gui;

import dev.menace.Menace;
import dev.menace.ui.altmanager.DirectLoginScreen;
import dev.menace.ui.hud.elements.GameStatsElement;
import dev.menace.utils.misc.ServerUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;

public class GuiDisconnected extends GuiScreen
{
    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    private final long time;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
        time = System.currentTimeMillis();
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
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu")));
        this.buttonList.add(new GuiButton(69, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 25, "Reconnect"));
        this.buttonList.add(new GuiButton(420, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 50, "AltManager"));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 69) {
            Menace.instance.hudManager.getElements().stream().filter(element -> element instanceof GameStatsElement).forEach(element -> {
                ((GameStatsElement) element).reset();
            });
        	ServerUtils.connectToLastServer(this.parentScreen);
        } else if (button.id == 420) {
            mc.displayGuiScreen(new DirectLoginScreen(this.parentScreen));
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
                this.drawCenteredString(this.fontRendererObj, s, width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        //GameStats
        this.drawCenteredString(this.fontRendererObj, "Username: " + mc.session.getUsername(), width / 2, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 75, -1);
        if (Menace.instance.hudManager.getElements().stream().anyMatch(element -> element instanceof GameStatsElement) &&
                ((GameStatsElement)Menace.instance.hudManager.getElements().stream().filter(element -> element instanceof GameStatsElement).findFirst().get()).timer.getStartTime() != -1) {
            LocalTime lt = LocalTime.ofSecondOfDay((time - ((GameStatsElement)Menace.instance.hudManager.getElements().stream().filter(element -> element instanceof GameStatsElement).findFirst().get()).timer.getStartTime()) / 1000);
            String second = lt.getSecond() < 10 ? "0" + lt.getSecond() : String.valueOf(lt.getSecond());
            String hour = lt.getHour() != 0 ? lt.getHour() + ":" : "";
            this.drawCenteredString(this.fontRendererObj, "Play time: " + hour + lt.getMinute() + ":" + second, width / 2, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 85, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
