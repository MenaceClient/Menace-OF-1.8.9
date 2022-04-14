package dev.menace.utils.entity.self;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventUpdate;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MiscUtils;
import dev.menace.utils.misc.ServerUtils;

public class PlayerUtils {
	
	static Minecraft MC = Menace.instance.MC;
	static Rotations rotations = new Rotations();
	private static long groundTimer = 0;

	private double lastX = 0.0;
	private double lastY = 0.0;
	private double lastZ = 0.0;
	public static double bps;
	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public static void tpToEnt(final Entity entity) {
        double curX = MC.thePlayer.posX;
        double curY = MC.thePlayer.posY;
        double curZ = MC.thePlayer.posZ;
        final double endX = entity.getPosition().getX();
        final double endY = entity.getPosition().getY();
        final double endZ = entity.getPosition().getZ();
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            final boolean next = false;
            final double diffX = curX - endX;
            final double diffY = curY - endY;
            final double diffZ = curZ - endZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
	
	public static void tpToPos(final BlockPos pos) {
        double curX = MC.thePlayer.posX;
        double curY = MC.thePlayer.posY;
        double curZ = MC.thePlayer.posZ;
        final double endX = pos.getX();
        final double endY = pos.getY();
        final double endZ = pos.getZ();
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            final boolean next = false;
            final double diffX = curX - endX;
            final double diffY = curY - endY;
            final double diffZ = curZ - endZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
	
    public static boolean isInLiquid() {
        for(int x = MathHelper.floor_double(MC.thePlayer.boundingBox.minY); x < MathHelper.floor_double(MC.thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(MC.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(MC.thePlayer.boundingBox.maxZ) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int)MC.thePlayer.boundingBox.minY, z);
                Block block = MC.theWorld.getBlockState(pos).getBlock();
                if(block != null && !(block instanceof BlockAir))
                    return block instanceof BlockLiquid;
            }
        }
        return false;
    }
    
    public static boolean isInsideBlock(EntityPlayer p) {
        for(int x = MathHelper.floor_double(p.boundingBox.minX); x < MathHelper.floor_double(p.boundingBox.maxX) + 1; x++) {
            for(int y = MathHelper.floor_double(p.boundingBox.minY); y < MathHelper.floor_double(p.boundingBox.maxY) + 1; y++) {
                for(int z = MathHelper.floor_double(p.boundingBox.minZ); z < MathHelper.floor_double(p.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if(block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(MC.theWorld, new BlockPos(x, y, z), MC.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if(block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if(boundingBox != null && p.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isInsideBlock() {
        for(int x = MathHelper.floor_double(MC.thePlayer.boundingBox.minX); x < MathHelper.floor_double(MC.thePlayer.boundingBox.maxX) + 1; x++) {
            for(int y = MathHelper.floor_double(MC.thePlayer.boundingBox.minY); y < MathHelper.floor_double(MC.thePlayer.boundingBox.maxY) + 1; y++) {
                for(int z = MathHelper.floor_double(MC.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(MC.thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if(block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(MC.theWorld, new BlockPos(x, y, z), MC.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if(block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if(boundingBox != null && MC.thePlayer.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isMoving() {
        return MC.thePlayer != null && (MC.thePlayer.movementInput.moveForward != 0f || MC.thePlayer.movementInput.moveStrafe != 0f);
    }
    
    public static float getSpeed() {
        return MathHelper.sqrt_double(MC.thePlayer.motionX * MC.thePlayer.motionX + MC.thePlayer.motionZ * MC.thePlayer.motionZ);
    }
    
    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
    	double forward = pseudoForward;
    	double strafe = pseudoStrafe;
    	double yaw = pseudoYaw;
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.z = 0.0;
            moveEvent.x = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (forward > 0.0) ? -45 : 45;
                } else if (strafe < 0.0) {
                    yaw += (forward > 0.0) ? 45 : -45;
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians((yaw + 90.0f)));
            double sin = Math.sin(Math.toRadians((yaw + 90.0f)));
            moveEvent.x = forward * moveSpeed * cos + strafe * moveSpeed * sin;
            moveEvent.z = forward * moveSpeed * sin - strafe * moveSpeed * cos;
        }
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafe(float speed) {
        if (!isMoving()) return;
        float yaw = (float) direction();
        MC.thePlayer.motionX = -MathHelper.sin(yaw) * speed;
        MC.thePlayer.motionZ = MathHelper.cos(yaw) * speed;
    }
    
    public static double direction() {
        double rotationYaw = MC.thePlayer.rotationYaw;
        if (MC.thePlayer.moveForward < 0f) rotationYaw += 180f;
        float forward = 1f;
        if (MC.thePlayer.moveForward < 0f) forward = -0.5f; else if (MC.thePlayer.moveForward > 0f) forward = 0.5f;
        if (MC.thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (MC.thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
    }

    public static boolean isInFOV(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = MiscUtils.getAngleDifference(MC.thePlayer.rotationYaw, rotations.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }
    
    public static boolean isOnEdgeOfBlock() {
    	return MC.theWorld.getCollidingBoundingBoxes(MC.thePlayer, MC.thePlayer.getEntityBoundingBox()
                .offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty();
    }
    
    public static boolean isOnEdgeOfBlock(EntityPlayer p) {
    	return MC.theWorld.getCollidingBoundingBoxes(p, p.getEntityBoundingBox()
                .offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty();
    }
    
    public static boolean canAttack(EntityLivingBase player) {
        if(player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !Menace.instance.moduleManager.killAuraModule.players)
                return false;
            if (player instanceof EntityAnimal && !Menace.instance.moduleManager.killAuraModule.animals)
                return false;
            if (player instanceof EntityMob && !Menace.instance.moduleManager.killAuraModule.mobs)
                return false;
        }
        if(player.isOnSameTeam(MC.thePlayer) && Menace.instance.moduleManager.killAuraModule.teams)
            return false;
        if(player.isInvisible() && !Menace.instance.moduleManager.killAuraModule.invisibles)
            return false;
        if(!isInFOV(player, 360))
            return false;
        return player != MC.thePlayer && player.isEntityAlive() && MC.thePlayer.getDistanceToEntity(player) <= MC.playerController.getBlockReachDistance();
    }

    public static double calculateGround() {
    	AxisAlignedBB playerBoundingBox = MC.thePlayer.getEntityBoundingBox();
    	double blockHeight = 1.0;
    	double ground = MC.thePlayer.posY;
    	
    	while (ground > 0.0) {
    		AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (MC.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight;
                ground += blockHeight;
                blockHeight = 0.05;
            }
            ground -= blockHeight;
    	}
    	
		return 0.0;
    }
    
    public static double calculateGround(BlockPos pos) {
    	AxisAlignedBB playerBoundingBox = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY() + 1, pos.getZ());
    	double blockHeight = 1.0;
    	double ground = MC.thePlayer.posY;
    	
    	while (ground > 0.0) {
    		AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (MC.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight;
                ground += blockHeight;
                blockHeight = 0.05;
            }
            ground -= blockHeight;
    	}
    	
		return 0.0;
    }
    
    public static void handleVanillaKickBypass() {
        if (System.currentTimeMillis() - groundTimer < 1000) return;

        final double x = MC.thePlayer.posX;
        final double y = MC.thePlayer.posY;
        final double z = MC.thePlayer.posZ;

        final double ground = calculateGround();

        for (double posY = y; posY > ground; posY -= 8D) {
            MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY - 8D < ground) break; // Prevent next step
        }

        MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, ground, z, true));


        for (double posY = ground; posY < y; posY += 8D) {
            MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY + 8D > y) break; // Prevent next step
        }

        MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));

        groundTimer = System.currentTimeMillis();
    }
    
    /*
	public static void handleVanillaKickBypass() {
		double ground = calculateGround();
		double oldY = MC.thePlayer.posY;

		for (double posY=MC.thePlayer.posY; posY>ground; posY-=8.0) {
            MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, posY, MC.thePlayer.posZ, true));
            if (posY - 8.0 < ground) break; // Prevent next step
		}
        MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, ground, MC.thePlayer.posZ, true));
        
        for (double posY=ground;posY < MC.thePlayer.posY;posY += 8.0) {
            MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, posY, MC.thePlayer.posZ, true));
            if (posY + 8.0 > MC.thePlayer.posY) break; // Prevent next step
        }
        MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
	}*/
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.thePlayer != null) {updateBPS();}
	}
	
	
	public void updateBPS() {
		if (MC.thePlayer == null || MC.thePlayer.ticksExisted < 1) {
            bps = 0.0;
        }
        double distance = MC.thePlayer.getDistance(lastX, lastY, lastZ);
        lastX = MC.thePlayer.posX;
        lastY = MC.thePlayer.posY;
        lastZ = MC.thePlayer.posZ;
        bps = distance * (20 * MC.timer.timerSpeed);
	}
	
	public static boolean isInVoid() {
		int i = (int) -(MC.thePlayer.posY-1.4857625);
		boolean dangerous = true;
		while (i <= 0) {
			dangerous = MC.theWorld.getCollisionBoxes(MC.thePlayer.getEntityBoundingBox().offset(MC.thePlayer.motionX * 0.5, (double) i, MC.thePlayer.motionZ * 0.5)).isEmpty();
			i++;
			if (!dangerous) break;
		}
		return dangerous;
	}


    public static boolean isOnServer(final String server) {

        final String ip = ServerUtils.getRemoteIp();

        if (server.equals("Hypixel")) {
            final boolean result = ip.contains("hypixel.net") && !ip.contains("ruhypixel.net") || ip.contains("2606:4700::6810:4e15");
            return result;
        }

        final boolean result = ip.contains(server);
        return result;
    }


    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = MC.thePlayer.getEntityBoundingBox();
        final WorldClient world = MC.theWorld;
        final int y = (int) playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static void stop() {
        MC.thePlayer.motionX = MC.thePlayer.motionZ = 0;
    }
    
    public static double moveSpeed() {
        if (MC.gameSettings.keyBindSprint.isKeyDown()) {
            if (MC.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (MC.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                    return 0.18386012061481244;
                } else {
                    return 0.21450346015841276;
                }
            } else {
                return 0.15321676228437875;
            }
        } else {
            if (MC.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (MC.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                    return 0.14143085686761;
                } else {
                    return 0.16500264553372018;
                }
            } else {
                return 0.11785905094607611;
            }
        }
    }
    
    public static Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX + offsetX, MC.thePlayer.posY + offsetY, MC.thePlayer.posZ + offsetZ)).getBlock();
    }

    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return MC.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
    
    /**
     * Used to get base movement speed
     */
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;

        if (MC.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (MC.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

        return baseSpeed;
    }
    
    public static boolean isBlockUnder() {
        if (MC.thePlayer.posY < 0) return false;
        int off = 0;
        while (off < MC.thePlayer.posY + 2) {
        	AxisAlignedBB bb = MC.thePlayer.getEntityBoundingBox()
                .offset(0.0, (double)-off, 0.0);
            if (!MC.theWorld.getCollidingBoundingBoxes(MC.thePlayer, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }
    
    public static double getDirection(final float yaw) {
        float rotationYaw = yaw;

        if (EntityPlayer.movementYaw != null) {
            rotationYaw = EntityPlayer.movementYaw;
        }

        if (MC.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (MC.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (MC.thePlayer.moveForward > 0F) forward = 0.5F;

        if (MC.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (MC.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
    
}

