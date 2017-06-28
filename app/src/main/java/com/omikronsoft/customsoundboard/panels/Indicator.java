package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

public abstract class Indicator {
    protected int column, row, radius, playDuration;
    protected PointF center;

    public Indicator(int column, int row, PointF center, int radius, int playDuration) {
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

    public int getPlayDuration(){
        return playDuration;
    }

    public void setPlayDuration(int playDuration) {
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
