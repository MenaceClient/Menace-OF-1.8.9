package dev.menace.utils.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AntiSkidUtils {

    public static void log(String message) throws IOException {
        URL url = new URL("https://discord.com/api/webhooks/1057840834814410752/A9zQqMcNRoZpiT5VZYGf0S7VM0-K0GD3s3DLncva22RUf8PYz3mW-CD0JjiWRZGirvG6");

        // Create the message payload as a JSON object
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "UUID: " + MenaceUUIDHandler.getUUID() + " " + message);


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
}
