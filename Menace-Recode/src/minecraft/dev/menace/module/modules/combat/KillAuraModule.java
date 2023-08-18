package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.player.RayCastUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.*;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@DontSaveState
public class KillAuraModule extends BaseModule {

    MSTimer delayTimer = new MSTimer();
    public final ArrayList<EntityLivingBase> trget = new ArrayList<>();
    long delay;
    public boolean blocking;
    public boolean shouldFakeBlock;
    float[] lastRotations = new float[2];
    Random rand = new Random();

    //Switch
    int switchIndex;
    MSTimer switchTimer = new MSTimer();

    //Settings
    public SliderSetting reach;
    public SliderSetting minCPS;
    public SliderSetting maxCPS;
    ListSetting mode;
    SliderSetting switchDelay;
    SliderSetting switchAmount;
    SliderSetting ticksExisted;
    SliderSetting fov;
    ListSetting filter;
    ListSetting attackPoint;
    ListSetting autoblock;
    public ToggleSetting keepSprint;
    ToggleSetting noswing;
    ToggleSetting throughwalls;
    ToggleSetting ininv;
    ToggleSetting raycast;
    ToggleSetting cancelClick;
    ToggleSetting autoDisable;
    ToggleSetting players;
    ToggleSetting menaceUsers;
    ToggleSetting hostiles;
    ToggleSetting passives;
    ToggleSetting invisibles;

