package dev.menace.module.modules.combat;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.world.Fucker;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;
import dev.menace.utils.misc.RayCastUtils;

public class KillAura extends Module
{
    public double krange;
    public double kdelay;
    public double kfr;
    public double particlesamount;
    private static boolean infinite;
    private static boolean morehit;
    private static boolean nosprint;
    private static boolean ignite;
    public static Minecraft mc = Minecraft.getMinecraft();
    public MSTimer time;
    private float[] lastRotations;
    private int rotationSpeed;
    private boolean rotated;
    public static Entity attacked = null;
    public int fr;
    int delay;
    public static boolean teams;
    public static boolean players;
    public static boolean invisibles;
    public static boolean animals;
    public static boolean mobs;
    public static boolean particles;
    public static boolean abk;
    public static boolean iabk;
    public static boolean unblock;
    public static boolean ab;
    public static boolean tw;
    public static boolean ns;
    public static boolean rc;
    public static boolean autodisable;
    public static boolean openinv;
    public static boolean aac;
    public static boolean reflex;
    public static boolean blocking;
    public static boolean fakeblock;
    
    //Settings
    DoubleSetting rangeSet;
    DoubleSetting delaySet;
    BoolSetting throughtWallsSet;
    BoolSetting raycastSet;
    DoubleSetting failrateSet;
    BoolSetting autoblockSet;
    BoolSetting interactAutoblockSet;
    BoolSetting unblockSet;
    BoolSetting fakeblockSet;
    BoolSetting morehitSet;
    BoolSetting infiniteSet;
    BoolSetting openInvSet;
    BoolSetting noSprintSet;
    BoolSetting igniteSet;
    BoolSetting aacSet;
    BoolSetting reflexSet;
    BoolSetting autoDisableSet;
    BoolSetting noSwingSet;
    BoolSetting particlesSet;
    DoubleSetting particlesAmountSet;
    BoolSetting invisiblesSet;
    BoolSetting playersSet;
    BoolSetting animalsSet;
    BoolSetting monstersSet;
    BoolSetting teamsSet;
    
    public KillAura() {
    	super("Killaura", 0, Category.COMBAT);
        this.time = new MSTimer();
        this.lastRotations = new float[2];
        this.rotationSpeed = 1;
        this.rotated = false;
        this.fr = 0;
        this.delay = 0;
        this.krange = 4.0;
        this.kdelay = 83.0;
        this.particlesamount = 1.0;
        KillAura.ab = true;
    }
    
    @Override
	public void setup() {
        rangeSet = new DoubleSetting("Range", this, 5, 1, 10);
        delaySet = new DoubleSetting("Delay", this, 8, 2, 20);
        throughtWallsSet = new BoolSetting("ThroughWalls", this, false);
        raycastSet = new BoolSetting("RayCast", this, true);
        failrateSet = new DoubleSetting("FailRate", this, 3, 0, 10);
        autoblockSet = new BoolSetting("AutoBlock", this, true);
        interactAutoblockSet = new BoolSetting("InteractAutoBlock", this, false);
        unblockSet = new BoolSetting("UnBlock", this, false);
        fakeblockSet = new BoolSetting("FakeBlock", this, false);
        morehitSet = new BoolSetting("MoreHit", this, false);
        infiniteSet = new BoolSetting("Infinite", this, false);
        openInvSet = new BoolSetting("OpenInv", this, true);
        noSprintSet = new BoolSetting("NoSprint", this, false);
        igniteSet = new BoolSetting("Ignite", this, false);
        aacSet = new BoolSetting("AAC", this, false);
        reflexSet = new BoolSetting("Reflex", this, false);
        autoDisableSet = new BoolSetting("AutoDisable", this, false);
        noSwingSet = new BoolSetting("NoSwing", this, false);
        particlesSet = new BoolSetting("Particles", this, false);
        particlesAmountSet = new DoubleSetting("ParticlesAmount", this, 3, 0, 10);
        invisiblesSet = new BoolSetting("Invisibles", this, false);
        playersSet = new BoolSetting("Players", this, true);
        animalsSet = new BoolSetting("Animals", this, false);
        monstersSet = new BoolSetting("Monsters", this, false);
        teamsSet = new BoolSetting("Teams", this, false);
    	this.rSetting(rangeSet);
    	this.rSetting(delaySet);
    	this.rSetting(throughtWallsSet);
    	this.rSetting(raycastSet);
    	this.rSetting(failrateSet);
    	this.rSetting(autoblockSet);
    	this.rSetting(interactAutoblockSet);
    	this.rSetting(unblockSet);
    	this.rSetting(fakeblockSet);
    	this.rSetting(morehitSet);
    	this.rSetting(infiniteSet);
    	this.rSetting(openInvSet);
    	this.rSetting(noSprintSet);
    	this.rSetting(igniteSet);
    	this.rSetting(aacSet);
    	this.rSetting(reflexSet);
    	this.rSetting(autoDisableSet);
    	this.rSetting(noSwingSet);
    	this.rSetting(particlesSet);
    	this.rSetting(particlesAmountSet);
    	this.rSetting(invisiblesSet);
    	this.rSetting(playersSet);
    	this.rSetting(animalsSet);
    	this.rSetting(monstersSet);
    	this.rSetting(teamsSet);
	}
    
