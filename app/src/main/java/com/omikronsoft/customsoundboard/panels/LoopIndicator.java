package com.omikronsoft.customsoundboard.panels;

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

/**
 * Created by Dariusz Lelek on 6/28/2017.
 * dariusz.lelek@gmail.com
 */

class LoopIndicator extends Indicator{
    private int radius2;
    private boolean playing;
    private PointF lightLoc;
    private Paint lightPaint;
    private String loopLightIndicator;
    private long counter;
    private Paint paint;

    LoopIndicator(int column, int row, PointF center, int radius, int playDuration, PointF lightLoc) {
        super(column, row, center, radius, playDuration);

        this.lightLoc = lightLoc;

        counter = 0;
        radius2 = radius / 2;
        playing = true;
        paint = PaintingResources.getInstance().getStrokePaint(radius / 3, Color.WHITE, Transparency.LOW, false);
        lightPaint = PaintingResources.getInstance().getTextPaintCenter(Globals.getInstance().getLightSize(),
                ContextCompat.getColor(ApplicationContext.get(), R.color.button_back_light_edit) , Transparency.OPAQUE);

        loopLightIndicator = Globals.getInstance().getResources().getString(R.string.loop_light_indicator);
    }

    @Override
    public void draw(Canvas canvas) {
        counter++;
        counter = counter > 360 ? 0 : counter % 360;

        ArcUtils.drawArc(canvas, center, radius2, 45 + counter, 90 , paint);
        ArcUtils.drawArc(canvas, center, radius2, 225 + counter, 90 , paint);

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
        counter = 0;
    }
}
