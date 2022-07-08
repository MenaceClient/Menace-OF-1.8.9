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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
        startY = mc.thePlayer.posY - 1;
        oldSlot = mc.thePlayer.inventory.currentItem;
        swappedSlot = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (silentSwap.getValue() && swappedSlot != -1) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        } else {
            mc.thePlayer.inventory.currentItem = oldSlot;
        }
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mc.thePlayer.onGround && !MovementUtils.isMoving() && startY != mc.thePlayer.posY - 1) startY = mc.thePlayer.posY - 1;
        if (jump.getValue() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            mc.thePlayer.jump();
        }
        mc.thePlayer.setSprinting(sprint.getValue());
        if (rotation[0] != -69696969 && keepRotations.getValue()) {
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

        if ((mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)
                || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)mc.thePlayer.getHeldItem().getItem()).getBlock())) && swappedSlot == -1
                || !BlockUtils.getMaterial(belowPlayer).isReplaceable()) return;

        if (mc.gameSettings.keyBindJump.isKeyDown() && tower.getValue() && (!keepY.getValue() || !MovementUtils.isMoving())) {
            mc.thePlayer.motionY = 0.42;
        }
        placeBlockSimple(belowPlayer, event);
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
                        rotation = aimAtLocation(neighbor.getX(), neighbor.getY(), neighbor.getZ(), side2);
                        //rotation = PlayerUtils.getDirectionToBlock(neighbor.getX(), neighbor.getY(), neighbor.getZ(), side2);
                    } else {
                        event.setYaw(PlayerUtils.getDirectionToBlock(neighbor.getX(), neighbor.getY(), neighbor.getZ(), side2)[0]);
                        event.setPitch(PlayerUtils.getDirectionToBlock(neighbor.getX(), neighbor.getY(), neighbor.getZ(), side2)[1]);
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

                    mc.rightClickDelayTimer = 4;
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
        BlockPos belowPlayer = new BlockPos(mc.thePlayer).down();
        if (keepY.getValue() && MovementUtils.isMoving()) {
            belowPlayer = new BlockPos(mc.thePlayer.posX, startY, mc.thePlayer.posZ);
        }
        RenderUtils.drawBlock(belowPlayer, Color.RED, 3);
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (!render.getValue()) return;

    }

    private float @NotNull [] aimAtLocation(final double x, final double y, final double z, final @NotNull EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball(mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y - 2.7035252353;
        temp.posZ = z + 0.5;
        final EntitySnowball entitySnowball10;
        entitySnowball10 = temp;
        entitySnowball10.posX += facing.getDirectionVec().getX() * 0.25;
        final EntitySnowball entitySnowball11;
        entitySnowball11 = temp;
        entitySnowball11.posY += facing.getDirectionVec().getY() * 0.25;
        final EntitySnowball entitySnowball12;
        entitySnowball12 = temp;
        entitySnowball12.posZ += facing.getDirectionVec().getZ() * 0.25;
        return this.aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }

    @Contract("_, _, _ -> new")
    private float @NotNull [] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - mc.thePlayer.posX;
        final double y = positionY - mc.thePlayer.posY;
        final double z = positionZ - mc.thePlayer.posZ;
        final double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[] { (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, (float)(-(Math.atan2(y, distance) * 180.0 / 3.141592653589793)) };
    }

}