    @Override
    public void onEnable() {
    	if (MC.theWorld == null) {
    		return;
    	}
    	time.reset();
    	this.teams = teamsSet.isChecked();
    	this.animals = animalsSet.isChecked();
    	this.mobs = monstersSet.isChecked();
    	this.invisibles = invisiblesSet.isChecked();
    	this.players = playersSet.isChecked();
    	this.particles = particlesSet.isChecked();
    	this.abk = autoblockSet.isChecked();
    	this.iabk = interactAutoblockSet.isChecked();
    	this.unblock = unblockSet.isChecked();
    	this.tw = throughtWallsSet.isChecked();
    	this.ignite = igniteSet.isChecked();
    	this.ns = noSwingSet.isChecked();
    	this.rc = raycastSet.isChecked();
    	this.autodisable = autoDisableSet.isChecked();
    	this.openinv = openInvSet.isChecked();
    	this.aac = aacSet.isChecked();
    	this.reflex = reflexSet.isChecked();
    	this.nosprint = noSprintSet.isChecked();
    	this.morehit = morehitSet.isChecked();
    	this.infinite = infiniteSet.isChecked();
    	this.fakeblock = fakeblockSet.isChecked();
    	this.particlesamount = particlesAmountSet.getValue();
    	this.kfr = failrateSet.getValue();
    	this.krange = rangeSet.getValue();
    	this.kdelay = delaySet.getValue();
    	blocking = mc.thePlayer.isBlocking();
    	super.onEnable();
    }
    
    @Override
    public void onDisable() {
    	super.onDisable();
        this.fr = 0; 
        this.attacked = null;
        blocking = false;
    }
    
