package dev.menace.utils.misc;

import net.minecraft.util.ResourceLocation;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    public static void playSound(ResourceLocation resourceLocation) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream("/assets/minecraft/" + resourceLocation.getResourcePath()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
