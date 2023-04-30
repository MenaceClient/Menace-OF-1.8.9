package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.network.play.client.C03PacketPlayer;

@JSMapping(106)
public class EventTeleport extends Event {

    C03PacketPlayer.C06PacketPlayerPosLook response;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;

    public EventTeleport(C03PacketPlayer.C06PacketPlayerPosLook response, double posX, double posY, double posZ, float yaw, float pitch) {
        this.response = response;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @MappedName(3)
    public double getPosX() {
        return posX;
    }

    @MappedName(52)
    public void setPosX(double posX) {
        this.posX = posX;
    }

    @MappedName(4)
    public double getPosY() {
        return posY;
    }

    @MappedName(53)
    public void setPosY(double posY) {
        this.posY = posY;
    }

    @MappedName(5)
    public double getPosZ() {
        return posZ;
    }

    @MappedName(54)
    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    @MappedName(64)
    public float getYaw() {
        return yaw;
    }

    @MappedName(66)
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @MappedName(65)
    public float getPitch() {
        return pitch;
    }

    @MappedName(67)
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @MappedName(75)
    public C03PacketPlayer.C06PacketPlayerPosLook getResponse() {
        return response;
    }
}
