package com.omikronsoft.customsoundboard.painting;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.TypedValue;

import com.omikronsoft.customsoundboard.R;
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
    private Resources res;
    private Bitmap loopBitmap;
    private int loopBitmapSize;

    private PaintingResources() {
        cachedPaints = new HashMap<>();
        bitmapPaints = new HashMap<>();

        res = Globals.getInstance().getResources();

        loopBitmap = BitmapFactory.decodeResource(res, R.drawable.arrows);
        loopBitmapSize = loopBitmap.getWidth();
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

    public Paint getTextPaintCenter(int spSize, int color, Transparency trans) {
        Paint paint;
        PaintingResource pr = new PaintingResource.Builder().color(color).width(spSize).transparency(trans.value).build();
        if (cachedPaints.containsKey(pr)) {
            paint = cachedPaints.get(pr);
        } else {
            paint = new Paint();
            paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spSize, res.getDisplayMetrics()));
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

    public Bitmap getLoopBitmap(int newSize) {
        if(newSize != loopBitmapSize){
            loopBitmapSize = newSize;

            int width = loopBitmap.getWidth();
            int height = loopBitmap.getHeight();
            float scaleWidth = ((float) newSize) / width;
            float scaleHeight = ((float) newSize) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(loopBitmap, 0, 0, width, height, matrix, false);
            loopBitmap.recycle();
            loopBitmap = resizedBitmap;
        }

        return loopBitmap;
    }

    public synchronized static PaintingResources getInstance() {
        if (instance == null) {
            instance = new PaintingResources();
        }
        return instance;
    }
}
