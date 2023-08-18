package dev.menace.ui.hud.options;

import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Util;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ColorSelectOption extends BaseOption {
    private static final int PICKER_SIZE = 40;
    private static final int SLIDER_WIDTH = 10;

    Color color;
    private float selectedHue = 0.0f;

    private int pickerX;
    private int pickerY;

    public ColorSelectOption(String name, Color defaultColor) {
        super(name);

        this.setColor(defaultColor);
    }

    @Override
    public void render(double posX, double posY) {
        pickerX = (int) posX;
        pickerY = (int) posY;

        drawColorPicker((int) posX, (int) posY);
    }

    private void drawColorPicker(int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        // Draw the color picker (color gradient)
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        Color col;
        for (int xx = 0; xx < PICKER_SIZE; xx++) {
            for (int yy = 0; yy < PICKER_SIZE; yy++) {
                //update color
                col = Color.getHSBColor(selectedHue, xx / (float) PICKER_SIZE, 1.0f - yy / (float) PICKER_SIZE);

                //Draw each pixel with the calculated color
                worldRenderer.pos(x + xx, y + yy, 0).tex(0, 0).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
                worldRenderer.pos(x + xx, y + yy + 1, 0).tex(0, 1).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
                worldRenderer.pos(x + xx + 1, y + yy + 1, 0).tex(1, 1).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
                worldRenderer.pos(x + xx + 1, y + yy, 0).tex(1, 0).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
            }
        }
        tessellator.draw();


        // Draw the hue selector handle (rainbow gradient)
        float selectorY = y + 35;
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (int i = 0; i < PICKER_SIZE; i++) {
            // Calculate the hue for the current pixel row
            float hue = i / (float) PICKER_SIZE;

            // Calculate the colors based on the current hue
            Color color1 = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
            Color color2 = new Color(Color.HSBtoRGB(hue + 1.0f / PICKER_SIZE, 1.0f, 1.0f));

            // Vertex positions
            double posX1 = x + PICKER_SIZE + 10;
            double posX2 = x + PICKER_SIZE + 5;
            double posY1 = selectorY + SLIDER_WIDTH / 2 - i;
            double posY2 = selectorY + SLIDER_WIDTH / 2 - (i + 1);

            // Texture coordinates (not used here, but kept for consistency)
            double texX1 = 0;
            double texX2 = 1;
            double texY1 = 0;
            double texY2 = 1;

            // Add vertices with calculated colors and positions
            worldRenderer.pos(posX1, posY1, 0).tex(texX1, texY1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), 255).endVertex();
            worldRenderer.pos(posX1, posY2, 0).tex(texX1, texY2).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 255).endVertex();
            worldRenderer.pos(posX2, posY2, 0).tex(texX2, texY2).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 255).endVertex();
            worldRenderer.pos(posX2, posY1, 0).tex(texX2, texY1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), 255).endVertex();
        }

        tessellator.draw();


        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void mouseClicked(double posX, double posY, int mouseX, int mouseY, int mouseButton) {
        // Check if the mouse click is inside the color picker or on the hue slider
        if (isMouseInPicker(mouseX, mouseY)) {
            // Get the color of the pixel the mouse is hovering over
            Color selectedColor = new Color(Color.HSBtoRGB(selectedHue, (float) (mouseX - posX) / PICKER_SIZE, 1.0f - (float) (mouseY - posY) / PICKER_SIZE));

            this.setColor(selectedColor);
        } else if (RenderUtils.hover((int)posX + 40 + 5, (int)posY, mouseX, mouseY, 10, 40)) {
            selectedHue = (pickerY - mouseY) / (float) PICKER_SIZE;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        //If the user pastes a color code, try to parse it
        if (keyCode == Keyboard.KEY_V && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            String clipboard;
            try {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
            } catch (UnsupportedFlavorException | IOException e) {
                //Clipboard does not contain a string
                return;
            }

            //Check if the clipboard contains a valid color code
            if (clipboard.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
                //Parse the color code
                Color color = Color.decode(clipboard);

                //Set the color
                this.setColor(color);
                selectedHue = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
                ChatUtils.message("Color set to " + clipboard);
            }
            //Check for an RGB input
            else  if (clipboard.matches("^rgb\\((\\d{1,3}),\\s*(\\d{1,3}),\\s*(\\d{1,3})\\)$")) {
                //Parse the RGB values
                String[] rgb = clipboard.substring(4, clipboard.length() - 1).split(",");

                //Check if the RGB values are valid
                if (rgb.length != 3) {
                    return;
                }

                //Parse the color
                Color color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

                //Set the color
                this.setColor(color);
                selectedHue = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
                ChatUtils.message("Color set to " + clipboard);
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    private boolean isMouseInPicker(int mouseX, int mouseY) {
        return mouseX >= pickerX && mouseX < pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    public void setColor(Color color) {
        this.color = color;

        this.update();
    }

    public Color getColor() {
        return color;
    }

}
