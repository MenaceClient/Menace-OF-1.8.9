package dev.menace.ui.clickgui.menace.components;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.ui.clickgui.menace.MenaceClickGui;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.animtion.Animate;
import dev.menace.utils.render.animtion.Easing;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CategorySelectorComponent {

    public int x, y, width, height;
    private final Animate categoryBarAnimation;
    private boolean categoryBarExpanded;
    private boolean categoryAntimationFinished;
    private boolean categoryCloseAnimation;
    private boolean categoryCloseAnimationFinished;
    private final int categoryX, categoryY, categoryWidth;
    private final MenaceFontRenderer fontRenderer;
    private final MenaceClickGui parent;

    public CategorySelectorComponent(MenaceClickGui parent) {
        this.parent = parent;
        this.x = parent.x;
        this.y = parent.y;
        this.width = parent.width;
        this.height = parent.height;
        this.categoryX = parent.x + 170;
        this.categoryY = parent.y + 5;
        this.categoryWidth = parent.width - 170;
        categoryBarExpanded = false;
        categoryAntimationFinished = false;
        categoryCloseAnimation = false;
        categoryCloseAnimationFinished = true;
        categoryBarAnimation = new Animate().setMin(8).setMax(65).setSpeed(150).setEase(Easing.LINEAR).setReversed(false);
        fontRenderer = Menace.instance.sfPro;
    }

    public void draw(int mouseX, int mouseY) {
        //Category bar
        if (RenderUtils.hover2(this.categoryX, this.categoryY, this.categoryWidth, this.y + 65, mouseX, mouseY) && categoryBarExpanded && categoryAntimationFinished) {
            RenderUtils.drawRoundedRect(this.categoryX, this.categoryY, this.categoryWidth, this.y + 65, 5, Color.RED.getRGB());
            RenderUtils.drawRoundedRect(this.categoryX + 5, this.categoryY + 5, this.categoryWidth - 5, this.y + 60, 5, Color.BLACK.getRGB());

            //Draw each category selector with its icon and name underneath
            int xOff = 10;
            for (Category category : Category.values()) {
                if (parent.selectedCategory == category) {
                    //Highlight background red
                    RenderUtils.drawRoundedRect(this.categoryX + 5 + xOff, this.categoryY + 6, this.categoryX + 41 + xOff, this.categoryY + 41, 1, Color.RED.getRGB());
                }

                ResourceLocation icon = new ResourceLocation("menace/menaceui/" + category.getName().toLowerCase() + ".png");
                RenderUtils.drawImage(this.categoryX + 7 + xOff, this.categoryY + 7, 32, 32, icon, new Color(255, 255, 255, 0));
                fontRenderer.drawCenteredString(category.getName(), this.categoryX + 23 + xOff, this.categoryY + 50, Color.WHITE.getRGB());

                xOff += 60;
            }

        } else if (RenderUtils.hover2(this.categoryX, this.categoryY, this.categoryWidth, (int) (this.y + Math.max(15, categoryBarAnimation.getValue())), mouseX, mouseY) && categoryBarExpanded && !categoryAntimationFinished) {
            categoryCloseAnimation = false;
            categoryCloseAnimationFinished = false;

            RenderUtils.drawRoundedRect(this.categoryX, this.categoryY, this.categoryWidth, this.y + categoryBarAnimation.getValue(), 5, Color.RED.getRGB());

            RenderUtils.drawRoundedRect(this.categoryX + 5, this.categoryY + 5, this.categoryWidth - 5, this.y + categoryBarAnimation.getValue() - 5, (float)(categoryBarAnimation.getValue() / 7.0), Color.BLACK.getRGB());

            categoryBarAnimation.update();

            if (categoryBarAnimation.getValue() >= categoryBarAnimation.getMax()) {
                categoryAntimationFinished = true;
            }
        } else if (RenderUtils.hover2(this.categoryX, this.categoryY, this.categoryWidth, this.y + 15, mouseX, mouseY) && !categoryBarExpanded) {
            RenderUtils.drawRoundedRect(this.categoryX, this.categoryY, this.categoryWidth, this.y + 15, 5, Color.RED.getRGB());
            categoryBarExpanded = true;
            categoryAntimationFinished = false;
            categoryBarAnimation.resetTime();
        } else {
            if (!categoryCloseAnimationFinished && !categoryCloseAnimation) {
                categoryBarAnimation.setReversed(true);
                categoryBarAnimation.resetTime();
                categoryCloseAnimation = true;

            } else if (!categoryCloseAnimationFinished && categoryCloseAnimation) {
                RenderUtils.drawRoundedRect(this.categoryX, this.categoryY, this.categoryWidth, this.y + categoryBarAnimation.getValue(), 5, Color.RED.getRGB());

                if (categoryBarAnimation.getValue() > 20) {
                    RenderUtils.drawRoundedRect(this.categoryX + 5, this.categoryY + 5, this.categoryWidth - 5, this.y + categoryBarAnimation.getValue() - 5, (float) (categoryBarAnimation.getValue() / 7.0), Color.BLACK.getRGB());
                }

                categoryBarAnimation.update();

                if (categoryBarAnimation.isFinished()) {
                    categoryCloseAnimationFinished = true;
                    categoryBarAnimation.setReversed(false);
                }
            } else {
                RenderUtils.drawRoundedRect(this.categoryX, this.categoryY, this.categoryWidth, this.y + 15, 5, Color.RED.getRGB());
                categoryBarExpanded = false;
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.hover2(this.categoryX, this.categoryY, this.categoryWidth, this.y + 65, mouseX, mouseY) && categoryBarExpanded && categoryAntimationFinished) {
            int xOff = 10;
            for (Category category : Category.values()) {
                if (RenderUtils.hover2(this.categoryX + 5 + xOff, this.categoryY + 6, this.categoryX + 5 + xOff + 35, this.categoryY + 40, mouseX, mouseY)) {
                    parent.selectedCategory = category;
                }

                xOff += 60;
            }
            return true;
        }

        return false;
    }

}
