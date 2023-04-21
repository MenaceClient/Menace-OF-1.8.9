package dev.menace.utils.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.menace.Menace;
import net.arikia.dev.drpc.DiscordUser;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AntiSkidUtils {

    public static void terminate(String error, int errorID, String loggerMessage) throws Exception {
        log(loggerMessage);

        runErrorPanel(error, errorID);

        //Delete the jar
        SelfDestruct.selfDestructJARFile();

        //Terminate the program but not the Error panel
        Minecraft.getMinecraft().shutdownMinecraftApplet();
    }

    public static void terminate(String error, int errorID) throws Exception {
        runErrorPanel(error, errorID);

        //Delete the jar
        SelfDestruct.selfDestructJARFile();

        //Terminate the program but not the Error panel
        Minecraft.getMinecraft().shutdownMinecraftApplet();
    }


    private static void log(String message) throws IOException {
        URL url = new URL("https://discord.com/api/webhooks/1057840834814410752/A9zQqMcNRoZpiT5VZYGf0S7VM0-K0GD3s3DLncva22RUf8PYz3mW-CD0JjiWRZGirvG6");

        //Discord ID so I can dm the dumbass trying to skid me
        StringBuilder discordIDs = new StringBuilder();
        for (DiscordUser user : Menace.instance.discordRP.getUsers()) {
            discordIDs.append(user.userId).append(" ");
        }
        String ids = discordIDs.toString();

        // Create the message payload as a JSON object
        JsonObject payload = new JsonObject();
        StringBuilder msg = new StringBuilder();
        msg.append("UUID: ").append(MenaceUUIDHandler.getUUID());
        if (!Menace.instance.discordRP.getUsers().isEmpty()) {
            msg.append(" Discord ID's found: ").append(ids);
        }
        msg.append(" ").append(message);
        payload.addProperty("content", msg.toString());


        // Serialize the payload as a JSON string
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(payload);

        // Send the POST request
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        conn.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(jsonPayload);
        out.flush();
        out.close();

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    private static void runErrorPanel(String error, int errorID) {
        new Thread() {
            @Override
            public void run() {
                Process process = null;
                try {
                    File temp = File.createTempFile("ErrorPanel", ".jar");

                    InputStream inputStream = AntiSkidUtils.class.getResourceAsStream("/resources/security/ErrorPanel.jar");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, read);
                    }
                    fileOutputStream.close();
                    inputStream.close();

                    String command = "java -jar " + temp.getAbsolutePath() + " " + error + " " + errorID;
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
                    process = builder.start();
                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                super.run();
            }
        }.start();
    }

}
