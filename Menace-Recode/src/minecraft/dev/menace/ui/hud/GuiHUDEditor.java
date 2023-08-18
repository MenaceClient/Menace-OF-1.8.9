package dev.menace.ui.hud;

import dev.menace.Menace;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.elements.HUDSettingsElement;
import dev.menace.ui.hud.options.BaseOption;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class GuiHUDEditor extends GuiScreen {

    HUDManager hudManager;

    private Optional<BaseElement> selectedElement = Optional.empty();

    private int prevX, prevY;

    public GuiHUDEditor() {
        this.hudManager = Menace.instance.hudManager;

        Collection<BaseElement> elements = hudManager.getElements();

        for (BaseElement element : elements) {
            adjustBounds(element);
        }

    }

    @Override
    public void updateScreen() {
        //HUD settings
        hudManager.settingsElement.setSelectedElement(selectedElement);
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        //Draw HUD Elements
        final float zBackup = this.zLevel;
        this.zLevel = 200;

        RenderUtils.drawHollowRect(0, 0, width, height,  1, 0xFFFF0000);

        for (BaseElement element : hudManager.getElements()) {
            element.renderDummy();

            if (element == selectedElement.orElse(null)) {
                RenderUtils.drawHollowRect((int) element.getPosX() - 1, (int) element.getPosY(), (int) (element.getPosX() + element.getWidth()) + 1, (int) (element.getPosY() + element.getHeight()) + 2, 1, 0xFF00FFFF);
            }
        }

        this.zLevel = zBackup;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selectedElement.isPresent() && !(selectedElement.get() instanceof HUDSettingsElement)) {
            for (BaseElement element : hudManager.getElements()) {
                if (element == selectedElement.get()) {
                    for (BaseOption option : element.getOptions()) {
                        option.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            hudManager.saveElements();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onGuiClosed() {
        hudManager.saveElements();
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        if (selectedElement.isPresent()) {
            moveSelectedElement(x - prevX, y - prevY);
        }

        this.prevX = x;
        this.prevY = y;
    }

    private void moveSelectedElement(int offsetX, int offsetY) {
        BaseElement renderer = selectedElement.get();

        renderer.setPos(renderer.getPosX() + offsetX, renderer.getPosY() + offsetY);

        adjustBounds(renderer);
    }


    private void adjustBounds(BaseElement renderer) {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        double absoluteX = Math.max(0, Math.min(renderer.getPosX(), Math.max(screenWidth - renderer.getWidth(), 0)));
        double absoluteY = Math.max(0, Math.min(renderer.getPosY(), Math.max(screenHeight - renderer.getHeight(), 0)));

        renderer.setPos(absoluteX, absoluteY);

    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {

        this.prevX = x;
        this.prevY = y;

        if (selectedElement.isPresent()
                && hudManager.getElements().stream().filter(new MouseOverFinder(x, y)).findFirst().orElse(null) instanceof HUDSettingsElement) {
            Menace.instance.hudManager.settingsElement.mouseClicked(x, y, button);
            return;
        }

        loadMouseOver(x, y);

        if (button == 2) {
            if (selectedElement.isPresent()) {
                hudManager.getElements().remove(selectedElement.get());
                selectedElement = Optional.empty();
            }
        }
    }

    private void loadMouseOver(int x, int y) {
        this.selectedElement = hudManager.getElements().stream().filter(new MouseOverFinder(x, y)).findFirst();
    }

    private static class MouseOverFinder implements Predicate<BaseElement> {

        private final int mouseX;
        private final int mouseY;

        public MouseOverFinder(int x, int y) {
            this.mouseX = x;
            this.mouseY = y;
        }

        @Override
        public boolean test(BaseElement renderer) {

            double absoluteX = renderer.getPosX();
            double absoluteY = renderer.getPosY();

            if (mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) {
                return mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight();
            }

            return false;
        }

    }

}
