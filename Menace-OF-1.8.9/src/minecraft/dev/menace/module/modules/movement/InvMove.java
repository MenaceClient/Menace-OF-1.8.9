package dev.menace.module.modules.movement;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;

public class InvMove extends Module {

	//Settings
	BoolSetting rotate;
	
	public InvMove() {
		super("InventoryMove", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		rotate = new BoolSetting("Rotate", this, true);
		this.rSetting(rotate);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.currentScreen instanceof GuiContainer || MC.currentScreen instanceof GuiInventory) {
			
            MC.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindForward);
            MC.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindBack);
            MC.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindRight);
            MC.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindLeft);
            MC.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindJump);
            MC.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindSprint);
			
            
            if (rotate.isChecked()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    if (MC.thePlayer.rotationPitch > -90) {
                        MC.thePlayer.rotationPitch -= 5;
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    if (MC.thePlayer.rotationPitch < 90) {
                        MC.thePlayer.rotationPitch += 5;
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    MC.thePlayer.rotationYaw -= 5;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    MC.thePlayer.rotationYaw += 5;
                }
            }
		}
		
	}

}
