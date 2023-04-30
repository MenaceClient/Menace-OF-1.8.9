package dev.menace.utils.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.menace.Menace;
import net.minecraft.util.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslatorUtils {

    public static String translate(String text) {

        try {
            final URL url = new URL("https://api.mymemory.translated.net/get?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8.name()) + "&langpair=zh-CN|en");
            HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            uc.setRequestMethod("GET");
            int responseCode = uc.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject reply = new JsonParser().parse(response.toString()).getAsJsonObject();
                return reply.get("responseData").getAsJsonObject().get("translatedText").getAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
