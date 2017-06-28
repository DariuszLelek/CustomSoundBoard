package com.omikronsoft.customsoundboard.utils;

import android.media.MediaPlayer;
import android.os.Looper;

import java.util.TimerTask;

import static android.R.attr.startOffset;

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

public class CustomTimerTask implements Runnable {
    MediaPlayer player;
    int start, end;

    public CustomTimerTask(MediaPlayer player, int start, int end) {
        this.player = player;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        AudioPlayer.getInstance().playWithOffset(player, start, end);
    }
}
