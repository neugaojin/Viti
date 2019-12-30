package com.gaojin.viti;

/**
 * Author: gaojin
 * Time: 2019-11-24 17:21
 */

public class Log {

    public static void info(String tag, String msg) {
        System.out.println(tag + ": " + msg);
    }

    public static void info(String tag, int msg) {
        info(tag, String.valueOf(msg));
    }
}
