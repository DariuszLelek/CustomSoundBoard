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
    private int startOffset, endOffset;
    private int volume;
    private boolean looping;

    public SoundData(int column, int row, String name, MediaPlayer media, int startOffset, int endOffset, String fileName, boolean looping, int volume) {
        this.column = column;
        this.row = row;
        this.name = name;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.fileName = fileName;
        this.media = media;
        this.looping = looping;
        setVolume(volume);

        updateIndicatorDuration();
    }

    private void updateIndicatorDuration(){
        if (media != null) {
            duration = media.getDuration() - (startOffset + endOffset);
        }
    }

    public void setMedia(MediaPlayer media) {
        this.media = media;
        updateIndicatorDuration();
        updateVolume();
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

    public int getIndicatorDuration() {
        return duration;
    }

    public int getClipDuration(){
        return media != null ? media.getDuration() : 0;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        updateVolume();
    }

    private void updateVolume(){
        if(media != null){
            float vol = volume / 100f;
            media.setVolume(vol, vol);
        }
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
        updateIndicatorDuration();
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
        updateIndicatorDuration();
    }
}