    public KillAuraModule() {
        super("KillAura", "Attacks targets for you", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        reach = new SliderSetting("Reach", true, 3, 1, 7, 0.1, false);
        minCPS = new SliderSetting("MinCPS", true, 10, 1, 20, true) {
            @Override
            public void constantCheck() {
                if (this.getValue() > Menace.instance.moduleManager.killAuraModule.maxCPS.getValue()) {
                    this.setValue(Menace.instance.moduleManager.killAuraModule.maxCPS.getValue());
                }
            }
        };
        maxCPS = new SliderSetting("MaxCPS", true, 10, 1, 20, true) {
            @Override
            public void constantCheck() {
                if (this.getValue() < Menace.instance.moduleManager.killAuraModule.minCPS.getValue()) {
                    this.setValue(Menace.instance.moduleManager.killAuraModule.minCPS.getValue());
                }
            }
        };
        mode = new ListSetting("Mode", true, "Single", new String[] {"Single", "Switch", "Multi"});
        switchDelay = new SliderSetting("SwitchDelay", true, 100, 0, 1000, true) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.killAuraModule.mode.getValue().equalsIgnoreCase("Switch"));
            }
        };
        switchAmount = new SliderSetting("SwitchAmount", true, 2, 2, 10, true) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.killAuraModule.mode.getValue().equalsIgnoreCase("Switch"));
            }
        };
        ticksExisted = new SliderSetting("TicksExisted", true, 10, 0 , 100, true);
        fov = new SliderSetting("FOV", true, 360, 0, 360, false);
        filter = new ListSetting("Filter", true, "Health", new String[] {"Health", "Distance", "Angle", "TicksExisted"});
        attackPoint = new ListSetting("AttackPoint", true, "Head", new String[] {"Head", "Eyes", "Body", "Cock", "Feet", "Closest"});
        autoblock = new ListSetting("AutoBlock", true, "None", new String[] {"None", "Fake", "Pre", "Post", "NoInteract", "Test"});
        keepSprint = new ToggleSetting("KeepSprint", true, true);
        noswing = new ToggleSetting("NoSwing", true, false);
        throughwalls = new ToggleSetting("ThroughWalls", true, false);
        ininv = new ToggleSetting("InInventory", true, false);
        raycast = new ToggleSetting("RayCast", true, false);
        cancelClick = new ToggleSetting("Cancel Click", true, false);
        autoDisable = new ToggleSetting("AutoDisable", true, true);
        players = new ToggleSetting("Players", true, true);
        menaceUsers = new ToggleSetting("MenaceUsers", true, false);
        hostiles = new ToggleSetting("Hostiles", true, true);
        passives = new ToggleSetting("Passives", true, false);
        invisibles = new ToggleSetting("Invisibles", true, false);
        this.rSetting(reach);
        this.rSetting(minCPS);
        this.rSetting(maxCPS);
        this.rSetting(mode);
        this.rSetting(switchDelay);
        this.rSetting(switchAmount);
        this.rSetting(ticksExisted);
        this.rSetting(fov);
        this.rSetting(filter);
        this.rSetting(attackPoint);
        this.rSetting(autoblock);
        this.rSetting(keepSprint);
        this.rSetting(noswing);
        this.rSetting(throughwalls);
        this.rSetting(ininv);
        this.rSetting(raycast);
        this.rSetting(cancelClick);
        this.rSetting(autoDisable);
        this.rSetting(players);
        this.rSetting(menaceUsers);
        this.rSetting(hostiles);
        this.rSetting(passives);
        this.rSetting(invisibles);
        super.setup();
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        switchTimer.reset();
        switchIndex = 0;
        delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
        lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        super.onEnable();
    }

    @Override
    public void onDisable() {
        shouldFakeBlock = false;
        unblock();
        trget.clear();
        super.onDisable();
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        if (autoDisable.getValue()) {
            ChatUtils.message("Toggled Killaura due to world change");
            this.toggle();
        }
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setDisplayName(mode.getValue());

        if (Menace.instance.moduleManager.scaffoldModule.isToggled()) {
            return;
        }

        List<EntityLivingBase> targets = filter(mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).map(EntityLivingBase.class::cast).collect(Collectors.toList()));

        Comparator<EntityLivingBase> comparator = null;
        switch (filter.getValue()) {
            case "Health":
                comparator = Comparator.comparingDouble(EntityLivingBase::getHealth);
                break;
            case "Distance":
                comparator = Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity));
                break;
            case "Angle":
                comparator = Comparator.comparingDouble(entity -> MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]));
                break;
            case "TicksExisted":
                comparator = Comparator.comparingInt(entity -> entity.ticksExisted);
                break;
        }

        targets.sort(comparator);

        if (!targets.isEmpty()) {
            trget.clear();
            trget.addAll(targets);

            EntityLivingBase target;
            if (mode.getValue().equals("Switch")) {
                if (switchTimer.hasTimePassed(switchDelay.getValueL())) {
                    switchIndex++;
                    switchTimer.reset();
                }
                if (switchIndex >= switchAmount.getValue() || switchIndex >= targets.size()) {
                    switchIndex = 0;
                }
                target = targets.get(switchIndex);
            } else {
                target = targets.get(0);
            }

            if (Menace.instance.moduleManager.chestStealerModule.isInChest) {
                PacketUtils.sendPacket(new C0DPacketCloseWindow(Menace.instance.moduleManager.chestStealerModule.guiChest.inventorySlots.windowId));
            }

            if (autoblock.getValue().equalsIgnoreCase("Fake")) {
                shouldFakeBlock = true;
            } else if (autoblock.getValue().equalsIgnoreCase("Pre")
                    || autoblock.getValue().equalsIgnoreCase("Test")
                    || autoblock.getValue().equalsIgnoreCase("NoInteract")) {
                block();
            }

            if (raycast.getValue()) {
                final MovingObjectPosition objectMouseOver = RayCastUtils.getMouseOver(reach.getValueF());
                assert objectMouseOver != null;
                if (target != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
                    target = (EntityLivingBase) objectMouseOver.entityHit;
                }
            }

            Vec3 attackPos = PlayerUtils.getCenter(target.getEntityBoundingBox());
            switch (attackPoint.getValue()) {
                case "Head":
                    attackPos = PlayerUtils.getHead(target.getEntityBoundingBox());
                    break;
                case "Eyes":
                    attackPos = PlayerUtils.getEyes(target);
                    break;
                case "Body":
                    attackPos = PlayerUtils.getCenter(target.getEntityBoundingBox());
                    break;
                case "Cock":
                    attackPos = PlayerUtils.getCock(target.getEntityBoundingBox());
                    break;
                case "Feet":
                    attackPos = PlayerUtils.getFeet(target.getEntityBoundingBox());
                    break;
                case "Closest":
                    attackPos = PlayerUtils.getClosestPoint(target.getEntityBoundingBox(), PlayerUtils.getEyesPos(), lastRotations);
                    break;
            }

            event.setYaw(lastRotations[0] = PlayerUtils.getFixedRotation(PlayerUtils.getRotations(attackPos), lastRotations)[0]);
            event.setPitch(lastRotations[1] = PlayerUtils.getFixedRotation(PlayerUtils.getRotations(attackPos), lastRotations)[1]);


            if (delayTimer.hasTimePassed(1000 / delay)) {

                if (autoblock.getValue().equalsIgnoreCase("Test")) {
                    unblock();
                }

                /*if (MathUtils.getAngleDifference(event.getYaw(), PlayerUtils.getRotations(target)[0]) > 15 || MathUtils.getAngleDifference(event.getPitch(), PlayerUtils.getRotations(target)[1]) > 15) {
                    return;
                }*/

                if (noswing.getValue()) {
                    PacketUtils.sendPacket(new C0APacketAnimation());
                } else {
                    mc.thePlayer.swingItem();
                }

                if (mode.getValue().equalsIgnoreCase("Multi")) {
                    for (EntityLivingBase entity : targets) {
                        mc.playerController.attackEntity(mc.thePlayer, entity);
                    }
                } else {
                    mc.playerController.attackEntity(mc.thePlayer, target);
                }
                delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
                delayTimer.reset();
            }
        } else {
            trget.clear();
            lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
            shouldFakeBlock = false;
            unblock();
        }
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        if (autoblock.getValue().equalsIgnoreCase("Post") && trget != null && !trget.isEmpty()) {
            block();
        }
    }

    @EventTarget
    public void onMouseClick(EventMouse event) {
        if (event.getButton() == 1 && cancelClick.getValue() && !trget.isEmpty()) {
            event.setCancelled(true);
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
        targets = targets.stream().filter(entity -> !Menace.instance.onlineMenaceUsers.containsValue(entity.getName()) || menaceUsers.getValue()).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> entity != Menace.instance.moduleManager.blinkModule.fp).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !(entity instanceof EntityPlayer && Menace.instance.moduleManager.backTrackerModule.fakePos.contains(entity))).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !isBot(entity)).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> !Menace.instance.friendManager.isFriend(entity.getName())).collect(Collectors.toList());
        
        return targets;
    }

    public double randomBetween(final double min, final double max) {
        return min + (rand.nextDouble() * (max - min));
    }

    private void block() {
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
        if (autoblock.getValue().equalsIgnoreCase("NoInteract")) {
            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), (int) (Math.random() * 100));
        }
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
        mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
        blocking = true;
    }

    private void unblock() {
        if (!blocking) return;
        mc.playerController.onStoppedUsingItem(mc.thePlayer);
        blocking = false;
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging && blocking) {
            blocking = false;
        }
    }

    private boolean isInFOV(Entity entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }

    private boolean isBot(Entity e) {
        AntiBotModule antiBotModule = Menace.instance.moduleManager.antiBotModule;
        return antiBotModule.isToggled() && e instanceof EntityPlayer && antiBotModule.bots.contains(e);
    }
}