package com.omikronsoft.customsoundboard.panels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import com.omikronsoft.customsoundboard.OptionsActivity;
import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;
import com.omikronsoft.customsoundboard.utils.Globals;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class HeadPanelControl extends Panel implements IPanelControl {
    private static HeadPanelControl instance;
    private RectF stopButtonClickArea, stopButtonBackLight, stopButtonCenter,
            editButtonClickArea, editButtonBackLight, editButtonCenter,
            optionsButtonClickArea, optionsButtonBackLight, optionsButtonCenter;
    private final Paint backPaintNormal, centerPaint, textPaint, backPaintEdit;
    private final Globals globals;
    private Bitmap headPanel;

    private HeadPanelControl() {
        super();
        globals = Globals.getInstance();
        area = SoundBoardLayout.getInstance().getHeadPanelArea();

        int offset = backGroundOffset;
        super.backGroundArea = new RectF(area.left + offset, area.top + offset, area.right - offset, area.bottom - offset / 2);
        int buttonWidth = (int) backGroundArea.width() / 4;
        int buttonOffset = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.sound_buttons_offset));
        int outline = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.button_outline_width));

        stopButtonClickArea = new RectF(backGroundArea.left, backGroundArea.top, backGroundArea.left + buttonWidth, backGroundArea.bottom);
        stopButtonBackLight = new RectF(stopButtonClickArea.left + buttonOffset, stopButtonClickArea.top + buttonOffset,
                stopButtonClickArea.right - buttonOffset, stopButtonClickArea.bottom - buttonOffset);
        stopButtonCenter = new RectF(stopButtonBackLight.left + outline, stopButtonBackLight.top + outline,
                stopButtonBackLight.right - outline, stopButtonBackLight.bottom - outline);

        editButtonClickArea = new RectF(backGroundArea.left + 2 * buttonWidth, backGroundArea.top, backGroundArea.left + 3 * buttonWidth, backGroundArea.bottom);
        editButtonBackLight = new RectF(editButtonClickArea.left + buttonOffset, editButtonClickArea.top + buttonOffset,
                editButtonClickArea.right - buttonOffset, editButtonClickArea.bottom - buttonOffset);
        editButtonCenter = new RectF(editButtonBackLight.left + outline, editButtonBackLight.top + outline,
                editButtonBackLight.right - outline, editButtonBackLight.bottom - outline);

        optionsButtonClickArea = new RectF(backGroundArea.left + 3 * buttonWidth, backGroundArea.top, backGroundArea.left + 4 * buttonWidth, backGroundArea.bottom);
        optionsButtonBackLight = new RectF(optionsButtonClickArea.left + buttonOffset, optionsButtonClickArea.top + buttonOffset,
                optionsButtonClickArea.right - buttonOffset, optionsButtonClickArea.bottom - buttonOffset);
        optionsButtonCenter = new RectF(optionsButtonBackLight.left + outline, optionsButtonBackLight.top + outline,
                optionsButtonBackLight.right - outline, optionsButtonBackLight.bottom - outline);

        Context context = ApplicationContext.get();
        backPaintNormal = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light), Transparency.HALF);
        backPaintEdit = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light_edit), Transparency.HALF);
        centerPaint = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_color), Transparency.OPAQUE);
        textPaint = PaintingResources.getInstance().getTextPaintCenter(Globals.getInstance().getResources().getInteger(R.integer.button_text_sp), Color.WHITE, Transparency.OPAQUE);

        headPanel = Bitmap.createBitmap((int) area.width(), (int) area.height(), Bitmap.Config.ARGB_8888);
        prepareHeadPanel();
    }


    @Override
    public void drawPanel(Canvas canvas) {
        super.drawPanelBackGround(canvas);
        canvas.drawBitmap(headPanel, 0, 0, PaintingResources.getInstance().getBitmapPaint(Transparency.OPAQUE));
    }

    @Override
    public void processClick(float x, float y) {
        if (stopButtonClickArea.contains(x, y)) {
            stopAll();
            return;
        }

        if (editButtonClickArea.contains(x, y)) {
            stopAll();
            globals.switchEditMode();
            prepareHeadPanel();
            SoundsPanelControl.getInstance().prepareSoundsBoard();
            return;
        }

        if (optionsButtonClickArea.contains(x, y)) {
            stopAll();
            Globals.getInstance().setDataLoading(true);
            Context context = ApplicationContext.get();
            Intent i = new Intent(context, OptionsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    @Override
    public RectF getPanelArea() {
        return area;
    }

    private void prepareHeadPanel() {
        headPanel.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(headPanel);

        canvas.drawRect(stopButtonBackLight, backPaintNormal);
        canvas.drawRect(stopButtonCenter, centerPaint);
        canvas.drawText("Stop", stopButtonCenter.centerX(), stopButtonCenter.centerY(), textPaint);

        canvas.drawRect(editButtonBackLight, Globals.getInstance().isEditMode() ? backPaintEdit : backPaintNormal);
        canvas.drawRect(editButtonCenter, centerPaint);
        canvas.drawText("Edit", editButtonCenter.centerX(), editButtonCenter.centerY(), textPaint);

        canvas.drawRect(optionsButtonBackLight, backPaintNormal);
        canvas.drawRect(optionsButtonCenter, centerPaint);
        canvas.drawText("Options", optionsButtonCenter.centerX(), optionsButtonCenter.centerY(), textPaint);
    }

    private void stopAll() {
        AudioPlayer.getInstance().stopAll();
        SoundsPanelControl.getInstance().stopIndicators();
    }

    public synchronized static HeadPanelControl getInstance() {
        if (instance == null) {
            instance = new HeadPanelControl();
        }
        return instance;
    }
}
