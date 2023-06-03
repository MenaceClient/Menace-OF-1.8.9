package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArmourElement extends BaseElement {


    public ArmourElement() {
        super(486, 482, true);
    }

    @Override
    public void render() {

        if (mc.thePlayer.inventory.armorInventory.length > 0) {
            List<ItemStack> items = new ArrayList<>();
            if (mc.thePlayer.getHeldItem() != null) {
                items.add(mc.thePlayer.getHeldItem());
            }
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
                if (stack != null) {
                    items.add(stack);
                }
            }
            int x = this.getAbsoluteX();
            for (ItemStack stack : items) {
                GlStateManager.pushMatrix();
                GlStateManager.enableLighting();
                mc.getRenderItem().renderItemIntoGUI(stack, x, this.getAbsoluteY());
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, this.getAbsoluteY(), "");
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                if (stack.isStackable() && stack.stackSize > 1) {
                    this.drawString(String.valueOf(stack.stackSize), x + 4, this.getAbsoluteY() + 4, Color.black.getRGB());
                }
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
                x += 18;
            }
        }

    }

    @Override
    public void renderDummy() {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Items.diamond_sword, 1, 0));
        items.add(new ItemStack(Items.diamond_helmet, 1, 0));
        items.add(new ItemStack(Items.diamond_chestplate, 1, 0));
        items.add(new ItemStack(Items.diamond_leggings, 1, 0));
        items.add(new ItemStack(Items.diamond_boots, 1, 0));

        int x = this.getAbsoluteX();
        for (ItemStack stack : items) {
            GlStateManager.pushMatrix();
            GlStateManager.enableLighting();
            mc.getRenderItem().renderItemIntoGUI(stack, x, this.getAbsoluteY());
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, this.getAbsoluteY(), "");
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            if (stack.isStackable() && stack.stackSize > 1) {
                this.drawString(String.valueOf(stack.stackSize), x + 4, this.getAbsoluteY() + 4, Color.black.getRGB());
            }
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            x += 18;
        }
    }

    @Override
    public int getWidth() {
        return 90;
    }

    @Override
    public int getHeight() {
        return 18;
    }
}
