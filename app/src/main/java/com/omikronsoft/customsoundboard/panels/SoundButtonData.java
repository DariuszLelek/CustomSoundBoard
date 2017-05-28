package com.omikronsoft.customsoundboard.panels;

import android.graphics.PointF;
import android.graphics.RectF;

import com.omikronsoft.customsoundboard.SoundData;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;
import com.omikronsoft.customsoundboard.utils.Globals;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundButtonData {
    private String label;
    private boolean empty;
    private int row, column;
    private RectF area;
    private PointF center;
    private SoundData soundData;

    public SoundButtonData(int column, int row) {
        this.row = row;
        this.column = column;
        soundData = new SoundData(column, row, "-", null, 0);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void processClick(){
            if (soundData != null && soundData.getMedia() != null) {
                SoundsPanelControl.getInstance().addPlayIndicator(new PlayIndicator(column, row, soundData.getDuration(), center, (int) area.width() / 3));
                AudioPlayer.getInstance().playSound(soundData.getMedia());
            }
    }

    public void setCenter(PointF center){
        this.center = center;
    }

    public void setArea(RectF area){
        this.area = area;
    }

    public RectF getArea() {
        return area;
    }

    public SoundData getSoundData() {
        return soundData;
    }

    public void setSoundData(SoundData soundData) {
        this.soundData = soundData;
        label = soundData.getName();
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
