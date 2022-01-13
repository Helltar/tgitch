package com.github.helltar.tgitch;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Tgitch {

    private String chatId, apiUrl = "https://api.telegram.org/";

    public Tgitch(String token, String chatId) {
        apiUrl += token;
        this.chatId = chatId;
    }

    public void sendMessage(String text) throws IOException {
        String[] data = {
                "chat_id", chatId,
                "parse_mode", "HTML",
                "disable_web_page_preview", "true",
                "text", text
        };

        sendPost(apiUrl + "/sendMessage", data);
    }

    private Connection.Response sendPost(String url, String[] data) throws IOException {
        return Jsoup
                .connect(url)
                .data(data)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                // .ignoreHttpErrors(true)
                .timeout(0)
                .execute();
    }
}
