package dev.menace.ui.hud.options;

import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class BooleanOption extends BaseOption {

    private boolean value;

    public BooleanOption(String name, boolean value) {
        super(name);
        this.setValue(value);
    }

    @Override
    public void render(double posX, double posY) {
        //Render checkbox
        RenderUtils.drawRect(posX, posY, posX + 10, posY + 10, Color.WHITE.getRGB());

        if (value) {
            RenderUtils.drawRect(posX + 1, posY + 1, posX + 9, posY + 9, Color.GREEN.getRGB());
        }

        //Render name
        fontRenderer.drawStringWithShadow(this.getName(), (float) (posX + 12), (float) (posY + 2), -1);

    }

    @Override
    public void mouseClicked(double posX, double posY, int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= posX && mouseX <= posX + 10 && mouseY >= posY && mouseY <= posY + 10) {
            this.setValue(!this.getValue());
        }
    }

    @Override
    public int getHeight() {
        return 10;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
        this.update();
    }
}
