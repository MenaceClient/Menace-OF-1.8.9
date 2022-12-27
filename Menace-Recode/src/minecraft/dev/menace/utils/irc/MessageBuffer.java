package dev.menace.utils.irc;

public class MessageBuffer {
    String buffer;

    public MessageBuffer() {
        buffer = "";
    }

    public void append(byte[] bytes) {
        buffer += new String(bytes);
    }

    public boolean hasCompleteMessage() {
        if (buffer.contains("\r\n"))
            return true;
        else
            return false;
    }

    public String getNextMessage() {
        int index = buffer.indexOf("\r\n");
        String message = "";

        if (index > -1) {
            message = buffer.substring(0, index);
            buffer = buffer.substring(index + 2);
        }

        return message;
    }
}
