package dev.menace.ui.hud.elements;

import com.google.gson.JsonObject;
import dev.menace.Menace;
import dev.menace.ui.clickgui.lime.utils.render.animation.easings.Animate;
import dev.menace.ui.clickgui.lime.utils.render.animation.easings.Easing;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;

public class SpotifyElement extends BaseElement {

    String imageLink = "";
    ResourceLocation albumImage = null;
    private JsonObject lastSongData = null;
    private final Animate songAnimation;
    private final Animate artistAnimation;

    public SpotifyElement() {
        super(0.5, 0.5, true);
        songAnimation = new Animate().setMin(0).setMax(5).setSpeed(5).setEase(Easing.QUAD_IN_OUT).setReversed(false);
        artistAnimation = new Animate().setMin(0).setMax(5).setSpeed(5).setEase(Easing.QUAD_IN_OUT).setReversed(false);
    }

    @Override
    public void render() {
        JsonObject songData = Menace.instance.spotifyUtils.getCurrentSong();
        int songProgress = Menace.instance.spotifyUtils.getSongProgress();
        boolean songchanged = false;
        if (songData == null) return;
        if (lastSongData == null) lastSongData = songData;
        if (!Objects.equals(songData.get("name").getAsString(), lastSongData.get("name").getAsString())) {
            songchanged = true;
            lastSongData = songData;
        }

        if (!Objects.equals(imageLink, songData.get("album").getAsJsonObject().get("images").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString())) {
            imageLink = songData.get("album").getAsJsonObject().get("images").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
            BufferedImage texture;
            try {
                URL textureUrl = new URL(imageLink);
                texture = ImageIO.read(textureUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            albumImage = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("image", new DynamicTexture(texture));
        }

        String songName = songData.get("name").getAsString();
        StringBuilder artistName = new StringBuilder();

        for (int i = 0; i < songData.get("artists").getAsJsonArray().size(); i++) {
            artistName.append(songData.get("artists").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString());
            if (i != songData.get("artists").getAsJsonArray().size() - 1) {
                artistName.append(", ");
            }
        }

        //IM GOING TO KILL MYSELF

        //Make the song name scroll back and forth slowly
        if (this.getStringWidth(songName) > 100) {
            if (songchanged) songAnimation.setValue(0);

            int length = songName.length() - this.reverseWrapString(songName, 100).length() + "_".length();
            songAnimation.setMax(length);

            if (this.getStringWidth(songName.substring((int) songAnimation.getValue())) <= 100) {
                songAnimation.setReversed(true);
            } else if (songAnimation.getValue() == 0) {
                songAnimation.setReversed(false);
            }

            songName = this.wrapString(songName.substring((int) songAnimation.getValue()), 100);

            songAnimation.update();
        }

        //Make the artist name scroll back and forth slowly
        String newName = artistName.toString();
        if (this.getStringWidth(newName) > 100) {
            if (songchanged) artistAnimation.setValue(0);

            int length = newName.length() - this.reverseWrapString(newName, 100).length() + "_".length();
            artistAnimation.setMax(length);
            artistAnimation.update();

            if (artistAnimation.getValue() == artistAnimation.getMax()) {
                artistAnimation.setReversed(true);
            } else if (artistAnimation.getValue() <= 0) {
                artistAnimation.setValue(0);
                artistAnimation.setReversed(false);
            }

            newName = this.wrapString(newName.substring((int) artistAnimation.getValue()), 100);
        }

        RenderUtils.drawRoundedRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + 150, this.getAbsoluteY() + 45, 5, new Color(0, 0, 0, 120).getRGB());

        RenderUtils.drawImage(this.getAbsoluteX() + 5, this.getAbsoluteY() + 5, 30, 30, albumImage, Color.white);

        this.drawString(songName, this.getAbsoluteX() + 40, this.getAbsoluteY() + 5, Color.white.getRGB());
        this.drawString(newName, this.getAbsoluteX() + 40, this.getAbsoluteY() + this.getFontHeight() + 10, Color.white.getRGB());

        //Draws the progress bar
        int progressPercentage = (int) (((double) songProgress / (double) songData.get("duration_ms").getAsInt()) * 145);
        progressPercentage = 145 - progressPercentage;
        progressPercentage = Math.max(0, progressPercentage);
        RenderUtils.drawRoundedRect(this.getAbsoluteX() + 5, this.getAbsoluteY() + 42, this.getAbsoluteX() + 145, this.getAbsoluteY() + 38, 2, new Color(0, 0, 0, 120).getRGB());
        RenderUtils.drawRoundedRect(this.getAbsoluteX() + 5, this.getAbsoluteY() + 42, this.getAbsoluteX() + 5 + progressPercentage, this.getAbsoluteY() + 38, 2, new Color(255, 255, 255, 255).getRGB());

    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 45;
    }
}
