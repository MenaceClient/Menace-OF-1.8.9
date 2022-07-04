package dev.menace.ui.altmanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import dev.menace.utils.render.GuiPasswordField;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class DirectLoginScreen
extends GuiScreen {
	private GuiPasswordField password;
	private final GuiScreen previousScreen;
	private GuiButton loginModeButton;
	private LoginThread thread;
	private GuiTextField username;
	private LoginMode loginModeName;


	public DirectLoginScreen(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 2: {
			this.mc.displayGuiScreen(this.previousScreen);
			break;
		}

		case 1: {
			switch (loginModeName) {
			case ALTENING: 
				loginModeName = LoginMode.ALTENINGPREMIUM;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			case ALTENINGPREMIUM:
				loginModeName = LoginMode.MICROSOFT_BROWSER;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			case MICROSOFT:
				loginModeName = LoginMode.ALTENING;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			case MICROSOFT_BROWSER:
				loginModeName = LoginMode.RNG;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			case RNG:
				loginModeName = LoginMode.MOJANG;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			case MOJANG:
				loginModeName = LoginMode.MICROSOFT;
				loginModeButton.displayString = "Mode: " + loginModeName.name;
				break;
			default:
				break;
			}
			break;
		}

		case 0: {
			this.thread = new LoginThread(this.username.getText(), this.password.getText(), this.loginModeName);
			this.thread.start();
			break;
		}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.username.drawTextBox();
		this.password.drawTextBox();
		this.loginModeButton.drawButton(mc, mouseX, mouseY);
		this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
		String status = null;
		if (thread instanceof LoginThread) {
			status = thread.getStatus();
		}
		this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? (Object)((Object)EnumChatFormatting.GRAY) + "Idle..." : status, width / 2, 29, -1);
		if (this.username.getText().isEmpty()) {
			this.drawString(this.mc.fontRendererObj, "Username / E-Mail / API Token", width / 2 - 96, 66, -7829368);
		}
		if (this.password.getText().isEmpty()) {
			this.drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.loginModeName = LoginMode.MOJANG;
		int var3 = height / 4 + 24;
		this.loginModeButton = new GuiButton(1, width / 2 - 100, var3 + 72 + 12, 100, 20, "Mode: " + loginModeName.name);
		this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
		this.buttonList.add(new GuiButton(0, width / 2, var3 + 72 + 12, 100, 20, "Login"));
		this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
		this.password = new GuiPasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
		this.username.setFocused(true);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(char character, int key) {
		if (character == '\t') {
			if (!this.username.isFocused() && !this.password.isFocused()) {
				this.username.setFocused(true);
			} else {
				this.username.setFocused(this.password.isFocused());
				this.password.setFocused(!this.username.isFocused());
			}
		}
		if (character == '\r') {
			this.actionPerformed((GuiButton)this.buttonList.get(0));
		}
		this.username.textboxKeyTyped(character, key);
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
		this.username.mouseClicked(x2, y2, button);
		this.password.mouseClicked(x2, y2, button);

		if (button == 0)
		{

			if (this.loginModeButton.mousePressed(this.mc, x2, y2))
			{
				this.selectedButton = loginModeButton;
				loginModeButton.playPressSound(this.mc.getSoundHandler());
				this.actionPerformed(loginModeButton);
			}
		}

	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}

	public enum LoginMode {
		MOJANG("Mojang"),
		MICROSOFT("Microsoft"),
		RNG("RNG"),
		ALTENING("Altening"),
		ALTENINGPREMIUM("AlteningPremium"),
		MICROSOFT_BROWSER("Browser");

		String name;
		private LoginMode(String name) {
			this.name = name;
		}
	}
}

