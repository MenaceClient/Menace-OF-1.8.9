package dev.menace.scripting;

import dev.menace.scripting.js.mappings.ElementMap;
import dev.menace.ui.hud.BaseElement;

public class ScriptElement extends BaseElement {

    private ElementMap elementMap;

    double width = 10;
    double height = 10;

    public ScriptElement(int posX, int posY, boolean visible) {
        super(posX, posY, visible);
    }

    public void setElementMap(ElementMap elementMap) {
        this.elementMap = elementMap;
    }

    public ElementMap getElementMap() {
        return elementMap;
    }

    @Override
    public void render() {
        elementMap.renderCallbacks.forEach(callback -> callback.call(elementMap));
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return (int) width;
    }

    @Override
    public int getHeight() {
        return (int) height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}
