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
        socket = new Socket("irc.chat.twitch.tv", 6667);

        writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(),
                        Charset.forName(StandardCharsets.UTF_8.name())));

        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(),
                        Charset.forName(StandardCharsets.UTF_8.name())));

        String channel = "#" + username;

        writer.write("PASS " + token + "\r\n");
        writer.write("NICK " + username + "\r\n");
        writer.flush();

        while ((line = reader.readLine()) != null) {
            if (line.indexOf("004") >= 0) {
                // logged
                break;
            } else if (line.indexOf("433") >= 0) {
                Logger.add("nickname is already in use");
            }
        }

        writer.write("JOIN " + channel + "\r\n");
        writer.flush();
    }

    public String getUpdates() throws IOException {
        String line = reader.readLine();

        if (line != null) {
            if (line.toLowerCase().startsWith("PING ")) {
                writer.write("PONG " + line.substring(5) + "\r\n");
                writer.flush();
            } else {
                return line.replaceAll("(.*?)PRIVMSG #(.*?) :(.*?)", "<b>$2</b>: $3");
            }
        }

        return "";
    }
}
