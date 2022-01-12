package com.github.helltar.tgitch;

public class Logger {

    public static void add(String msg) {
        System.out.println(msg);
    }

    public static void add(Exception e) {
        add(e.getMessage());
    }
}
