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
    ToggleSetting jump;
    public ToggleSetting eagle;
    SliderSetting eagleDelay;
    ToggleSetting noSwing;
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
        keepRotations = new ToggleSetting("KeepRotations", true, true);
        silentSwap = new ToggleSetting("SilentSwap", true, false);
        keepY = new ToggleSetting("KeepY", true, false);
        jump = new ToggleSetting("Jump", true, false);
        eagle = new ToggleSetting("Eagle", true, false);
        eagleDelay = new SliderSetting("Eagle Delay", false, 200, 100, 1000, 100, true) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.scaffoldModule.eagle.getValue());
                super.constantCheck();
            }
        };
        noSwing = new ToggleSetting("NoSwing", true, false);
        timer = new SliderSetting("Timer", true, 1, 1, 5, 0.1, false);
        render = new ToggleSetting("Render", true, true);
        this.rSetting(tower);
        this.rSetting(towerMove);
        this.rSetting(towerMode);
        this.rSetting(towerSpeed);
        this.rSetting(towerDerp);
        this.rSetting(sprint);
        this.rSetting(keepRotations);
        this.rSetting(silentSwap);
        this.rSetting(keepY);
        this.rSetting(jump);
        this.rSetting(eagle);
        this.rSetting(eagleDelay);
        this.rSetting(noSwing);
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
    public void onUpdate(EventUpdate event) {
        mc.timer.timerSpeed = timer.getValueF();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mc.thePlayer.onGround && !MovementUtils.isMoving() && startY != mc.thePlayer.posY - 1) startY = mc.thePlayer.posY - 1;
        if (jump.getValue() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            mc.thePlayer.jump();
        }
        mc.thePlayer.setSprinting(sprint.getValue());
        if (towerDerp.getValue() && isTowering) {
            event.setYaw(derpYaw);
            event.setPitch(90);
            derpYaw += 100f;
        } else if (rotation[0] != -69696969 && keepRotations.getValue()) {
            event.setYaw(rotation[0]);
            event.setPitch(rotation[1]);
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

    public void placeBlockSimple(final BlockPos pos, EventPreMotion event) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {

                    if (keepRotations.getValue()) {
                        rotation = getRotsNew(neighbor, side2);
                    } else {
                        event.setYaw(getRotsNew(neighbor, side2)[0]);
                        event.setPitch(MathUtils.clamp(getRotsNew(neighbor, side2)[1], -90F, 90F));
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

    public void findNearbyBlocks(@NotNull BlockPos pos, EventPreMotion event) {

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

    public float[] getRotsNew(final BlockPos pos, final EnumFacing facing) {
        final float yaw = this.getYaw(pos, facing);
        final float[] rots2 = this.getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), facing);
        return new float[] { (float)(yaw + ThreadLocalRandom.current().nextDouble(-1.0, 1.0)), mc.thePlayer.onGround ? 80.31f : Math.min(90.0f, rots2[1]) };
    }

    public float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg(this.mc.theWorld);
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        var4.posX += var3.getDirectionVec().getX() * 0.25;
        var4.posY += var3.getDirectionVec().getY() * 0.25;
        var4.posZ += var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity(var4);
    }

    public float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + mc.thePlayer.rotationYaw, getPitch(var0) + mc.thePlayer.rotationPitch };
    }

    public float getYaw(final @NotNull Entity var0) {
        final double var = var0.posX - mc.thePlayer.posX;
        final double var2 = var0.posZ - mc.thePlayer.posZ;
        final double degrees = Math.toDegrees(Math.atan(var2 / var));
        double var3;
        if (var2 < 0.0 && var < 0.0) {
            var3 = 90.0 + degrees;
        }
        else if (var2 < 0.0 && var > 0.0) {
            var3 = -90.0 + degrees;
        }
        else {
            var3 = Math.toDegrees(-Math.atan(var / var2));
        }
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)var3));
    }

    public float getPitch(final @NotNull Entity var0) {
        final double var = var0.posX - mc.thePlayer.posX;
        final double var2 = var0.posZ - mc.thePlayer.posZ;
        final double var3 = var0.posY - 1.6 + var0.getEyeHeight() - mc.thePlayer.posY;
        final double var4 = MathHelper.sqrt_double(var * var + var2 * var2);
        final double var5 = -Math.toDegrees(Math.atan(var3 / var4));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)var5);
    }

    public float getYaw(final @NotNull BlockPos block, final @NotNull EnumFacing face) {
        final Vec3 vecToModify = new Vec3(block.getX(), block.getY(), block.getZ());
        switch (face) {
            case EAST:
            case WEST: {
                vecToModify.zCoord += 0.5;
                break;
            }
            case SOUTH:
            case NORTH: {
                vecToModify.xCoord += 0.5;
                break;
            }
            case UP:
            case DOWN: {
                vecToModify.xCoord += 0.5;
                vecToModify.zCoord += 0.5;
                break;
            }
        }
        final double x = vecToModify.xCoord - this.mc.thePlayer.posX;
        final double z = vecToModify.zCoord - this.mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        if (yaw < 0.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }

}
