package com.omikronsoft.customsoundboard;

import android.media.MediaPlayer;

import com.omikronsoft.customsoundboard.utils.StorageLocation;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundData {
    int column, row;
    private String name;
    private String fileName;
    private StorageLocation storageLoc;
    private MediaPlayer media;
    private int duration;
    private int offset;

    public SoundData(int column, int row, String name, MediaPlayer media, int offset, String fileName) {
        this.column = column;
        this.row = row;
        this.name = name;
        this.offset = offset;
        this.fileName = fileName;
        this.media = media;
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageLocation getStorageLoc() {
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
