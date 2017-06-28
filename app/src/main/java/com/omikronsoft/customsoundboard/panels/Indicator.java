package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

abstract class Indicator {
    int column, row, radius, playDuration;
    PointF center;

    Indicator(int column, int row, PointF center, int radius, int playDuration) {
        this.column = column;
        this.row = row;
        this.center = center;
        this.radius = radius;
        this.playDuration = playDuration;
    }

    public abstract void draw(Canvas canvas);
    public abstract boolean isPlaying();
    public abstract void stop();
    public abstract void reset();

    int getPlayDuration(){
        return playDuration;
    }

    void setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Indicator that = (Indicator) o;

        return column == that.column && row == that.row;

    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }
}
