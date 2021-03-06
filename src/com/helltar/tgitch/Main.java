package com.helltar.tgitch;

import static com.helltar.tgitch.Utils.getStringFromFile;

import java.io.IOException;

public class Main {

    public static final String
            EXT_FILE = ".txt",
            TG_TOKEN_FILE = "telegram_token" + EXT_FILE,
            TG_CHANNEL_FILE = "telegram_channel" + EXT_FILE,
            TWITCH_OAUTH_FILE = "twitch_oauth" + EXT_FILE,
            TWITCH_USERNAME_FILE = "twitch_username" + EXT_FILE,
            TWITCH_CHANNEL_FILE = "twitch_channel" + EXT_FILE;

    public static Tgitch tgitchBot;
    public static TwitchIRC twitchIRC;

    public static String channel, username;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        channel = getStringFromFile(TWITCH_CHANNEL_FILE);
        username = getStringFromFile(TWITCH_USERNAME_FILE);

        tgitchBot = new Tgitch(getStringFromFile(TG_TOKEN_FILE),
                "@" + getStringFromFile(TG_CHANNEL_FILE)); // @ - for channels

        twitchIRC = new TwitchIRC(channel, username, getStringFromFile(TWITCH_OAUTH_FILE));

        connectToTwitch();

        try {
            while (true) {
                getUpdates();
            }
        } catch (IOException e) {
            addLog(e);
        }
    }

    public static void connectToTwitch() {
        try {
            twitchIRC.connect();
            sendMessageToTg("ā ā Logged\nš¢ ā " + channel + "\nš ā " + username);
        } catch (IOException e) {
            addLog(e);
            sendMessageToTg("ā ā Login error");
        }
    }

    public static void sendMessageToTg(String text) {
        try {
            tgitchBot.sendMessage(text);
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            addLog(e);
        }
    }

    public static void getUpdates() throws IOException {
        String msg = twitchIRC.getUpdates();

        if (!msg.isEmpty()) {
            if (!msg.equals("null")) {
                sendMessageToTg(msg);
            } else {
                sendMessageToTg("š ā Reconnecting");
                connectToTwitch();
            }
        }
    }

    public static void addLog(String msg) {
        System.out.println(msg);
    }

    public static void addLog(Exception e) {
        addLog(e.getMessage());
    }
}
