package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.ui.hud.options.ListOption;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class ArrayElement extends BaseElement {

    private boolean customFont;
    private String alignMode;
    private String outlineMode;
    private String colorMode;
    private Color color;
    private Color color2;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font",  false) {
            @Override
            public void update() {
                ArrayElement.this.customFont = this.getValue();

                super.update();
            }
        });
        this.addOption(new ListOption("Align", new String[]{"Left", "Right"}, "Left") {
            @Override
            public void update() {
                ArrayElement.this.alignMode = this.getSelected();

                super.update();
            }
        });
        this.addOption(new ListOption("Outline", new String[]{"Back", "Full", "None"}, "Back") {
            @Override
            public void update() {
                ArrayElement.this.outlineMode = this.getSelected();

                super.update();
            }
        });
        this.addOption(new ListOption("Color Mode", new String[]{"Rainbow", "Fade", "Custom"}, "Rainbow") {
            @Override
            public void update() {
                ArrayElement.this.colorMode = this.getSelected();

                super.update();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.red) {
            @Override
            public void update() {
                ArrayElement.this.color = this.getColor();

                super.update();
            }
        });
        this.addOption(new ColorSelectOption("Color2", new Color(203, 26, 26)) {
            @Override
            public void update() {
                ArrayElement.this.color2 = this.getColor();

                super.update();
            }
        });
    }

    private double lastWidth = 0;

    @Override
    public void render() {
        ArrayList<BaseModule> enabledModules = Menace.instance.moduleManager.getActiveModules().stream()
                .filter(BaseModule::isVisible)
                .sorted((m1, m2) -> this.getStringWidth(m2.getDisplayName(), this.customFont) - this.getStringWidth(m1.getDisplayName(), this.customFont))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        int y = (int) this.getPosY();

        if (outlineMode.equalsIgnoreCase("Full")) {
            //Draw top bar
            if (alignMode.equalsIgnoreCase("Left")) {
                RenderUtils.drawRect(this.getPosX(), y - 1, this.getPosX() + this.getStringWidth(enabledModules.get(0).getDisplayName(), this.customFont) + 1, y, getColor(y).getRGB());
            } else {
                RenderUtils.drawRect(this.getPosX() - this.getStringWidth(enabledModules.get(0).getDisplayName(), this.customFont) + this.getWidth(), y - 1, this.getPosX() + getWidth(), y, getColor(y).getRGB());
            }
        }

        lastWidth = this.getStringWidth(enabledModules.get(0).getDisplayName(), this.customFont);

        for (BaseModule m : enabledModules) {
            if (alignMode.equalsIgnoreCase("Left")) {
                //Align left
                RenderUtils.drawRect(this.getPosX(), y - 1, this.getPosX() + this.getStringWidth(m.getDisplayName(), this.customFont) + 1, y + this.getFontHeight(this.customFont), new Color(0, 0, 0, 100).getRGB());

                if (outlineMode.equalsIgnoreCase("Back")) {
                    RenderUtils.drawRect(this.getPosX(), y - 1, this.getPosX() - 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());
                } else if (outlineMode.equalsIgnoreCase("Full")) {
                    RenderUtils.drawRect(this.getPosX(), y - 1, this.getPosX() - 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());
                    RenderUtils.drawRect(this.getPosX() + this.getStringWidth(m.getDisplayName(), this.customFont), y - 1, this.getPosX() + this.getStringWidth(m.getDisplayName(), this.customFont) + 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());

                    if (this.getStringWidth(m.getDisplayName(), this.customFont) < lastWidth) {
                        RenderUtils.drawRect(this.getPosX() + this.getStringWidth(m.getDisplayName(), this.customFont), y - 1, this.getPosX() + lastWidth + 1, y, getColor(y).getRGB());
                    }

                }

                this.drawString(m.getDisplayName(), this.getPosX(), y, getColor(y).getRGB(), this.customFont);
            } else {
                //Align right
                RenderUtils.drawRect(this.getPosX() + this.getStringWidth("ArrayList", this.customFont), y - 1, this.getPosX() - this.getStringWidth(m.getDisplayName(), this.customFont) + this.getStringWidth("ArrayList", this.customFont) - 1, y + this.getFontHeight(this.customFont), new Color(0, 0, 0, 100).getRGB());

                if (outlineMode.equalsIgnoreCase("Back")) {
                    RenderUtils.drawRect(this.getPosX() + getWidth(), y - 1, this.getPosX() + getWidth() - 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());
                } else if (outlineMode.equalsIgnoreCase("Full")) {
                    RenderUtils.drawRect(this.getPosX() + getWidth(), y - 1, this.getPosX() + getWidth() - 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());
                    RenderUtils.drawRect(this.getPosX() - this.getStringWidth(m.getDisplayName(), this.customFont) + this.getStringWidth("ArrayList", this.customFont), y - 1, this.getPosX() - this.getStringWidth(m.getDisplayName(), this.customFont) + this.getStringWidth("ArrayList", this.customFont) - 1, y + this.getFontHeight(this.customFont), getColor(y).getRGB());

                    if (this.getStringWidth(m.getDisplayName(), this.customFont) < lastWidth) {
                        RenderUtils.drawRect(this.getPosX() - this.getStringWidth(m.getDisplayName(), this.customFont) + this.getStringWidth("ArrayList", this.customFont), y - 1, this.getPosX() + this.getStringWidth("ArrayList", this.customFont) - lastWidth - 1, y, getColor(y).getRGB());
                    }
                }

                this.drawString(m.getDisplayName(), this.getPosX() - this.getStringWidth(m.getDisplayName(), this.customFont) + this.getStringWidth("ArrayList", this.customFont), y, getColor(y).getRGB(), this.customFont);
            }

            lastWidth = this.getStringWidth(m.getDisplayName(), this.customFont);
            y += this.getFontHeight(this.customFont) + 1;
        }

        if (outlineMode.equalsIgnoreCase("Full")) {
            //Draw bottom bar
            if (alignMode.equalsIgnoreCase("Left")) {
                RenderUtils.drawRect(this.getPosX() - 1, y - 1, this.getPosX() + this.getStringWidth(enabledModules.get(enabledModules.size() - 1).getDisplayName(), this.customFont) + 1, y, getColor(y).getRGB());
            } else {
                RenderUtils.drawRect(this.getPosX() - this.getStringWidth(enabledModules.get(enabledModules.size() - 1).getDisplayName(), this.customFont) + this.getWidth() - 1, y - 1, this.getPosX() + getWidth(), y, getColor(y).getRGB());
            }
        }
    }

    private Color getColor(int y) {
        if (colorMode.equalsIgnoreCase("Rainbow")) {
            return ColorUtils.cleanRainbow(10, y);
        } else if (colorMode.equalsIgnoreCase("Fade")) {
            return ColorUtils.fade(color, color2, y);
        } else {
            return color;
        }
    }

    @Override
    public void renderDummy() {
        this.drawString("ArrayList", this.getPosX(), this.getPosY(), getColor((int) this.getPosY()).getRGB(), this.customFont);
    }

    @Override
    public int getWidth() {
        return this.getStringWidth("ArrayList", this.customFont);
    }

    @Override
    public int getHeight() {
        return this.getFontHeight(this.customFont);
    }
}
