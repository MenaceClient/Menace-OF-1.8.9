package dev.menace.module.modules.world;

import java.util.ArrayList;
import java.util.Objects;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Fucker extends Module {
	
	private float dmg;
    private BlockPos blockPos;
    private Vec3 respawnPoint;
    
    //Settings
    DoubleSetting rangeSet;
    BoolSetting swingSet;
    BoolSetting cakeSet;
    BoolSetting bedSet;
    BoolSetting ownBedSet;
    BoolSetting mineplexSet;
    BoolSetting rotationSet;
    
	public Fucker() {
		super("BedFucker", 0, Category.WORLD);
	}
	
	@Override
	public void setup() {
		rangeSet = new DoubleSetting("Range", this, 4, 1, 7);
		swingSet = new BoolSetting("Swing", this, true);
		cakeSet = new BoolSetting("Cake", this, true);
		bedSet = new BoolSetting("Bed", this, true);
		ownBedSet = new BoolSetting("Whitelist own Bed", this, true);
		mineplexSet = new BoolSetting("Mineplex Through Walls", this, false);
		rotationSet = new BoolSetting("Rotations", this, true);
		this.rSetting(rangeSet);
		this.rSetting(swingSet);
        this.rSetting(cakeSet);
        this.rSetting(bedSet);
        this.rSetting(ownBedSet);
        this.rSetting(mineplexSet);
        this.rSetting(rotationSet);
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		double range = rangeSet.getValue();
		boolean swing = swingSet.isChecked();
		boolean cake = cakeSet.isChecked();
		boolean bed = bedSet.isChecked();
		boolean whitelistOwnBase = ownBedSet.isChecked();
		boolean breakAbove = mineplexSet.isChecked();
		boolean rotationss = rotationSet.isChecked();
		
		blockPos = null;
        if (MC.gameSettings.keyBindAttack.isKeyDown()) return;
        //Cake
        ArrayList<Integer> pos = getBlock(Blocks.cake, (int) range);

        if (cake) {
            if (pos != null && MC.thePlayer != null && (respawnPoint == null || MC.thePlayer.getDistance(respawnPoint.xCoord, respawnPoint.yCoord, respawnPoint.zCoord) > 25 || !whitelistOwnBase)) {
                final BlockPos pos2 = new BlockPos(MC.thePlayer.posX + pos.get(0), MC.thePlayer.posY + pos.get(1), MC.thePlayer.posZ + pos.get(2));
                final BlockPos posAbove = new BlockPos(MC.thePlayer.posX + pos.get(0), MC.thePlayer.posY + pos.get(1) + 1, MC.thePlayer.posZ + pos.get(2));
                final BlockPos currentPos = pos2.add(pos.get(0), pos.get(1), pos.get(2));

                if (rotationss) {
                    final float[] rotations = Rotations.getRotations(blockPos);
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                }

                if (breakAbove) {
                    MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                    MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                }

                if (!Objects.requireNonNull(Menace.instance.moduleManager.flightModule.isToggled()))
                    MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, MC.thePlayer.getHeldItem(), pos2, EnumFacing.UP, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));

                if (swing) MC.thePlayer.swingItem();
            }
        }

        //Bed
        if (blockPos == null || MC.theWorld.getBlockState(blockPos).getBlock() != Block.getBlockById(26) && MC.thePlayer != null) {
            pos = getBlock(Blocks.bed, (int) range);

            if (pos != null)
                blockPos = new BlockPos(MC.thePlayer.posX + Objects.requireNonNull(pos).get(0), MC.thePlayer.posY + Objects.requireNonNull(pos).get(1), MC.thePlayer.posZ + Objects.requireNonNull(pos).get(2));
        }

        if (bed) {
            if (blockPos != null && (respawnPoint == null || MC.thePlayer.getDistance(respawnPoint.xCoord, respawnPoint.yCoord, respawnPoint.zCoord) > 25 || !whitelistOwnBase)) {
                if (breakAbove) {
                    final BlockPos posAbove = new BlockPos(MC.thePlayer.posX + blockPos.getX(), MC.thePlayer.posY + blockPos.getY() + 1, MC.thePlayer.posZ + blockPos.getZ());
                    MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                    MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posAbove, EnumFacing.NORTH));
                }

                if (rotationss) {
                    final float[] rotations = Rotations.getRotations(blockPos);
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                } else if (PlayerUtils.isOnServer("hypixel")) {
                    event.setYaw((float) (Math.random() * 360));
                    event.setPitch((float) (-90 + Math.random() * 180));
                }

                if (dmg == 0) {
                    MC.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                    if (PlayerUtils.isOnServer("hypixel")) {
                    	MC.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                        MC.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                    }

                    if (MC.theWorld.getBlockState(blockPos).getBlock().getPlayerRelativeBlockHardness(MC.thePlayer, MC.theWorld, blockPos) >= 1) {
                        if (swing) MC.thePlayer.swingItem();

                        MC.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

                        dmg = 0;
                        blockPos = null;
                        return;
                    }
                }

                if (swing) MC.thePlayer.swingItem();

                dmg += MC.theWorld.getBlockState(blockPos).getBlock().getPlayerRelativeBlockHardness(MC.thePlayer, MC.theWorld, blockPos);
                MC.theWorld.sendBlockBreakProgress(MC.thePlayer.getEntityId(), blockPos, (int) (dmg * 10) - 1);

                if (dmg >= 1) {
                	MC.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                    MC.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

                    dmg = 0;
                    blockPos = null;
                }
            }
        }
    }

    @EventTarget
    public void onPacketReceive(final EventReceivePacket event) {
		Boolean whitelistOwnBase = ownBedSet.isChecked();
        if (!whitelistOwnBase) return;

        final Packet<?> p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) p;
            final double x = s08.getX();
            final double y = s08.getY();
            final double z = s08.getZ();

            if (MC.thePlayer.getDistance(x, y, z) > 40) {
                respawnPoint = new Vec3(x, y, z);
            }
        }
	}
    
    public ArrayList<Integer> getBlock(final Block b, final int r) {
        final ArrayList<Integer> pos = new ArrayList<>();

        for (int x = -r; x < r; ++x) {
            for (int y = r; y > -r; --y) {
                for (int z = -r; z < r; ++z) {
                    if (MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX + x, MC.thePlayer.posY + y, MC.thePlayer.posZ + z)).getBlock() == b) {
                        pos.add(x);
                        pos.add(y);
                        pos.add(z);
                        return pos;
                    }
                }
            }
        }
        return null;
    }
}
