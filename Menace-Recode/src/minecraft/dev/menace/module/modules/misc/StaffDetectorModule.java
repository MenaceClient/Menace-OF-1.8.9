package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffDetectorModule extends BaseModule {

    private boolean detected = false;
    List<String> obStaffs = new ArrayList<>();

    public StaffDetectorModule() {
        super("StaffDetector", Category.MISC, 0);
    }

    @Override
    public void setup() {
        final boolean[] check = {false};
        Thread t = new Thread() {
            @Override
            public void run() {
                MSTimer timer = new MSTimer();
                timer.reset();
                while (!check[0] && !timer.hasTimePassed(30000L)) {
                    obStaffs = readURL();

                    if (!obStaffs.contains("checking")) {
                        check[0] = true;
                        System.out.println("[Menace] Found " + obStaffs.size() + " staff");
                    }
                }

                if (!check[0]) {
                    obStaffs = readURL2();
                    System.out.println("[Menace] Found " + obStaffs.size() + " staff using backup");
                }
                super.run();
            }
        };

        t.start();

        super.setup();
    }

    @Override
    public void onEnable() {
        detected = false;
        super.onEnable();
    }

    @EventTarget
    public void onRecievePacket(@NotNull EventReceivePacket event) {
        if (event.getPacket() instanceof S1DPacketEntityEffect) {
            Entity entity = MC.theWorld.getEntityByID(((S1DPacketEntityEffect)event.getPacket()).getEntityId());
            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S18PacketEntityTeleport) {
            Entity entity = MC.theWorld.getEntityByID(((S18PacketEntityTeleport)event.getPacket()).getEntityId());
            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S20PacketEntityProperties) {
            Entity entity = MC.theWorld.getEntityByID(((S20PacketEntityProperties)event.getPacket()).getEntityId());
            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S0BPacketAnimation) {
            Entity entity = MC.theWorld.getEntityByID(((S0BPacketAnimation)event.getPacket()).getEntityID());
            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S14PacketEntity) {
            Entity entity = ((S14PacketEntity)event.getPacket()).getEntity(MC.theWorld);
            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S19PacketEntityStatus) {
            Entity entity = ((S19PacketEntityStatus)event.getPacket()).getEntity(MC.theWorld);

            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S19PacketEntityHeadLook) {
            Entity entity = ((S19PacketEntityHeadLook)event.getPacket()).getEntity(MC.theWorld);

            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
        if (event.getPacket() instanceof S49PacketUpdateEntityNBT) {
            Entity entity = ((S49PacketUpdateEntityNBT)event.getPacket()).getEntity(MC.theWorld);

            if (entity != null && (obStaffs.contains(entity.getName()) || obStaffs.contains(entity.getDisplayName().getUnformattedText()))) {
                if (!detected) {
                    ChatUtils.message("Staff Detected! (" + entity.getName() + ")");
                    MC.thePlayer.sendChatMessage("/leave");
                    detected = true;
                }
            }
        }
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        detected = false;
    }

    public static @NotNull List<String> readURL() {
        List<String> s = new ArrayList<>();
        try {
            //final URL url = new URL("https://themoskau.github.io/LiquidCloud/LiquidBounce/staffs.txt");
            final URL url = new URL("https://add-my-brain.exit-scammed.repl.co/staff/");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.addAll(Arrays.asList(hwid.split(" ")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static @NotNull List<String> readURL2() {
        List<String> s = new ArrayList<>();
        try {
            final URL url = new URL("https://themoskau.github.io/LiquidCloud/LiquidBounce/staffs.txt");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.addAll(Arrays.asList(hwid.split(" ")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}