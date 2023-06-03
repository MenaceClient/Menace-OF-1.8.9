package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.InventoryUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

@DontSaveState
public class ScaffoldModule extends BaseModule {

    float[] rotation = new float[2];
    double startY;
    int oldSlot;
    int  swappedSlot;
    float derpYaw;
    boolean isTowering;
    int towerHeight;

    MSTimer eagleTimer = new MSTimer();

    public ToggleSetting tower;
    ToggleSetting towerMove;
    ListSetting towerMode;
    SliderSetting towerSpeed;
    ToggleSetting towerDerp;
    public ToggleSetting sprint;
    ToggleSetting keepRotations;
    ToggleSetting silentSwap;
    ToggleSetting keepY;
    ToggleSetting bypass;
    ListSetting rotationMode;
    ToggleSetting blocksMC;
    public ToggleSetting eagle;
    SliderSetting eagleDelay;
    ToggleSetting clampPitch;
    ToggleSetting notOnBlock;
    ToggleSetting noSwing;
    public ToggleSetting boost;
    public ToggleSetting jump;
    ToggleSetting lowhop;
    ListSetting lowhopMode;
    SliderSetting timer;
    ToggleSetting render;

    public ScaffoldModule() {
        super("Scaffold", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        tower = new ToggleSetting("Tower", true, true);
        towerMove = new ToggleSetting("Tower Move", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.tower.getValue());
                super.constantCheck();
            }
        };
        towerMode = new ListSetting("Tower Mode", true, "Normal", new String[]{"Normal", "BlocksMC"}) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.tower.getValue());
                super.constantCheck();
            }
        };
        towerSpeed = new SliderSetting("Tower Speed", true, 42, 10, 50, true) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.tower.getValue() && Menace.instance.moduleManager.scaffoldModule.towerMode.getValue().equalsIgnoreCase("Normal"));
                super.constantCheck();
            }
        };
        towerDerp = new ToggleSetting("Tower Derp", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.tower.getValue());
                super.constantCheck();
            }
        };
        sprint = new ToggleSetting("Sprint", true, false);
        silentSwap = new ToggleSetting("SilentSwap", true, false);
        bypass = new ToggleSetting("Bypass", true, false);
        rotationMode = new ListSetting("Rotation Mode", true, "Normal1", new String[]{"Normal1", "Normal2"}) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };

        keepRotations = new ToggleSetting("KeepRotations", true, true) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };
        blocksMC = new ToggleSetting("BlocksMC", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };
        eagle = new ToggleSetting("Eagle", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };
        eagleDelay = new SliderSetting("Eagle Delay", false, 200, 100, 1000, 100, true) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.eagle.getValue() && bypass.getValue());
                super.constantCheck();
            }
        };
        clampPitch = new ToggleSetting("ClampPitch", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };
        notOnBlock = new ToggleSetting("NotOnBlock", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(bypass.getValue());
                super.constantCheck();
            }
        };
        keepY = new ToggleSetting("KeepY", true, false);
        noSwing = new ToggleSetting("NoSwing", true, false);
        boost = new ToggleSetting("Boost", true, false);
        jump = new ToggleSetting("Jump", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.boost.getValue());
                super.constantCheck();
            }
        };
        lowhop = new ToggleSetting("LowHop", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.boost.getValue()
                    && Menace.instance.moduleManager.scaffoldModule.jump.getValue());
                super.constantCheck();
            }
        };
        lowhopMode = new ListSetting("Mode", true, "Verus", new String[]{"Verus", "NCP"}) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.boost.getValue()
                        && Menace.instance.moduleManager.scaffoldModule.jump.getValue()
                        && Menace.instance.moduleManager.scaffoldModule.lowhop.getValue());
                super.constantCheck();
            }
        };
        timer = new SliderSetting("Timer", true, 1, 1, 5, 0.1, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.boost.getValue());
                super.constantCheck();
            }
        };
        render = new ToggleSetting("Render", true, true);
        this.rSetting(tower);
        this.rSetting(towerMove);
        this.rSetting(towerMode);
        this.rSetting(towerSpeed);
        this.rSetting(towerDerp);
        this.rSetting(sprint);
        this.rSetting(silentSwap);
        this.rSetting(keepY);
        this.rSetting(bypass);
        this.rSetting(rotationMode);
        this.rSetting(keepRotations);
        this.rSetting(blocksMC);
        this.rSetting(eagle);
        this.rSetting(eagleDelay);
        this.rSetting(clampPitch);
        this.rSetting(notOnBlock);
        this.rSetting(noSwing);
        this.rSetting(boost);
        this.rSetting(jump);
        this.rSetting(lowhop);
        this.rSetting(lowhopMode);
        this.rSetting(timer);
        this.rSetting(render);
        super.setup();
    }

    @Override
    public void onEnable() {
        rotation[0] = -69696969;
        startY = mc.thePlayer.posY - 1;
        oldSlot = mc.thePlayer.inventory.currentItem;
        swappedSlot = -1;
        eagleTimer.reset();
        derpYaw = 0;
        isTowering = false;
        towerHeight = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (silentSwap.getValue() && swappedSlot != -1) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        } else {
            mc.thePlayer.inventory.currentItem = oldSlot;
        }
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mc.thePlayer.onGround && !MovementUtils.isMoving() && startY != mc.thePlayer.posY - 1) startY = mc.thePlayer.posY - 1;
        mc.thePlayer.setSprinting(sprint.getValue());
        if (towerDerp.getValue() && isTowering) {
            event.setYaw(derpYaw);
            event.setPitch(90);
            derpYaw += 100f;
        } else if (rotation[0] != -69696969 && keepRotations.getValue()) {

            if (blocksMC.getValue()) {
                double randomYaw = Math.random() * 20 - 10;
                event.setYaw((float) randomYaw + rotation[0]);
                event.setPitch(MathUtils.clamp((float) (randomYaw + rotation[1]), -90, 90));
            } else {
                if (clampPitch.getValue()) {
                    event.setYaw(rotation[0]);
                    event.setPitch(MathUtils.clamp(rotation[1], -90, 90));
                } else {
                    event.setYaw(rotation[0]);
                    event.setPitch(rotation[1]);
                }
            }
        }

        BlockPos belowPlayer = new BlockPos(mc.thePlayer).down();
        if (keepY.getValue() && MovementUtils.isMoving()) {
            belowPlayer = new BlockPos(mc.thePlayer.posX, startY, mc.thePlayer.posZ);
        }

        if ((silentSwap.getValue() && (swappedSlot == -1 || mc.thePlayer.inventory.getStackInSlot(swappedSlot) == null)) ||
                (!silentSwap.getValue() && (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)))) {
            for (int i = 0; i < 9; ++i) {
                ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
                if (s != null && s.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)s.getItem()).getBlock())) {
                    if (silentSwap.getValue()) {
                        swappedSlot = i;
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(swappedSlot));
                    } else {
                        mc.thePlayer.inventory.currentItem = i;
                    }
                    return;
                }
            }
        }

        //Boost
        if (!lowhop.getValue() || !lowhopMode.getValue().equalsIgnoreCase("NCP")) {
            mc.timer.timerSpeed = timer.getValueF();
        }
        if (jump.getValue() && !lowhop.getValue() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            mc.thePlayer.jump();
        } else if (jump.getValue() && lowhop.getValue() && lowhopMode.getValue().equalsIgnoreCase("NCP") && MovementUtils.shouldMove()) {
            if (mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 0.95f;
                mc.thePlayer.jump();
                if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                        MovementUtils.strafe(0.5893f);
                    } else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                        MovementUtils.strafe(0.6893f);
                    }
                } else {
                    MovementUtils.strafe(0.485f);
                }
            } else if (mc.thePlayer.motionY < 0.16 && mc.thePlayer.motionY > 0.0) {
                mc.thePlayer.motionY = -0.1;
                mc.timer.timerSpeed = 1.2F;
            }
            MovementUtils.strafe();
        }

        if (mc.gameSettings.keyBindJump.isKeyDown() && tower.getValue() && ((!keepY.getValue() && towerMove.getValue()) || !MovementUtils.isMoving()) && towerMode.getValue().equalsIgnoreCase("Normal")) {
            isTowering = true;
            mc.thePlayer.motionY = towerSpeed.getValue() / 100D;
        } else if (towerMode.getValue().equalsIgnoreCase("Normal")) {
            isTowering = false;
        }

        if ((mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)
                || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)mc.thePlayer.getHeldItem().getItem()).getBlock())) && swappedSlot == -1
                || !BlockUtils.getMaterial(belowPlayer).isReplaceable()) return;

        if (mc.gameSettings.keyBindJump.isKeyDown() && tower.getValue() && ((!keepY.getValue() && towerMove.getValue()) || !MovementUtils.isMoving()) && towerMode.getValue().equalsIgnoreCase("BlocksMC")) {
            isTowering = true;
            if (towerHeight >= 10) {
                towerHeight = 0;
                mc.thePlayer.motionY = 0;
            } else {
                mc.thePlayer.motionY = 0.42;
            }
        } else if (towerMode.getValue().equalsIgnoreCase("BlocksMC")){
            isTowering = false;
        }

        if (eagleTimer.hasTimePassed(eagleDelay.getValueL()) && eagle.getValue()) {
            eagleTimer.reset();
            mc.gameSettings.keyBindSneak.pressed = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mc.gameSettings.keyBindSneak.pressed = false;
                    super.run();
                }
            }.start();
        }

        findNearbyBlocks(belowPlayer, event);
    }

    public void findNearbyBlocks(BlockPos pos, EventPreMotion event) {

        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            if (BlockUtils.getMaterial(pos).isReplaceable() && BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                placeBlockSimple(pos, event);
                return;
            }
        }

        for (int amt = 1; amt < 4; amt++) {
            for (int sides = EnumFacing.values().length, side_ = 0; side_ < sides; side_++) {
                BlockPos newPos = pos.offset(EnumFacing.values()[side_], amt);
                EnumFacing[] values2;
                for (int length = (values2 = EnumFacing.values()).length, i = 0; i < length; ++i) {
                    final EnumFacing side = values2[i];
                    final BlockPos neighbor = newPos.offset(side);
                    if (BlockUtils.getMaterial(newPos).isReplaceable() && BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                        placeBlockSimple(newPos, event);
                        return;
                    }
                }
            }
        }
    }

    public void placeBlockSimple(final BlockPos pos, EventPreMotion event) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                //Thanks Geuxy!!
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0 && (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -0.0001, 0)).isEmpty() || !notOnBlock.getValue())) {

                    if (keepRotations.getValue()) {
                        if (rotationMode.getValue().equalsIgnoreCase("Normal1")) {
                            rotation = PlayerUtils.getRotations(hitVec);
                        } else if (rotationMode.getValue().equalsIgnoreCase("Normal2")) {
                            rotation = PlayerUtils.getRotsNew(neighbor, side2);
                        }
                    } else {
                        if (rotationMode.getValue().equalsIgnoreCase("Normal1")) {
                            if (clampPitch.getValue()) {
                                event.setPitch(MathUtils.clamp(PlayerUtils.getRotations(hitVec)[1], -90F, 90F));
                            } else {
                                event.setPitch(PlayerUtils.getRotations(hitVec)[1]);
                            }
                            event.setYaw(PlayerUtils.getRotations(hitVec)[0]);
                        } else if (rotationMode.getValue().equalsIgnoreCase("Normal2")) {
                            if (clampPitch.getValue()) {
                                event.setPitch(MathUtils.clamp(PlayerUtils.getRotsNew(neighbor, side2)[1], -90F, 90F));
                            } else {
                                event.setPitch(PlayerUtils.getRotsNew(neighbor, side2)[1]);
                            }
                            event.setYaw(PlayerUtils.getRotsNew(neighbor, side2)[0]);
                        }

                    }



                    if (silentSwap.getValue() && swappedSlot != -1) {
                        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(swappedSlot), neighbor, side2, hitVec);
                    } else {
                        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                    }

                    if (noSwing.getValue()) {
                        PacketUtils.sendPacket(new C0APacketAnimation());
                    } else {
                        mc.thePlayer.swingItem();
                    }

                    if (isTowering && towerMode.getValue().equalsIgnoreCase("BlocksMC")) {
                        towerHeight++;
                    }

                    mc.rightClickDelayTimer = 4;
                }
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (jump.getValue() && lowhop.getValue() && lowhopMode.getValue().equalsIgnoreCase("Verus") && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            mc.thePlayer.jump();
            mc.thePlayer.motionY = 0;
            event.setY(0.3);
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (silentSwap.getValue() && event.getPacket() instanceof C09PacketHeldItemChange) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(swappedSlot));
            event.cancel();
        } else if (silentSwap.getValue() && event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            ((C08PacketPlayerBlockPlacement) event.getPacket()).setStack(mc.thePlayer.inventory.getStackInSlot(swappedSlot));
        }
    }

    @EventTarget
    public void on3D(EventRender3D event) {
        if (!render.getValue()) return;
        BlockPos belowPlayer = new BlockPos(mc.thePlayer).down();
        if (keepY.getValue() && MovementUtils.isMoving()) {
            belowPlayer = new BlockPos(mc.thePlayer.posX, startY, mc.thePlayer.posZ);
        }
        RenderUtils.drawBlock(belowPlayer, Color.RED, 3);
    }

    @EventTarget
    public void on2D(EventRender2D event) {
        if (!render.getValue()) return;

    }

}
