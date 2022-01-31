package com.github.helltar.tgitch;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Tgitch {

    private final String chatId;
    private String apiUrl = "https://api.telegram.org/";

    public Tgitch(String token, String chatId) {
        apiUrl += token;
        this.chatId = chatId;
    }

    public void sendMessage(String text) throws IOException {
        String[] data = {
                "chat_id", chatId,
                "parse_mode", "HTML",
                "text", text
        };

        Jsoup
                .connect(apiUrl + "/sendMessage")
                .data(data)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(0)
                .execute();
    }
}
