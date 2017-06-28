package com.omikronsoft.customsoundboard.panels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.ArcUtils;
import com.omikronsoft.customsoundboard.utils.Globals;

import static android.R.attr.textSize;

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

public class LoopIndicator extends Indicator{
    private int radius2, bitmapX, bitmapY;
    private Bitmap loopBitmap;
    private boolean playing;
    private PointF lightLoc;
    private Paint lightPaint;
    private String loopLightIndicator;

    LoopIndicator(int column, int row, PointF center, int radius, int playDuration, PointF lightLoc) {
        super(column, row, center, radius, playDuration);

        this.lightLoc = lightLoc;
        playing = true;
        radius2 = radius / 2;
        loopBitmap = PaintingResources.getInstance().getLoopBitmap(2 * radius);
        lightPaint = PaintingResources.getInstance().getTextPaintCenter(Globals.getInstance().getLightSize(),
                ContextCompat.getColor(ApplicationContext.get(), R.color.button_back_light_edit) , Transparency.OPAQUE);
        loopLightIndicator = Globals.getInstance().getResources().getString(R.string.loop_light_indicator);

        bitmapX = (int) center.x - loopBitmap.getWidth() / 2;
        bitmapY = (int) center.y - loopBitmap.getHeight() / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(loopBitmap, bitmapX, bitmapY, PaintingResources.getInstance().getBitmapPaint(Transparency.LOW));

        if(lightLoc != null){
            canvas.drawText(loopLightIndicator, lightLoc.x, lightLoc.y, lightPaint);
        }
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
