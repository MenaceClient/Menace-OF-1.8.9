package dev.menace.utils.spotify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.Executors;

public class SpotifyUtils {

    private Thread songUpdateThread;
    public String CLIENTID = "";
    public String CLIENTSECRET = "";
    private final String REDIRECTURI = "http://localhost:8080";
    private String refresh_token;
    private String access_token;
    private boolean isAuthed = false;
    private HttpServer server = null;

    public SpotifyUtils() {
        fileAuthenticate();
        if (isAuthed) {
            startSongUpdateThread();
        }
    }

    public void auth() {
        if (isAuthed) return;
        authenticate();
        startSongUpdateThread();
    }

    public void startSongUpdateThread() {
        if (songUpdateThread != null && songUpdateThread.isAlive()) return;
        songUpdateThread = new Thread(() -> {
            while (true) {
                boolean idk = updateCurrentSong();
                if (!idk) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        songUpdateThread.start();
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
                    refreshAuth();
                    sendApiRequest(apiUrl);
                } else if (conn.getResponseCode() == 204) {
                    return null;
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

    public boolean updateCurrentSong() {
        if (!songTimer.hasTimePassed(songEndTimeStamp) || !isAuthed) {
            return false;
        }

        JsonObject response = sendApiRequest("https://api.spotify.com/v1/me/player/currently-playing");

        if (response == null) {
            currentSong = null;
            return false;
        }

        currentSong = response.get("item").getAsJsonObject();
        songEndTimeStamp = (response.get("item").getAsJsonObject().get("duration_ms").getAsInt() - response.get("progress_ms").getAsInt());
        songTimer.reset();
        return true;
    }

    public int getSongProgress() {
        return (int) (songEndTimeStamp - songTimer.timePassed());
    }

    public void fileAuthenticate() {
        File spotifyAuth = new File(FileManager.getMenaceFolder(), "SpotifyAuth.txt");
        try {
            if (spotifyAuth.exists()) {
                FileReader fileReader = new FileReader(spotifyAuth);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("CLIENTID:")) {
                        CLIENTID = line.substring(9);
                    } else if (line.startsWith("CLIENTSECRET:")) {
                        CLIENTSECRET = line.substring(13);
                    } else if (line.startsWith("REFRESH_TOKEN:")) {
                        sendAuthRequest("grant_type=refresh_token&refresh_token=" + line.substring(14));
                    }
                }

                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authenticate() {
        File spotifyAuth = new File(FileManager.getMenaceFolder(), "SpotifyAuth.txt");
        try {
            if (spotifyAuth.exists()) {
                FileReader fileReader = new FileReader(spotifyAuth);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("CLIENTID:")) {
                        CLIENTID = line.substring(9);
                    } else if (line.startsWith("CLIENTSECRET:")) {
                        CLIENTSECRET = line.substring(13);
                    } else if (line.startsWith("REFRESH_TOKEN:")) {
                        sendAuthRequest("grant_type=refresh_token&refresh_token=" + line.substring(14));
                    }
                }

                bufferedReader.close();
            } else {
                startWebServer();

                //open link in browser
                String url = "https://accounts.spotify.com/authorize?client_id=" + CLIENTID +  "&response_type=code&redirect_uri=http://localhost:8080&show_dialog=true&scope=user-read-private%20user-read-email%20user-modify-playback-state%20user-read-playback-position%20user-library-read%20streaming%20user-read-playback-state%20user-read-recently-played%20playlist-read-private";

                switch (Util.getOSType()) {
                    case LINUX:
                        Runtime.getRuntime().exec("xdg-open " + url);
                        break;
                    case WINDOWS:
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                        break;
                    case OSX:
                        Runtime.getRuntime().exec("open " + url);
                        break;
                    case SOLARIS:
                        Runtime.getRuntime().exec("firefox " + url);
                        break;
                    default:
                        break;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  void refreshAuth() {
        sendAuthRequest("grant_type=refresh_token&refresh_token=" + refresh_token);
    }

    public void startWebServer() {
        if (server != null) {
            server.stop(0);
        }

        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String response = "Authentication successful! You can now close this window.";
            if (query != null && query.contains("code")) {
                String code = query.substring(5);
                new Thread(() -> {
                    sendAuthRequest("grant_type=authorization_code&code=" + code + "&redirect_uri=" + REDIRECTURI);
                    server.stop(0);
                }).start();
            } else {
                response = "Authentication failed. Please try again.";
            }
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    private void sendAuthRequest(String body) {
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENTID + ":" + CLIENTSECRET).getBytes()));
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            conn.getOutputStream().write(body.getBytes());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder sb = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();

            access_token = json.get("access_token").getAsString();
            if (!isAuthed) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    ChatUtils.message("Authentication Successful!");
                }
                isAuthed = true;
            }

            if (json.has("refresh_token")) {
                refresh_token = json.get("refresh_token").getAsString();
                File spotifyAuth = new File(FileManager.getMenaceFolder(), "SpotifyAuth.txt");
                FileWriter fileWriter = new FileWriter(spotifyAuth);
                fileWriter.write("CLIENTID:" + CLIENTID + "\nCLIENTSECRET:" + CLIENTSECRET + "\nREFRESH_TOKEN:" + refresh_token);
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        return access_token;
    }

    public boolean isAuthed() {
        return isAuthed;
    }
}
