package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;

import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ArcUtils;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

class PlayIndicator {
    private int column, row;
    private PointF center;
    private int radius, radius2;
    private long playStart, playDuration, playEnd;

    PlayIndicator(int column, int row, int playDuration, PointF center, int radius) {
        this.playDuration = playDuration;
        this.radius = radius;
        this.center = center;
        this.column = column;
        this.row = row;

        radius2 = radius / 2;
        reset();
    }

    void reset() {
        playStart = System.currentTimeMillis();
        playEnd = playStart + playDuration;
    }

    void stop() {
        playEnd = 0;
    }

    boolean isPlaying() {
        long currentTime = System.currentTimeMillis();
        return playEnd > currentTime;
    }

    void draw(Canvas canvas) {
        ArcUtils.drawArc(canvas, center, radius2, 270, 360 * (float) (System.currentTimeMillis() - playStart) / playDuration,
                PaintingResources.getInstance().getStrokePaint(radius, Color.WHITE, Transparency.LOW, false));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayIndicator that = (PlayIndicator) o;

        return column == that.column && row == that.row && playDuration == that.playDuration;

    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        result = 31 * result + (int) (playDuration ^ (playDuration >>> 32));
        return result;
    }
}
