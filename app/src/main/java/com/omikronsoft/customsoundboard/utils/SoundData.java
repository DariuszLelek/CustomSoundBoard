package com.omikronsoft.customsoundboard.utils;

import android.media.MediaPlayer;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundData {
    private int column, row;
    private String name;
    private String fileName;
    private StorageLocation storageLoc;
    private MediaPlayer media;
    private int duration;
    private int offset;
    private boolean looping;

    public SoundData(int column, int row, String name, MediaPlayer media, int offset, String fileName, boolean looping) {
        this.column = column;
        this.row = row;
        this.name = name;
        this.offset = offset;
        this.fileName = fileName;
        this.media = media;
        this.looping = looping;
        updateDuration();
    }

    private void updateDuration(){
        if (media != null) {
            duration = media.getDuration() - offset;
        }
    }

    public void setMedia(MediaPlayer media) {
        this.media = media;
        updateDuration();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        updateDuration();
    }

    public String getName() {
        return name;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setName(String name) {
        this.name = name;
    }

    StorageLocation getStorageLoc() {
        return storageLoc;
    }

    public void setStorageLoc(StorageLocation storageLoc) {
        this.storageLoc = storageLoc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MediaPlayer getMedia() {
        return media;
    }

    public int getDuration() {
        return duration;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }


}
