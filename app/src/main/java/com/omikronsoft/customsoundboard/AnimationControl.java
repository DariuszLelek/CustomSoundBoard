package com.omikronsoft.customsoundboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Dariusz Lelek on 5/22/2017.
 * dariusz.lelek@gmail.com
 */

public class AnimationControl extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean canDraw;
    private SurfaceHolder surfaceHolder;

    private final Globals globals;
    private final PaintingResources paintRes = PaintingResources.getInstance();

    private final int FRAME_PERIOD = 1000 / Globals.getMaxFps(); // the frame period

    public AnimationControl(Context context) {
        super(context);

        globals = Globals.getInstance();
        thread = null;
        canDraw = false;
        surfaceHolder = getHolder();
    }

    @Override
    public void run() {
        while (canDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            long started = System.currentTimeMillis();

            Canvas canvas = surfaceHolder.lockCanvas();


            surfaceHolder.unlockCanvasAndPost(canvas);

            float deltaTime = (System.currentTimeMillis() - started);

            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0) {
                try {
                    thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        return super.onTouchEvent(event);
    }

    public void pause() {
        canDraw = false;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume() {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }

}
