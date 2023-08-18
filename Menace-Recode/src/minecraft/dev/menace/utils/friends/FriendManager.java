package dev.menace.utils.friends;

import dev.menace.utils.file.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendManager {

    ArrayList<String> friends = new ArrayList<>();

    public FriendManager() {
        File friendFile = new File(FileManager.getMenaceFolder(), "Friends.txt");
        if (!friendFile.exists()) {
            try {
                friendFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Load the friends from the file
        try {
            FileReader reader = new FileReader(friendFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                friends.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(String name) {

        if (isFriend(name)) return;

        File friendFile = new File(FileManager.getMenaceFolder(), "Friends.txt");

        //Save the friends to the file
        try {
            FileWriter writer = new FileWriter(friendFile);
            writer.append(name);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        friends.add(name);
    }

    public void removeFriend(String name) {

        File friendFile = new File(FileManager.getMenaceFolder(), "Friends.txt");

        //Remove the friend from the file
        try {
            FileWriter writer = new FileWriter(friendFile);
            for (String friend : friends) {
                if (!friend.equals(name)) {
                    writer.append(friend);
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        friends.remove(name);
    }

    public void clearFriends() {
        File friendFile = new File(FileManager.getMenaceFolder(), "Friends.txt");
        try {
            FileWriter writer = new FileWriter(friendFile);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        friends.clear();
    }

    public boolean isFriend(String name) {
        return friends.contains(name);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

}
