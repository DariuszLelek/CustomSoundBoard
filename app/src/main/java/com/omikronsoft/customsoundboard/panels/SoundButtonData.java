package com.omikronsoft.customsoundboard.panels;

import android.graphics.PointF;
import android.graphics.RectF;

import com.omikronsoft.customsoundboard.utils.SoundData;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundButtonData {
    private int row, column;
    private RectF area;
    private PointF center;
    private SoundData soundData;

    SoundButtonData(int column, int row) {
        this.row = row;
        this.column = column;
        soundData = new SoundData(column, row, "-", null, 0, "");
    }

    void processClick() {
        if (soundData != null && soundData.getMedia() != null) {
            SoundsPanelControl.getInstance().addPlayIndicator(new PlayIndicator(column, row, soundData.getDuration(), center, (int) area.width() / 3));
            AudioPlayer.getInstance().playWithOffset(soundData.getMedia(), soundData.getOffset());
        }
    }

    void setCenter(PointF center) {
        this.center = center;
    }

    void setArea(RectF area) {
        this.area = area;
    }

    public SoundData getSoundData() {
        return soundData;
    }

    void setSoundData(SoundData soundData) {
        this.soundData = soundData;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
