package com.github.helltar.tgitch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TwitchIRC {

    private final String channel, username, token;
    private String line;

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public TwitchIRC(String channel, String username, String token) {
        this.channel = "#" + channel;
        this.username = username;
        this.token = token;
    }

    public void connect() throws IOException {
        if (socket != null) {
            if (socket.isConnected()) {
                socket.close();
            }
        }

        socket = new Socket("irc.chat.twitch.tv", 6667);

        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                Charset.forName(StandardCharsets.UTF_8.name())));

        writer.write("PASS " + token + "\r\n");
        writer.write("NICK " + username + "\r\n");
        writer.flush();

        while ((line = reader.readLine()) != null) {
            if (line.contains("001")) {
                writer.write("JOIN " + channel + "\r\n");
                writer.flush();
                break;
            }
        }
    }

    public String getUpdates() throws IOException {
        if ((line = reader.readLine()) != null) {
            if (line.contains("PRIVMSG")) {
                return line.replaceAll("<", "&lt;")
                        // TODO: :|
                        .replaceAll(":(.*?)!(.*?)@(.*?) PRIVMSG #(.*?) :(.*?)",
                                "<b>$1</b> ✎ $5");
            } else if (line.contains("PING :tmi.twitch.tv")) {
                writer.write("PONG :tmi.twitch.tv\r\n");
                writer.flush();
                return "🏓 → PONG";
            } else {
                return "";
            }
        }

        return "null";
    }
}
