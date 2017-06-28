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
    private PointF center, lightLoc;
    private SoundData soundData;

    SoundButtonData(int column, int row) {
        this.row = row;
        this.column = column;
        soundData = new SoundData(column, row, "", null, 0, 0, "", false, 100);
    }

    void processClick() {
        if (soundData != null && soundData.getMedia() != null) {
            int radius = (int) area.width() / 3 > (int) area.height() / 3 ? (int) area.height() / 3 : (int) area.width() / 3;

            if(soundData.isLooping()){
                if(AudioPlayer.getInstance().isLooping(soundData.getMedia())){
                    SoundsPanelControl.getInstance().stopIndicator(column, row);
                    AudioPlayer.getInstance().stopLoop(soundData.getMedia());
                }else{
                    SoundsPanelControl.getInstance().addIndicator(new LoopIndicator(column, row, center, radius, soundData.getIndicatorDuration(), lightLoc));
                    AudioPlayer.getInstance().loopWithOffset(soundData.getMedia(), soundData.getStartOffset(), soundData.getEndOffset());
                }
            }else{
                SoundsPanelControl.getInstance().addIndicator(new PlayIndicator(column, row, center, radius, soundData.getIndicatorDuration()));
                AudioPlayer.getInstance().playWithOffset(soundData.getMedia(), soundData.getStartOffset(),soundData.getEndOffset());
            }
        }
    }

    void setCenter(PointF center) {
        this.center = center;
    }

    void setLightLocation(PointF lightLoc){
        this.lightLoc = lightLoc;
    }

    public PointF getLightLoc() {
        return lightLoc;
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
