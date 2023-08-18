package dev.menace.ui.hud.elements;

import com.google.gson.JsonObject;
import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.animtion.Animate;
import dev.menace.utils.render.animtion.Easing;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SpotifyElement extends BaseElement {

    private boolean customFont;

    String imageLink = "";
    ResourceLocation albumImage = null;
    private JsonObject lastSongData = null;
    private final Animate songAnimation = new Animate().setMin(0).setMax(5).setSpeed(5).setEase(Easing.QUAD_IN_OUT).setReversed(false);
    private final Animate artistAnimation = new Animate().setMin(0).setMax(5).setSpeed(5).setEase(Easing.QUAD_IN_OUT).setReversed(false);

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                SpotifyElement.this.customFont = this.getValue();
                super.update();
            }
        });
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
            albumImage = mc.getTextureManager().getDynamicTextureLocation("image", new DynamicTexture(texture));
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
        if (this.getStringWidth(songName, this.customFont) > 100) {
            if (songchanged) songAnimation.setValue(0);

            int length = songName.length() - this.reverseWrapString(songName, 100, this.customFont).length() + "_".length();
            songAnimation.setMax(length);
            songAnimation.update();

            if (songAnimation.getValue() == songAnimation.getMax()) {
                songAnimation.setReversed(true);
            } else if (songAnimation.getValue() <= 0) {
                songAnimation.setReversed(false);
            }

            songName = this.wrapString(songName.substring((int) songAnimation.getValue()), 100, this.customFont);
        }

        //Make the artist name scroll back and forth slowly
        String newName = artistName.toString();
        if (this.getStringWidth(newName, this.customFont) > 100) {
            if (songchanged) artistAnimation.setValue(0);

            int length = newName.length() - this.reverseWrapString(newName, 100, this.customFont).length() + "_".length();
            artistAnimation.setMax(length);
            artistAnimation.update();

            if (artistAnimation.getValue() == artistAnimation.getMax()) {
                artistAnimation.setReversed(true);
            } else if (artistAnimation.getValue() <= 0) {
                artistAnimation.setValue(0);
                artistAnimation.setReversed(false);
            }

            newName = this.wrapString(newName.substring((int) artistAnimation.getValue()), 100, this.customFont);
        }

        RenderUtils.drawRoundedRect((float) this.getPosX(), (float) this.getPosY(), (float) (this.getPosX() + 150), (float) (this.getPosY() + 45), 5, new Color(0, 0, 0, 120).getRGB());

        RenderUtils.drawImage((float) (this.getPosX() + 5), (float) (this.getPosY() + 5), 30, 30, albumImage, Color.white);

        this.drawString(songName, this.getPosX() + 40, this.getPosY() + 5, Color.white.getRGB(), this.customFont);
        this.drawString(newName, this.getPosX() + 40, this.getPosY() + this.getFontHeight(this.customFont) + 10, Color.white.getRGB(), this.customFont);

        //Draws the progress bar
        int progressPercentage = (int) (((double) songProgress / (double) songData.get("duration_ms").getAsInt()) * 145);
        progressPercentage = 145 - progressPercentage;
        progressPercentage = Math.max(0, progressPercentage);
        RenderUtils.drawRoundedRect((float) (this.getPosX() + 5), (float) (this.getPosY() + 42), (float) (this.getPosX() + 145), (float) (this.getPosY() + 38), 2, new Color(0, 0, 0, 120).getRGB());
        RenderUtils.drawRoundedRect((float) (this.getPosX() + 5), (float) (this.getPosY() + 42), (float) (this.getPosX() + 5 + progressPercentage), (float) (this.getPosY() + 38), 2, new Color(255, 255, 255, 255).getRGB());
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
