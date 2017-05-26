package com.omikronsoft.customsoundboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.panels.HeadPanelControl;
import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.Globals;

/**
 * Created by Dariusz Lelek on 5/22/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundBoardActivityControl extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean canDraw;
    private SurfaceHolder surfaceHolder;
    private HeadPanelControl headPanelCtrl;
    private SoundsPanelControl soundsPanelCtrl;
    private Bitmap backGround;

    private final Globals globals;
    private final PaintingResources paintRes = PaintingResources.getInstance();

    private final int FRAME_PERIOD = 1000 / Globals.getMaxFps(); // the frame period

    public SoundBoardActivityControl(Context context) {
        super(context);

        globals = Globals.getInstance();
        thread = null;
        canDraw = false;
        surfaceHolder = getHolder();


        backGround = BitmapFactory.decodeResource(globals.getResources(), R.drawable.back);
        float xScale, yScale;
        xScale = globals.getScreenWidth() / (float)backGround.getWidth();
        yScale = globals.getScreenHeight() / (float)backGround.getHeight();
        backGround = Bitmap.createScaledBitmap(backGround, (int)(backGround.getWidth() * xScale), (int)(backGround.getHeight() * yScale), true);

        headPanelCtrl = HeadPanelControl.getInstance();
        soundsPanelCtrl = SoundsPanelControl.getInstance();
    }

    @Override
    public void run() {
        while (canDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            long started = System.currentTimeMillis();

            Canvas canvas = surfaceHolder.lockCanvas();

            if(!globals.isDataPrepared()){
                // show splash screen
                globals.prepareData();
            }else{
                canvas.drawRect(0,0, globals.getScreenWidth(), globals.getScreenHeight(), paintRes.getFillPaint(Color.BLACK, Transparency.OPAQUE));
                canvas.drawBitmap(backGround, 0,0, paintRes.getBitmapPaint(Transparency.OPAQUE));

                headPanelCtrl.drawPanel(canvas);
                soundsPanelCtrl.drawPanel(canvas);
            }

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
