package dev.menace.scripting.js.mappings;

import dev.menace.scripting.ScriptElement;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;

@JSMapping
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

    @MappedName(1)
    public String getName() {
        return name;
    }

    @MappedName(2)
    public void render(ScriptObjectMirror callback) {
        renderCallbacks.add(callback);
    }

    @MappedName(3)
    public double getPosX() {
        return element.getAbsoluteX();
    }

    @MappedName(4)
    public double getPosY() {
        return element.getAbsoluteY();
    }

    @MappedName(6)
    public boolean isVisible() {
        return element.isVisible();
    }

    @MappedName(7)
    public double getWidth() {
        return element.getWidth();
    }

    @MappedName(8)
    public double getHeight() {
        return element.getHeight();
    }

    @MappedName(9)
    public void drawString(String text, double x, double y) {
        element.drawString(text, (int) x, (int) y);
    }

    @MappedName(9)
    public void drawString(String text, double x, double y, int color) {
        element.drawString(text, (int) x, (int) y, color);
    }

}