    @EventTarget
    public void onPacket(EventSendPacket event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    if (!this.reflex && !this.aac) {
                    C03PacketPlayer c03PacketPlayer = new C03PacketPlayer();
					c03PacketPlayer.setYaw(facing[0]);
					c03PacketPlayer.setPitch(facing[1]);
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (Menace.instance.moduleManager.targetStrafeModule.isToggled() && Menace.instance.moduleManager.targetStrafeModule.rotate.isChecked()) {
                    	mc.thePlayer.rotationYaw = facing[0];	
                    }
                }
            }
            }
        }
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	if (this.attacked != null && !isValid(this.attacked) || this.attacked != null && this.attacked.isDead) this.attacked = null;
    	if (!this.infinite) {
    		this.setDisplayName("Killaura §7[Multi]");
    	} else {
    		this.setDisplayName("Killaura §7[Infinite]");
    	}
        if (this.reflex) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    mc.thePlayer.rotationYaw = facing[0];
                    mc.thePlayer.rotationPitch = facing[1];
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                		final float randomYaw = (float) (Math.random() * 1);
                		final float randomPitch = (float) (Math.random() * 1);
                        mc.thePlayer.rotationYaw += randomYaw;
                        mc.thePlayer.rotationPitch += randomPitch;
                        mc.thePlayer.rotationYawHead += randomYaw;
                        mc.thePlayer.renderYawOffset += randomYaw;
                    } else {
                		final float randomYaw = (float) (Math.random() * 1);
                		final float randomPitch = (float) (Math.random() * 1);
                        mc.thePlayer.rotationYaw -= randomYaw;
                        mc.thePlayer.rotationPitch -= randomPitch;
                        mc.thePlayer.rotationYawHead -= randomYaw;
                        mc.thePlayer.renderYawOffset -= randomYaw;
                    }
                }
            }
        }
        if (this.aac) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    MC.thePlayer.rotationYaw = (facing[0]);
                    MC.thePlayer.rotationPitch = (facing[1]);
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (Menace.instance.moduleManager.targetStrafeModule.isToggled()) {
                    mc.thePlayer.rotationYaw = facing[0];	
                    }
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                		final float randomYaw = (float) (facing[0] + Math.random() * 1);
                		final float randomPitch = (float) (facing[1] + Math.random() * 1);
                		MC.thePlayer.rotationYaw = (randomYaw);
                		MC.thePlayer.rotationPitch = (randomPitch);
                        mc.thePlayer.rotationYawHead = randomYaw;
                        mc.thePlayer.renderYawOffset = randomYaw;
                    } else {
                		final float randomYaw = (float) (facing[0] - Math.random() * 1);
                		final float randomPitch = (float) (facing[1] - Math.random() * 1);
                		MC.thePlayer.rotationYaw = (randomYaw);
                		MC.thePlayer.rotationPitch = (randomPitch);
                        mc.thePlayer.rotationYawHead = randomYaw;
                        mc.thePlayer.renderYawOffset = randomYaw;
                    }
                }
            }
        }
        if (this.autodisable && mc.thePlayer.ticksExisted <= 1 || this.autodisable && mc.currentScreen instanceof GuiGameOver) {
        	ChatUtils.message("KillAura disabled due to death");
        	this.toggle();
        }
        final int failrate = (int)this.kfr;
        for (final Object j : mc.theWorld.loadedEntityList) {
            Entity e = (Entity)j;
            if (this.isValid(e)) {
            	if (this.infinite) {
            		PlayerUtils.tpToEnt(e);
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(e.posX, e.posY, e.posZ, false));
            	}
                if (this.abk) {
                    final int range = (int)this.krange;
                    if (mc.thePlayer.getDistanceToEntity(e) <= range) {
                        try {
                        	blocking = true;
                        	if (!this.iabk) {
                        		if (MC.thePlayer.getCurrentEquippedItem() != null && MC.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                    MC.playerController.sendUseItem(MC.thePlayer, MC.theWorld, MC.thePlayer.getCurrentEquippedItem());
                        		}
                        	} else {
                            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                                mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), (int) (Math.random() * 100));                                
                            }
                        }
                        }
                        catch (Exception ex) {}
                    }
                }               
                if (this.unblock) {
                    final int range = (int)this.krange;
                    if (mc.thePlayer.getDistanceToEntity(e) <= range) {
                    	if (mc.thePlayer.isBlocking()) {
                    		mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    		blocking = false;
                    	}
                    }
                }
                final Random rdm = new Random();
                this.delay = rdm.nextInt((int)this.kdelay);
                if (this.delay < this.kdelay * 0.5) {
                    this.delay = (int)this.kdelay;
                }
                if (!this.time.hasTimePassed(this.delay)) {
                    continue;
                }
                if (((EntityLivingBase)e).deathTime == 0) {
                    try {
                        if (this.rc) {
                            final MovingObjectPosition objectMouseOver = RayCastUtils.getMouseOver(this.lastRotations[0], this.lastRotations[1], (float)this.krange);
                            if (objectMouseOver.entityHit != null && e != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
                                e = objectMouseOver.entityHit;
                            }
                        }
                    }
                    catch (Exception ex2) {}
                    final int particlesamount = (int)this.particlesamount;
                    if (failrate != 0 && failrate != 1) {
                        if (this.fr < failrate) {
                        	if (this.ns) {
                        		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        	}
                        	else {
                        		mc.thePlayer.swingItem();
                        	}
                            if (KillAura.nosprint) {
                            	if (this.morehit) {
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {                      	        
                                    mc.playerController.attackEntity(mc.thePlayer, e);
                                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                            	    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                    mc.playerController.attackEntity(mc.thePlayer, e);	
                                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                            	    }
                            	}
                            	} else {                            		
                            	mc.playerController.attackEntity(mc.thePlayer, e);
                            	}
                            } else {
                            	if (this.morehit) {
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                            		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                        	        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                        	        	mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                        mc.thePlayer.swingItem();
                        	        }
                        	        }
                            	} else {
                            	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                            	}
                            }
                            if (this.fr < failrate) {
                                ++this.fr;
                            }
                            if (this.particles) {
                                for (int i = 0; i < particlesamount; ++i) {
                                    mc.effectRenderer.emitParticleAtEntity(e, EnumParticleTypes.CRIT);
                                }
                            }
                            attacked = e;
                        }
                        else {
                            this.fr = 0;
                        }
                    }
                    else {
                    	if (this.ns) {
                    		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    	}
                    	else {
                    		mc.thePlayer.swingItem();
                    	}
                    	if (KillAura.nosprint) {
                        	if (this.morehit) {
                    	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                                mc.playerController.attackEntity(mc.thePlayer, e);
                        	    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                mc.playerController.attackEntity(mc.thePlayer, e);	
                        	    }
                        	}
                        	} else {                            		
                        	mc.playerController.attackEntity(mc.thePlayer, e);
                        	}
                        } else {
                        	if (this.morehit) {
                    	        if (mc.thePlayer.ticksExisted % 1 == 0) {
                        		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                    	        if (mc.thePlayer.ticksExisted % 1 == 0) {
                    	        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                    	        }
                    	        }
                        	} else {
                        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                        	}
                        }
                        if (this.particles) {
                            for (int i = 0; i < particlesamount; ++i) {
                                mc.effectRenderer.emitParticleAtEntity(e, EnumParticleTypes.CRIT);
                            }
                        }
                        attacked = e;
                    }
                }
                this.time.reset();
            }
        }
    }
    
    public boolean isBot(final Entity e) {
        return AntiBot.bots.contains(new StringBuilder().append(e.getEntityId()).toString());
    }
    
    public boolean isValid(final Entity e) {
        return e instanceof EntityLivingBase && /*!FriendManager.isFriend(e.getName()) &&*/ (this.invisibles || !(e.isInvisible())) && (this.players || !(e instanceof EntityPlayer)) && (this.animals || !(e instanceof EntityAnimal)) && (this.mobs || !(e instanceof EntityMob)) && e != mc.thePlayer && !(e instanceof EntityVillager) && mc.thePlayer.getDistanceToEntity(e) <= this.krange && !e.getName().contains("#")  && (!this.teams || !e.getDisplayName().getFormattedText().startsWith("§" + mc.thePlayer.getDisplayName().getFormattedText().charAt(1))) && (!this.ab || !this.isBot(e)) && !e.getName().toLowerCase().contains("shop") && !e.getName().toLowerCase().contains("upgrades") && (this.tw || mc.thePlayer.canEntityBeSeen(e)) && (this.openinv || !(mc.currentScreen instanceof Gui) && !e.isDead);
    }
    
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    
    public float[] getRotations(final Vec3 vec) {
        final Vec3 eyesPos = this.getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public Vec3 getEyesPos() {
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }
    
    public static double myRound(final double wert, final int stellen) {
        return Math.round(wert * Math.pow(10.0, stellen)) / Math.pow(10.0, stellen);
    }
}