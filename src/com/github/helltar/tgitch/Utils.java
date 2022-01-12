package com.github.helltar.tgitch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static String getStringFromFile(String filename) {
        String line = "";

        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            Logger.add(e);
        }

        return line;
    }
}