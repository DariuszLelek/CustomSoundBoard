package com.omikronsoft.customsoundboard.panels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;

import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ArcUtils;

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

public class LoopIndicator extends Indicator{
    private int radius2, bitmapX, bitmapY;
    private Bitmap loopBitmap;
    private boolean playing;

    LoopIndicator(int column, int row, PointF center, int radius, int playDuration) {
        super(column, row, center, radius, playDuration);

        playing = true;
        radius2 = radius / 2;
        loopBitmap = PaintingResources.getInstance().getLoopBitmap(2 * radius);

        bitmapX = (int) center.x - loopBitmap.getWidth() / 2;
        bitmapY = (int) center.y - loopBitmap.getHeight() / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(loopBitmap, bitmapX, bitmapY, PaintingResources.getInstance().getBitmapPaint(Transparency.LOW));
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public void reset() {
        playing = true;
    }
}
