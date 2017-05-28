package com.omikronsoft.customsoundboard;

import android.media.MediaPlayer;
import android.net.Uri;

import com.omikronsoft.customsoundboard.utils.ApplicationContext;

import java.net.URI;

import static android.R.attr.path;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundData {
    int column, row;
    private String name;
    private Uri path;
    private MediaPlayer media;
    private int duration;
    private int delay;

    public SoundData(int column, int row, String name, Uri path, int delay) {
        this.column = column;
        this.row = row;
        this.name = name;
        this.path = path;
        this.delay = delay;

        if(path != null){
            media = MediaPlayer.create(ApplicationContext.get(), path);
            if(media != null){
                duration = media.getDuration();
            }
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    public MediaPlayer getMedia() {
        return media;
    }

    public int getDuration() {
        return duration;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
