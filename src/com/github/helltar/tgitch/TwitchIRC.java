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

    private String username, token, line;

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public TwitchIRC(String username, String token) {
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

        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(),
                        Charset.forName(StandardCharsets.UTF_8.name())));

        String channel = "#" + username;

        writer.write("PASS " + token + "\r\n");
        writer.write("NICK " + username + "\r\n");
        writer.flush();

        while ((line = reader.readLine()) != null) {
            if (line.indexOf("001") >= 0) {
                writer.write("JOIN " + channel + "\r\n");
                writer.flush();
                break;
            }
        }
    }

    public String getUpdates() throws IOException {
        if ((line = reader.readLine()) != null) {
            if (line.toLowerCase().startsWith("PING ")) {
                writer.write("PONG :tmi.twitch.tv\r\n");
                writer.flush();
                return "PONG tmi.twitch.tv";
            } else {
                if (line.indexOf("PRIVMSG") >= 0) {
                    return line.replaceAll("(.*?)PRIVMSG #(.*?) :(.*?)", "<b>$2</b>: $3");
                } else {
                    return "";
                }
            }
        }

        return "null";
    }
}
