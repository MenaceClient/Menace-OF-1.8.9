package dev.menace.ui.hud;

import com.google.gson.JsonObject;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.elements.*;
import dev.menace.ui.hud.options.BaseOption;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.ui.hud.options.ListOption;
import dev.menace.utils.file.FileManager;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class HUDManager {

    private  ArrayList<BaseElement> elements = new ArrayList<>();
    private ArrayList<Class<? extends BaseElement>> elementList = new ArrayList<>();

    public HUDSettingsElement settingsElement;

    public HUDManager() {
        //Add all elements to the elementList (Except HUDSettingsElement)
        elementList.add(ArmourElement.class);
        elementList.add(ArrayElement.class);
        elementList.add(GameStatsElement.class);
        elementList.add(PingElement.class);
        elementList.add(PositionElement.class);
        elementList.add(ScoreboardElement.class);
        elementList.add(SpotifyElement.class);
        elementList.add(TabGuiElement.class);
        elementList.add(TargetHudElement.class);
        elementList.add(WatermarkElement.class);

        loadElements();

        settingsElement = new HUDSettingsElement();
        registerElement(settingsElement);

        Menace.instance.eventManager.register(this);
    }

    public void registerElement(BaseElement element) {
        elements.add(element);
    }

    public void removeElement(BaseElement element) {
        elements.remove(element);
    }

    public ArrayList<BaseElement> getElements() {
        return elements;
    }

    public ArrayList<Class<? extends BaseElement>> getElementList() {
        return elementList;
    }

    @EventTarget
    public void onRender(EventRender2D event) {
        for (BaseElement element : elements) {
            element.render();
        }
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (BaseElement element : elements) {
            if (element instanceof TabGuiElement) {
                ((TabGuiElement) element).onKey(event);
            }
        }
    }

    public void saveElements() {
        JsonObject hudFile = new JsonObject();
        for (BaseElement element : elements) {
            String elementName = element.getClass().getSimpleName();
            //If the element already exists in the file, add a number to the end of the name
            if (hudFile.has(elementName)) {
                int i = 1;
                while (hudFile.has(elementName + i)) {
                    i++;
                }
                elementName += i;
            }

            JsonObject elementObject = new JsonObject();
            elementObject.addProperty("X", element.getPosX());
            elementObject.addProperty("Y", element.getPosY());

            JsonObject optionObject = new JsonObject();
            for (BaseOption option : element.getOptions()) {
                if (option instanceof BooleanOption) {
                    optionObject.addProperty(option.getName(), ((BooleanOption) option).getValue());
                } else if (option instanceof ColorSelectOption) {
                    optionObject.addProperty(option.getName(), ((ColorSelectOption) option).getColor().getRGB());
                } else if (option instanceof ListOption) {
                    optionObject.addProperty(option.getName(), ((ListOption) option).getSelected());
                }
            }

            elementObject.add("Options", optionObject);

            hudFile.add(elementName, elementObject);
        }

        //TODO: Multiple HUD files support
        FileManager.writeJsonToFile(new File(FileManager.getHudFolder(), "Hud.json"), hudFile);
    }

    public void loadElements() {
        if (!(new File(FileManager.getHudFolder(), "Hud.json").exists())) {
            saveElements();
            return;
        }

        JsonObject hudFile = FileManager.readJsonFromFile(new File(FileManager.getHudFolder(), "Hud.json"));
        assert hudFile != null;

        for (Class<? extends BaseElement> element : elementList) {
            //If there are multiple elements with the same name, add a number to the end of the name
            int i = 0;
            while (hudFile.has(element.getSimpleName() + (i == 0 ? "" : i))) {
                JsonObject elementObject = hudFile.getAsJsonObject(element.getSimpleName());
                BaseElement baseElement = null;

                try {
                    baseElement = element.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                assert baseElement != null;

                baseElement.setPosX(elementObject.get("X").getAsInt());
                baseElement.setPosY(elementObject.get("Y").getAsInt());

                JsonObject optionObject = elementObject.getAsJsonObject("Options");
                for (BaseOption option : baseElement.getOptions()) {
                    if (!optionObject.has(option.getName())) {
                        continue;
                    }

                    if (option instanceof BooleanOption) {
                        ((BooleanOption) option).setValue(optionObject.get(option.getName()).getAsBoolean());
                    } else if (option instanceof ColorSelectOption) {
                        ((ColorSelectOption) option).setColor(new Color(optionObject.get(option.getName()).getAsInt()));
                    } else if (option instanceof ListOption) {
                        ((ListOption) option).setSelected(optionObject.get(option.getName()).getAsString());
                    }
                }

                registerElement(baseElement);
                i++;
            }
        }

    }


}
