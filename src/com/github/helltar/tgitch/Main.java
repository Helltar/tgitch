package com.github.helltar.tgitch;

import static com.github.helltar.tgitch.Utils.getStringFromFile;

public class Main {
    
    public static final String
            EXT_FILE = ".txt",
            TG_TOKEN_FILE = "telegram_token" + EXT_FILE,
            TG_CHANNEL_FILE = "telegram_channel" + EXT_FILE,
            TWITCH_OAUTH_FILE = "twitch_oauth" + EXT_FILE,
            TWITCH_USERNAME_FILE = "twitch_username" + EXT_FILE;

    public static void main(String[] args) {
        Tgitch tgitchBot = new Tgitch(getStringFromFile(TG_TOKEN_FILE),
                                            "@" + getStringFromFile(TG_CHANNEL_FILE)); // @ - for channels
        
        TwitchIRC twitchIRC = new TwitchIRC(getStringFromFile(TWITCH_USERNAME_FILE),
                                            getStringFromFile(TWITCH_OAUTH_FILE));

        if (twitchIRC.connect()) {
            while (true) {
                tgitchBot.sendMessage(twitchIRC.getUpdates());
                
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Logger.add(e);
                }
            }
        }
    }
}
