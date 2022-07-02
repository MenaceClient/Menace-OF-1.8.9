package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.Event2D;
import dev.menace.event.events.Event3D;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.InventoryUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.awt.*;

@DontSaveState
public class ScaffoldModule extends BaseModule {

    float[] rotation = new float[2];
    double startY;
    int oldSlot;
    int  swappedSlot;

    ToggleSetting tower;
    public ToggleSetting sprint;
    ToggleSetting keepRotations;
    ToggleSetting silentSwap;
    ToggleSetting keepY;
    ToggleSetting jump;
    ToggleSetting noSwing;
    ToggleSetting render;

    public ScaffoldModule() {
        super("Scaffold", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        tower = new ToggleSetting("Tower", true, true);
        sprint = new ToggleSetting("Sprint", true, false);
        keepRotations = new ToggleSetting("KeepRotations", true, true);
        silentSwap = new ToggleSetting("SilentSwap", true, false);
        keepY = new ToggleSetting("KeepY", true, false);
        jump = new ToggleSetting("Jump", true, false);
        noSwing = new ToggleSetting("NoSwing", true, false);
        render = new ToggleSetting("Render", true, true);
        this.rSetting(tower);
        this.rSetting(sprint);
        this.rSetting(keepRotations);
        this.rSetting(silentSwap);
        this.rSetting(keepY);
        this.rSetting(jump);
        this.rSetting(noSwing);
        this.rSetting(render);
        super.setup();
    }

    @Override
    public void onEnable() {
        rotation[0] = -69696969;
        startY = MC.thePlayer.posY - 1;
        oldSlot = MC.thePlayer.inventory.currentItem;
        swappedSlot = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (silentSwap.getValue() && swappedSlot != -1) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(MC.thePlayer.inventory.currentItem));
        } else {
            MC.thePlayer.inventory.currentItem = oldSlot;
        }
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (MC.thePlayer.onGround && !MovementUtils.isMoving() && startY != MC.thePlayer.posY - 1) startY = MC.thePlayer.posY - 1;
        if (jump.getValue() && MC.thePlayer.onGround && MovementUtils.isMoving()) {
            MC.thePlayer.jump();
        }
        MC.thePlayer.setSprinting(sprint.getValue());
        if (rotation[0] != -69696969 && keepRotations.getValue()) {
            event.setYaw(rotation[0]);
            event.setPitch(rotation[1]);
        }
        BlockPos belowPlayer = new BlockPos(MC.thePlayer).down();
        if (keepY.getValue() && MovementUtils.isMoving()) {
            belowPlayer = new BlockPos(MC.thePlayer.posX, startY, MC.thePlayer.posZ);
        }

        if ((silentSwap.getValue() && (swappedSlot == -1 || MC.thePlayer.inventory.getStackInSlot(swappedSlot) == null)) ||
                (!silentSwap.getValue() && (MC.thePlayer.getHeldItem() == null || !(MC.thePlayer.getHeldItem().getItem() instanceof ItemBlock)))) {
            for (int i = 0; i < 9; ++i) {
                ItemStack s = MC.thePlayer.inventory.getStackInSlot(i);
                if (s != null && s.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)s.getItem()).getBlock())) {
                    if (silentSwap.getValue()) {
                        swappedSlot = i;
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(swappedSlot));
                    } else {
                        MC.thePlayer.inventory.currentItem = i;
                    }
                    return;
                }
            }
        }

        if ((MC.thePlayer.getHeldItem() == null || !(MC.thePlayer.getHeldItem().getItem() instanceof ItemBlock)
                || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)MC.thePlayer.getHeldItem().getItem()).getBlock())) && swappedSlot == -1
                || !BlockUtils.getMaterial(belowPlayer).isReplaceable()) return;

        if (MC.gameSettings.keyBindJump.isKeyDown() && tower.getValue() && (!keepY.getValue() || !MovementUtils.isMoving())) {
            MC.thePlayer.motionY = 0.42;
        }
        placeBlockSimple(belowPlayer, event);
    }

    public void placeBlockSimple(final BlockPos pos, EventPreMotion event) {
        final Vec3 eyesPos = new Vec3(MC.thePlayer.posX, MC.thePlayer.posY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockUtils.getBlock(neighbor).canCollideCheck(MC.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {

                    if (keepRotations.getValue()) {
                        rotation = PlayerUtils.getRotations(neighbor);
                    } else {
                        event.setYaw(PlayerUtils.getRotations(neighbor)[0]);
                        event.setPitch(PlayerUtils.getRotations(neighbor)[1]);
                    }

                    if (silentSwap.getValue() && swappedSlot != -1) {
                        MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.inventory.getStackInSlot(swappedSlot), neighbor, side2, hitVec);
                    } else {
                        MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                    }

                    if (noSwing.getValue()) {
                        PacketUtils.sendPacket(new C0APacketAnimation());
                    } else {
                        MC.thePlayer.swingItem();
                    }

                    MC.rightClickDelayTimer = 4;
                }
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (silentSwap.getValue() && event.getPacket() instanceof C09PacketHeldItemChange) {
            event.cancel();
        }
    }

    @EventTarget
    public void on3D(Event3D event) {
        if (!render.getValue()) return;
        BlockPos belowPlayer = new BlockPos(MC.thePlayer).down();
        if (keepY.getValue() && MovementUtils.isMoving()) {
            belowPlayer = new BlockPos(MC.thePlayer.posX, startY, MC.thePlayer.posZ);
        }
        RenderUtils.drawBlock(belowPlayer, Color.RED, 3);
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (!render.getValue()) return;

    }

}
