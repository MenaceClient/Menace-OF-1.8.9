package dev.menace.utils.security;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.minecraft.util.Util;

public class AntiVM
{
    public static boolean run() {
        return Util.getOSType() == Util.EnumOS.WINDOWS && (run("wmic computersystem get model", "Model", new String[] { "virtualbox", "vmware", "kvm", "hyper-v" }) && run("WMIC BIOS GET SERIALNUMBER", "SerialNumber", new String[] { "0" }) && run("wmic baseboard get Manufacturer", "Manufacturer", new String[] { "Microsoft Corporation" }));
    }

    private static boolean run(final String command, final String startsWith, final String[] closePhrase) {
        try {
            final Process p = Runtime.getRuntime().exec(command);
            final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!line.startsWith(startsWith) && !line.equals("")) {
                    final String model = line.replaceAll(" ", "");
                    if (closePhrase.length > 1) {
                        for (int n = closePhrase.length, n2 = 0; n2 < n; ++n2) {
                            final String str = closePhrase[n2];
                            if (model.contains(str)) {
                                AntiSkidUtils.terminate("You seem to be running Menace in a virtual machine if you  think this is an error please contact the admins.", 0x01, "VM Detected: " + model);
                                return false;
                            }
                        }
                    }
                    else if (model.equals(closePhrase[0])) {
                        AntiSkidUtils.terminate("You seem to be running Menace in a virtual machine if you  think this is an error please contact the admins.", 0x02, "VM Detected: " + model);
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}