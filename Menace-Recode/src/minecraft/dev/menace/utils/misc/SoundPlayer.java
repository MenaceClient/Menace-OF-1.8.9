package dev.menace.utils.misc;


import net.minecraft.util.ResourceLocation;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundPlayer {

    public static void playSound(ResourceLocation resourceLocation) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceLocation.getClass().getResourceAsStream("/assets/minecraft/" + resourceLocation.getResourcePath()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
