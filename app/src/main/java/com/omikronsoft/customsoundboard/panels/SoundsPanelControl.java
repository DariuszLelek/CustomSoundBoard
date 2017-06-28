package com.omikronsoft.customsoundboard.panels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

import com.omikronsoft.customsoundboard.EditButtonActivity;
import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.utils.SoundData;
import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundsPanelControl extends Panel implements IPanelControl {
    private static SoundsPanelControl instance;
    private int columns, rows, columnWidth, rowHeight;
    private SoundButtonData[][] soundButtonData;
    private SoundData[][] soundData;
    private Bitmap soundsBoard;
    private List<Indicator> indicators;
    private final Paint backPaintNormal, backPaintEdit, centerPaint;
    private Paint textPaint, testInfoPaint;

    private SoundsPanelControl() {
        super();
        area = SoundBoardLayout.getInstance().getSoundsPanelArea();

        columns = Globals.getInstance().getColumns();
        columnWidth = Globals.getInstance().getScreenWidth() / columns;
        rows = Globals.getInstance().getRows();
        rowHeight = (int) area.height() / rows;
        int offset = backGroundOffset;
        backGroundArea = new RectF(area.left + offset, area.top + offset / 2, area.right - offset, area.bottom - offset);
        indicators = new LinkedList<>();

        Context context = ApplicationContext.get();
        backPaintNormal = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light), Transparency.HALF);
        backPaintEdit = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light_edit), Transparency.HALF);
        centerPaint = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_color), Transparency.OPAQUE);

        readSoundData();
        prepareSoundsBoard();
    }

    public void updateButtonData(SoundButtonData sbd) {
        SoundData sd = sbd.getSoundData();

        SoundDataStorageControl.getInstance().saveSoundData(sd);
        soundData[sbd.getColumn()][sbd.getRow()] = sd;
        prepareSoundsBoard();
    }

    public void readSoundData() {
        soundData = SoundDataStorageControl.getInstance().readSavedSoundsData();
    }

    public void prepareSoundsBoard() {
        Globals.getInstance().setDataLoading(true);
        soundButtonData = new SoundButtonData[columns][rows];
        soundsBoard = Bitmap.createBitmap((int) backGroundArea.width(), (int) backGroundArea.height(), Bitmap.Config.ARGB_8888);
        soundsBoard.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(soundsBoard);

        int buttonWidth, buttonHeight, buttonWidth2;
        int offset = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.sound_buttons_offset));
        int outline = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.button_outline_width));
        buttonWidth = (soundsBoard.getWidth() / columns) - 2 * offset;
        buttonHeight = (soundsBoard.getHeight() / rows) - 2 * offset;
        buttonWidth2 = buttonWidth / 2;

        // experimenting with size
        int textSize = buttonWidth / 8;

        //Globals.getInstance().getResources().getInteger(R.integer.button_text_sp)

        textPaint = PaintingResources.getInstance().getTextPaintCenter(textSize, Color.WHITE, Transparency.OPAQUE);
        testInfoPaint = PaintingResources.getInstance().getTextPaintCenter(textSize/2, Color.WHITE, Transparency.OPAQUE);

        PointF buttonCenter;

        int top, left, right, bottom;

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                left = (i * (buttonWidth + 2 * offset)) + offset;
                top = (j * (buttonHeight + 2 * offset)) + offset;
                right = left + buttonWidth;
                bottom = top + buttonHeight;
                buttonCenter = new PointF(backGroundArea.left + left + buttonWidth2, backGroundArea.top + top + buttonHeight / 2);

                SoundButtonData sbd = new SoundButtonData(i, j);
                sbd.setArea(new RectF(left, backGroundArea.top + top, right, backGroundArea.top + bottom));
                sbd.setCenter(buttonCenter);
                if (soundData[i][j] != null) {
                    sbd.setSoundData(soundData[i][j]);
                }

                soundButtonData[i][j] = sbd;

                // button back
                canvas.drawRect(left, top, right, bottom, Globals.getInstance().isEditMode() ? backPaintEdit : backPaintNormal);

                // button center
                canvas.drawRect(left + outline, top + outline, right - outline, bottom - outline, centerPaint);

                // button label
                canvas.drawText(sbd.getSoundData().getName(), left + buttonWidth2, top + buttonHeight / 2, textPaint);

                if(sbd.getSoundData().isLooping()){
                    // button info
                    canvas.drawText("LOOP", left + buttonWidth2, top + buttonHeight / 1.2f, testInfoPaint);
                }
            }
        }
        Globals.getInstance().setDataLoading(false);
    }

    @Override
    public void processClick(float x, float y) {
        int row = (int) y / rowHeight;
        int col = (int) x / columnWidth;

        if (row < rows && col < columns) {
            SoundButtonData sbd = soundButtonData[col][row];
            if (sbd != null) {
                if (!Globals.getInstance().isEditMode()) {
                    sbd.processClick();
                } else {
                    Globals.getInstance().setDataLoading(true);
                    Globals.getInstance().setEditedButton(sbd);
                    SoundDataStorageControl.getInstance().loadData();
                    Context context = ApplicationContext.get();
                    Intent i = new Intent(context, EditButtonActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        }
    }

    @Override
    public void drawPanel(Canvas canvas) {
        super.drawPanelBackGround(canvas);

        if (Globals.getInstance().isDataLoading()) {
            canvas.drawText("LOADING ...", backGroundArea.centerX(), backGroundArea.centerY(),
                    PaintingResources.getInstance().getTextPaintCenter(35, Color.WHITE, Transparency.HALF));
        } else {
            canvas.drawBitmap(soundsBoard, backGroundArea.left, backGroundArea.top, PaintingResources.getInstance().getBitmapPaint(Transparency.OPAQUE));
            drawIndicators(canvas);
        }
    }

    @Override
    public RectF getPanelArea() {
        return area;
    }

    synchronized void addIndicator(Indicator i) {
        if (indicators.contains(i)) {
            Indicator cachedIndicator = indicators.get(indicators.indexOf(i));
            if(cachedIndicator.getPlayDuration() != i.getPlayDuration()){
                cachedIndicator.setPlayDuration(i.getPlayDuration());
            }
            cachedIndicator.reset();
        } else {
            indicators.add(i);
        }
    }

    private synchronized void drawIndicators(Canvas canvas) {
        for (Indicator i : indicators) {
            if (i.isPlaying()) {
                i.draw(canvas);
            }
        }
    }

    void stopIndicator(int col, int row){
        for (Indicator i : indicators) {
            if (i.isPlaying() && i.column == col && i.row == row) {
                i.stop();
                break;
            }
        }
    }

    void stopIndicators() {
        for (Indicator i : indicators) {
            i.stop();
        }
    }

    public synchronized static SoundsPanelControl getInstance() {
        if (instance == null) {
            instance = new SoundsPanelControl();
        }
        return instance;
    }
}
