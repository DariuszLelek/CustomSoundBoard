package com.omikronsoft.customsoundboard.panels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.Globals;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundsPanelControl extends Panel implements IPanelControl {
    private static SoundsPanelControl instance;
    private int columns, rows;
    private SoundData[][] soundData;
    private Bitmap soundsBoard;

    private SoundsPanelControl(){
        super();

        columns = Globals.getInstance().getResources().getInteger(R.integer.sound_button_columns);
        rows = Globals.getInstance().getResources().getInteger(R.integer.sound_button_rows);

        soundData = new SoundData[columns][rows];

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                soundData[i][j] = new SoundData();
            }
        }

        area = SoundBoardLayout.getInstance().getSoundsPanelArea();
        int offset = backGroundOffset;
        backGroundArea = new RectF(area.left + offset, area.top + offset/2, area.right - offset, area.bottom - offset);

        prepareSoundsBoard();
    }

    @Override
    public void drawPanel(Canvas canvas) {
        super.drawPanelBackGround(canvas);
        canvas.drawBitmap(soundsBoard, backGroundArea.left, backGroundArea.top, PaintingResources.getInstance().getBitmapPaint(Transparency.OPAQUE));
    }

    @Override
    public RectF getPanelArea() {
        return area;
    }

    private void prepareSoundsBoard(){
        soundsBoard = Bitmap.createBitmap((int)backGroundArea.width(), (int)backGroundArea.height(), Bitmap.Config.ARGB_8888);
        soundsBoard.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(soundsBoard);

        int buttonWidth, buttonHeight;
        int offset = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.sound_buttons_offset));
        int outline = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.button_outline_width));
        buttonWidth = (soundsBoard.getWidth() / columns) - 2 * offset;
        buttonHeight = (soundsBoard.getHeight() / rows) - 2 * offset;

        Context context = ApplicationContext.get();
        Paint backPaint = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light), Transparency.HALF);
        Paint centerPaint = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_color), Transparency.OPAQUE);

        int top, left, right, bottom;

        //canvas.drawRect(0,0, soundsBoard.getWidth(), soundsBoard.getHeight(), PaintingResources.getInstance().getFillPaint(Color.GREEN, Transparency.OPAQUE));

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                left = (i * (buttonWidth + 2*offset)) + offset;
                top = (j * (buttonHeight + 2*offset)) + offset;
                right = left + buttonWidth;
                bottom = top + buttonHeight;

                // button back
                canvas.drawRect(left, top, right, bottom, backPaint);

                // button center
                canvas.drawRect(left + outline, top + outline, right - outline, bottom - outline, centerPaint);

            }
        }

    }

    public synchronized static SoundsPanelControl getInstance() {
        if (instance == null) {
            instance = new SoundsPanelControl();
        }
        return instance;
    }
}
