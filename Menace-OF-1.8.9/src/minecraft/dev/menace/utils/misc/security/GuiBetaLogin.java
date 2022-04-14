package dev.menace.utils.misc.security;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.lwjgl.input.Keyboard;

import com.google.common.hash.Hashing;

import dev.menace.altmanager.directlogin.AltLoginThread;
import dev.menace.altmanager.directlogin.AltLoginThreadBrowser;
import dev.menace.altmanager.directlogin.AltLoginThreadMsa;
import dev.menace.altmanager.directlogin.PasswordField;
import dev.menace.utils.misc.file.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiBetaLogin extends GuiScreen {

	private PasswordField password;
	
    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            
            case 1: {
                
            	if (this.password.getText().isEmpty()) return;
            	
            	String sha256hex = Hashing.sha256()
            			  .hashString(this.password.getText(), StandardCharsets.UTF_8)
            			  .toString();
            	
            	if (sha256hex.equalsIgnoreCase("f1828ed7dc093d757ab7a7313411d8b509987e6965a113c356641f1bdc7b8584")) {
            		FileManager.writeJsonToFile(new File(FileManager.getBetaFolder(), "beta.json"), password.getText());
            		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
            	}
            	
                break;
            }
            
            case 0: {
                Minecraft.getMinecraft().shutdownMinecraftApplet();
                break;
            }
        }
    }
	
    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Beta Login", width / 2, 20, -1);
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Beta Key", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12, 200, 20, "Login"));
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12 + 24, "Quit"));
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
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
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.password.updateCursorCounter();
    }
	
}
