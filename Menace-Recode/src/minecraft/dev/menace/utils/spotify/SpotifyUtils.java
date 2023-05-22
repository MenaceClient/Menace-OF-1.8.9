package dev.menace.utils.spotify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.menace.Menace;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifyUtils {

    private String access_token;
    private boolean isAuthed = false;
    private final Thread songUpdateThread;

    public SpotifyUtils() {
        songUpdateThread = new Thread(() -> {
            while (true) {
                updateCurrentSong();
            }
        });
    }

    public void auth() {
        sendAuthRequest();
        startSongUpdateThread();
    }

    public void startSongUpdateThread() {
        if (!songUpdateThread.isAlive()) {
            songUpdateThread.start();
        }
    }

    private void sendAuthRequest() {
        try {
            final URL url = new URL( Menace.instance.apiURL + "/spotify/getToken/" + Menace.instance.user.getHwid() + "/");
            HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            uc.setRequestMethod("GET");

            if (uc.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + uc.getResponseCode() + " " + uc.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((uc.getInputStream())));

            StringBuilder sb = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            if (sb.toString().equals("Failed")) {
                isAuthed = false;
            } else {
                access_token = sb.toString();
                isAuthed = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject sendApiRequest(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + access_token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            if (conn.getResponseCode() != 200) {
                if (conn.getResponseCode() == 401) {
                    sendAuthRequest();
                    sendApiRequest(apiUrl);
                }
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder sb = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();

            conn.disconnect();

            return json;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    JsonObject currentSong = null;
    long songEndTimeStamp = 0;
    MSTimer songTimer = new MSTimer();
    public JsonObject getCurrentSong() {
        return currentSong;
    }

    public void updateCurrentSong() {
        if (!songTimer.hasTimePassed(songEndTimeStamp) || !isAuthed) {
            return;
        }

        JsonObject response = sendApiRequest("https://api.spotify.com/v1/me/player/currently-playing");

        if (response == null) {
            currentSong = null;
            return;
        }

        currentSong = response.get("item").getAsJsonObject();
        songEndTimeStamp = (response.get("item").getAsJsonObject().get("duration_ms").getAsInt() - response.get("progress_ms").getAsInt());
        songTimer.reset();
    }

    public int getSongProgress() {
        return (int) (songEndTimeStamp - songTimer.timePassed());
    }

    public String getAccessToken() {
        return access_token;
    }

    public boolean isAuthed() {
        return isAuthed;
    }
}
