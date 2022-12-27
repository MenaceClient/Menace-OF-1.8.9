package dev.menace.utils.security;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenaceUUIDHandler {

    private static UUID menaceUUID;

    public static void parseUUID(String str) {
        menaceUUID = UUID.fromString(str);
    }

    public static UUID getUUID() {
        return menaceUUID;
    }

    public static void validate() {
        Matcher matcher = Pattern.compile("([a-f0-9]{8})-([a-f0-9]{4})-([a-f0-9]{4})-([a-f0-9]{4})-([a-f0-9]{12})").matcher(menaceUUID.toString());
        if (!matcher.matches()) {
            throw new RuntimeException("Menace UUID is invalid, please contact Exterminate#6552.");
        }
    }
}
