package com.example.doo88.pocketv;

/**
 * Created by doo88 on 2017-07-17.
 */

public class NDK {
    static {

        System.loadLibrary("ffmpeg");

    }

    public native int run_ffmpeg(String roomnumber);
}
