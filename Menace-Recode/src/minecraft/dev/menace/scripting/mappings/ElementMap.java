package dev.menace.scripting.mappings;

import dev.menace.scripting.ScriptElement;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;

public class ElementMap {

    private ScriptElement element;
    private String name;

    public ArrayList<ScriptObjectMirror> renderCallbacks = new ArrayList<>();

    public ElementMap() {}

    public ElementMap(ScriptElement element, String name) {
        this.element = element;
        this.name = name;
        element.setElementMap(this);
    }

    public String getName() {
        return name;
    }

    public void render(ScriptObjectMirror callback) {
        renderCallbacks.add(callback);
    }

    public double getPosX() {
        return element.getAbsoluteX();
    }

    public double getPosY() {
        return element.getAbsoluteY();
    }

    public boolean isVisible() {
        return element.isVisible();
    }

    public double getWidth() {
        return element.getWidth();
    }

    public double getHeight() {
        return element.getHeight();
    }

    public void drawString(String text, double x, double y) {
        element.drawString(text, (int) x, (int) y);
    }

    public void drawString(String text, double x, double y, int color) {
        element.drawString(text, (int) x, (int) y, color);
    }

}
