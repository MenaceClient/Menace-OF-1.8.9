package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.WorldSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestAuraModule extends BaseModule {

    //EntityLivingBase target;
    Random rand = new Random();
    MSTimer delayTimer = new MSTimer();
    long delay = 0;
    float[] lastRotations = new float[2];

    public SliderSetting minCPS;
    public SliderSetting maxCPS;
    public SliderSetting reach;
    SliderSetting ticksExisted;
    SliderSetting fov;
    ToggleSetting throughwalls;
    ToggleSetting ininv;
    ToggleSetting players;
    ToggleSetting hostiles;
    ToggleSetting passives;
    ToggleSetting invisibles;

    public TestAuraModule() {
        super("TestAura",  Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        minCPS = new SliderSetting("MinCPS", true, 10, 1, 20, true) {
            @Override
            public void constantCheck() {
                if (this.getValue() > Menace.instance.moduleManager.testAuraModule.maxCPS.getValue()) {
                    this.setValue(Menace.instance.moduleManager.testAuraModule.maxCPS.getValue());
                }
            }
        };
        maxCPS = new SliderSetting("MaxCPS", true, 10, 1, 20, true) {
            @Override
            public void constantCheck() {
                if (this.getValue() < Menace.instance.moduleManager.testAuraModule.minCPS.getValue()) {
                    this.setValue(Menace.instance.moduleManager.testAuraModule.minCPS.getValue());
                }
            }
        };
        reach = new SliderSetting("Reach", true, 3, 1, 7, 0.1, false);
        fov = new SliderSetting("FOV", true, 360, 0, 360, false);
        ticksExisted = new SliderSetting("TicksExisted", true, 10, 0 , 100, true);
        throughwalls = new ToggleSetting("ThroughWalls", true, false);
        ininv = new ToggleSetting("InInventory", true, false);
        players = new ToggleSetting("Players", true, true);
        hostiles = new ToggleSetting("Hostiles", true, true);
        passives = new ToggleSetting("Passives", true, false);
        invisibles = new ToggleSetting("Invisibles", true, false);
        this.rSetting(minCPS);
        this.rSetting(maxCPS);
        this.rSetting(reach);
        this.rSetting(fov);
        this.rSetting(ticksExisted);
        this.rSetting(throughwalls);
        this.rSetting(ininv);
        this.rSetting(players);
        this.rSetting(hostiles);
        this.rSetting(passives);
        this.rSetting(invisibles);
        super.setup();
    }

    @Override
    public void onEnable() {
        //target = null;
        delayTimer.reset();
        delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
        lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {

        List<EntityLivingBase> targets = filter(mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).map(EntityLivingBase.class::cast).collect(Collectors.toList()));

        targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));

        if (!targets.isEmpty()) {
            EntityLivingBase target = targets.get(0);

            event.setYaw(PlayerUtils.getFixedRotation(PlayerUtils.getRotations2(target), lastRotations)[0]);
            event.setPitch(PlayerUtils.getFixedRotation(PlayerUtils.getRotations2(target), lastRotations)[1]);



            if (delayTimer.hasTimePassed(1000 / delay)) {
                mc.thePlayer.swingItem();
                PacketUtils.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
                delayTimer.reset();
            }
        } else {
            lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        }
    }

    public List<EntityLivingBase> filter(List<EntityLivingBase> targets) {
        targets = targets.stream().filter(entity -> entity != mc.thePlayer).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> mc.thePlayer.canEntityBeSeen(entity) || throughwalls.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !entity.isInvisible() || invisibles.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> entity.ticksExisted >= ticksExisted.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> isInFOV(entity, fov.getValue())).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> mc.currentScreen == null || ininv.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !(entity instanceof EntityPlayer) || players.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !(entity instanceof EntityMob) || hostiles.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !(entity instanceof EntityAnimal) || passives.getValue()).collect(Collectors.toList());
        return targets;
    }

    public double randomBetween(final double min, final double max) {
        return min + (rand.nextDouble() * (max - min));
    }

    private boolean isInFOV(Entity entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }


}
