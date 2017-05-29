package com.omikronsoft.customsoundboard.painting;

import android.graphics.Paint;

import com.omikronsoft.customsoundboard.utils.Globals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dariusz Lelek on 5/22/2017.
 * dariusz.lelek@gmail.com
 */

public class PaintingResources {
    private static PaintingResources instance;
    private Map<PaintingResource, Paint> cachedPaints;
    private Map<Transparency, Paint> bitmapPaints;

    private PaintingResources() {
        cachedPaints = new HashMap<>();
        bitmapPaints = new HashMap<>();
    }

    public Paint getBitmapPaint(Transparency transparency) {
        Paint paint;
        if (bitmapPaints.containsKey(transparency)) {
            paint = bitmapPaints.get(transparency);
        } else {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            paint.setAlpha(transparency.value);
            bitmapPaints.put(transparency, paint);
        }
        return paint;
    }

    public Paint getTextPaintCenter(int width, int color, Transparency trans) {
        Paint paint;
        PaintingResource pr = new PaintingResource.Builder().color(color).width(width).transparency(trans.value).build();
        if (cachedPaints.containsKey(pr)) {
            paint = cachedPaints.get(pr);
        } else {
            paint = new Paint();
            paint.setTextSize(width * Globals.getInstance().getPixelDensity());
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAlpha(trans.value);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            cachedPaints.put(pr, paint);
        }

        if (paint.getTextAlign() != Paint.Align.CENTER) {
            paint.setTextAlign(Paint.Align.CENTER);
        }
        return paint;
    }

    public Paint getFillPaint(int color, Transparency trans) {
        Paint paint;
        PaintingResource pr = new PaintingResource.Builder().color(color).transparency(trans.value).build();
        if (cachedPaints.containsKey(pr)) {
            paint = cachedPaints.get(pr);
        } else {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAlpha(trans.value);
            paint.setAntiAlias(true);
            cachedPaints.put(pr, paint);
        }
        return paint;
    }

    public Paint getStrokePaint(int width, int color, Transparency trans, boolean densityWidth) {
        Paint paint;
        PaintingResource pr = new PaintingResource.Builder().color(color).transparency(trans.value).width(width).build();
        if (cachedPaints.containsKey(pr)) {
            paint = cachedPaints.get(pr);
        } else {
            paint = new Paint();
            paint.setStrokeWidth(densityWidth ? width * Globals.getInstance().getPixelDensity() : width);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setAlpha(trans.value);
            paint.setAntiAlias(true);
            cachedPaints.put(pr, paint);
        }
        return paint;
    }

    public synchronized static PaintingResources getInstance() {
        if (instance == null) {
            instance = new PaintingResources();
        }
        return instance;
    }
}
