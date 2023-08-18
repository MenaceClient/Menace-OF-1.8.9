package dev.menace.ui.hud.elements;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import dev.menace.Menace;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardElement extends BaseElement {

    int width = 0;
    int height = 0;
    private boolean customFont;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                ScoreboardElement.this.customFont = this.getValue();
            }
        });
    }

    @Override
    public void render() {
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective objective = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (objective == null) {
            return;
        }

        //Render scoreboard
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (list.size() > 15)
        {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        }
        else
        {
            collection = list;
        }

        int width = this.getStringWidth(objective.getDisplayName(), this.customFont);

        for (Score score : collection)
        {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName());
            width = Math.max(width, this.getStringWidth(s, this.customFont));
        }

        int height = collection.size() * this.getFontHeight(this.customFont) + this.getFontHeight(this.customFont);

        this.width = width + 4;
        this.height = height + 6;

        RenderUtils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, 0x60000000);

        int yOff = this.getFontHeight(this.customFont) + 4;

        //Flip the collection so it renders from the bottom up
        collection = Lists.reverse(Lists.newArrayList(collection));

        for (Score score : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String[] s = {ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName())};

            Menace.instance.onlineMenaceUsers.forEach((username, ign) -> {
                if (ign != null && s[0].contains(ign)) {
                    s[0] = s[0].replace(ign, ign + " §r(§b" + username + "§r)");
                }
            });

            //SecurityFeatures
            if (Menace.instance.moduleManager.securityFeaturesModule.isToggled()) {
                s[0] = s[0].replaceAll(mc.thePlayer.getName(), Menace.instance.user.getUsername());
            }

            String points = EnumChatFormatting.RED + "" + score.getScorePoints();

            this.drawString(s[0], this.getPosX() + 2, this.getPosY() + yOff + 2, -1, this.customFont);
            this.drawString(points, this.getPosX() + this.width - this.getStringWidth(points, this.customFont) - 2, this.getPosY() + yOff + 2, Color.red.getRGB(), this.customFont);

            yOff += this.getFontHeight(this.customFont);
        }

        String displayName = objective.getDisplayName();
        this.drawString(displayName, this.getPosX() + this.width / 2 - this.getStringWidth(displayName, this.customFont) / 2, this.getPosY() + 2, -1, this.customFont);

    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
