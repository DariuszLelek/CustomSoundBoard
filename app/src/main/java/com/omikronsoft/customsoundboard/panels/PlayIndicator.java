package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ArcUtils;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

class PlayIndicator extends Indicator {
    private int radius2;
    private long playStart, playEnd;
    private Paint paint;

    PlayIndicator(int column, int row, PointF center, int radius, int playDuration) {
        super(column, row, center, radius, playDuration);

        radius2 = radius / 2;
        paint = PaintingResources.getInstance().getStrokePaint(radius / 3, Color.WHITE, Transparency.LOW, false);

        reset();
    }

    @Override
    public void reset() {
        playStart = System.currentTimeMillis();
        playEnd = playStart + playDuration;
    }

    @Override
    public void stop() {
        playEnd = 0;
    }

    @Override
    public boolean isPlaying() {
        long currentTime = System.currentTimeMillis();
        return playEnd > currentTime;
    }

    @Override
    public void draw(Canvas canvas) {
        ArcUtils.drawArc(canvas, center, radius2, 270, 360 * (float) (System.currentTimeMillis() - playStart) / playDuration, paint);
    }
}
