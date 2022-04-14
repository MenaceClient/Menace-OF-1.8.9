package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.settings.*;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot extends Module {
	public static ArrayList<String> bots = new ArrayList<String>();
	
	//Settings
    StringSetting mode;
	
	public AntiBot() {
		super("AntiBot", 0, Category.COMBAT);
	}

	@Override
    public void setup() {
	    ArrayList<String> options = new ArrayList<String>();
        options.add("Advanced");
        options.add("Invisible");
        mode = new StringSetting("Mode", this, "Advanced", options);
        this.rSetting(mode);
    }
	
	@Override
	public void onDisable() {
		super.onDisable();
		this.bots.clear();
	}

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String m = mode.getString();

        if(m.equalsIgnoreCase("Advanced") && event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)event.getPacket();
            double posX = packet.getX() / 32D;
            double posY = packet.getY() / 32D;
            double posZ = packet.getZ() / 32D;

            double diffX = MC.thePlayer.posX - posX;
            double diffY = MC.thePlayer.posY - posY;
            double diffZ = MC.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

            if(dist <= 17D && posX != MC.thePlayer.posX && posY != MC.thePlayer.posY && posZ != MC.thePlayer.posZ)
                //event.setCancelled(true);
            	bots.add(String.valueOf(packet.getEntityID()));
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String m = mode.getString();
        this.setDisplayName("AntiBot \u00A77" + m);

        if(m.equalsIgnoreCase("Invisible"))
            for (Object entity : MC.theWorld.loadedEntityList)
                if (((Entity) entity).isInvisible() && entity != MC.thePlayer)
                    MC.theWorld.removeEntity((Entity) entity);
    }
}
