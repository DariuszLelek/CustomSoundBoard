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
import android.widget.Button;

import com.omikronsoft.customsoundboard.EditButtonActivity;
import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.SoundData;
import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;
import com.omikronsoft.customsoundboard.utils.UpdateData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.R.attr.data;
import static android.R.id.list;

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
    private List<PlayIndicator> playIndicators;
    private final Paint backPaintNormal, backPaintEdit, centerPaint, textPaint;

    private Canvas canvas;

    private SoundsPanelControl(){
        super();
        area = SoundBoardLayout.getInstance().getSoundsPanelArea();

        columns = Globals.getInstance().getResources().getInteger(R.integer.sound_button_columns);
        columnWidth = Globals.getInstance().getScreenWidth() / columns;
        rows = Globals.getInstance().getResources().getInteger(R.integer.sound_button_rows);
        rowHeight = (int) area.height() / rows;
        int offset = backGroundOffset;
        backGroundArea = new RectF(area.left + offset, area.top + offset/2, area.right - offset, area.bottom - offset);
        playIndicators = new LinkedList<>();

        Context context = ApplicationContext.get();
        backPaintNormal = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light), Transparency.HALF);
        backPaintEdit = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_back_light_edit), Transparency.HALF);
        centerPaint = PaintingResources.getInstance().getFillPaint(ContextCompat.getColor(context, R.color.button_color), Transparency.OPAQUE);
        textPaint = PaintingResources.getInstance().getTextPaintCenter(15, Color.WHITE, Transparency.OPAQUE);

        readSoundData();
        prepareSoundsBoard();
    }

    public void updateButtonData(SoundButtonData sbd){
        // trigger save sbd
        soundData[sbd.getColumn()][sbd.getRow()] = sbd.getSoundData();
        prepareSoundsBoard();
    }

    @Override
    public void processClick(float x, float y) {
        int row = (int)y / rowHeight;
        int col = (int)x / columnWidth;

        if(row < rows && col < columns){
            SoundButtonData sbd = soundButtonData[col][row];
            if(sbd != null){
                if(!Globals.getInstance().isEditMode()){
                    sbd.processClick();
                }else{
                    Globals.getInstance().setDataLoading(true);
                    Globals.getInstance().setEditedButton(sbd);
                    Context context = ApplicationContext.get();
                    Intent i = new Intent(context, EditButtonActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("ButtonCoords", col + 1 + "," + row + 1);
//                    i.putExtra("Column", row);
//                    i.putExtra("Row", col);
//                    i.putExtra("Name", sbd.getSoundData().getName());
                    context.startActivity(i);
                    //sbd.processClick();
                }
            }
        }
    }

    @Override
    public void drawPanel(Canvas canvas) {
        super.drawPanelBackGround(canvas);
        //canvas.drawBitmap(soundsBoard, backGroundArea.left, backGroundArea.top, PaintingResources.getInstance().getBitmapPaint(Transparency.OPAQUE));

        if(Globals.getInstance().isDataLoading()){
            //canvas.drawRect(backGroundArea, PaintingResources.getInstance().getFillPaint(Color.WHITE, Transparency.LOW));
            canvas.drawText("LOADING ...", backGroundArea.centerX(), backGroundArea.centerY(),
                    PaintingResources.getInstance().getTextPaintCenter(35, Color.WHITE, Transparency.HALF));
        }else{
            canvas.drawBitmap(soundsBoard, backGroundArea.left, backGroundArea.top, PaintingResources.getInstance().getBitmapPaint(Transparency.OPAQUE));
            drawPlayIndicators(canvas);
        }
    }

    @Override
    public RectF getPanelArea() {
        return area;
    }

    public synchronized void addPlayIndicator(PlayIndicator pi){
        if(playIndicators.contains(pi)){
            playIndicators.get(playIndicators.indexOf(pi)).reset();
        }else{
            playIndicators.add(pi);
        }
    }

    private synchronized void drawPlayIndicators(Canvas canvas){
        for(PlayIndicator pi : playIndicators){
            if(pi.isPlaying()){
                pi.draw(canvas);
            }
        }
    }

    public void stopPlayIndicators(){
        for(PlayIndicator pi : playIndicators){
            pi.stop();
        }
    }

    private void readSoundData(){
        Globals.getInstance().setDataLoading(true);
        soundData = SoundDataStorageControl.getInstance().readSavedSoundsData();
    }

    public void prepareSoundsBoard(){
        soundButtonData = new SoundButtonData[columns][rows];
        soundsBoard = Bitmap.createBitmap((int)backGroundArea.width(), (int)backGroundArea.height(), Bitmap.Config.ARGB_8888);
        soundsBoard.eraseColor(Color.TRANSPARENT);
        canvas = new Canvas(soundsBoard);

        int buttonWidth, buttonHeight;
        int offset = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.sound_buttons_offset));
        int outline = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.button_outline_width));
        buttonWidth = (soundsBoard.getWidth() / columns) - 2 * offset;
        buttonHeight = (soundsBoard.getHeight() / rows) - 2 * offset;

        PointF buttonCenter;

        int top, left, right, bottom;

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                left = (i * (buttonWidth + 2*offset)) + offset;
                top = (j * (buttonHeight + 2*offset)) + offset;
                right = left + buttonWidth;
                bottom = top + buttonHeight;
                buttonCenter = new PointF(backGroundArea.left + left + buttonWidth / 2,backGroundArea.top + top + buttonHeight / 2);

                SoundButtonData sbd = new SoundButtonData(i, j);
                sbd.setArea(new RectF(left, backGroundArea.top + top, right, backGroundArea.top + bottom));
                sbd.setCenter(buttonCenter);
                if(soundData[i][j] != null){
                    sbd.setSoundData(soundData[i][j]);
                }

                soundButtonData[i][j] = sbd;

                // button back
                canvas.drawRect(left, top, right, bottom, Globals.getInstance().isEditMode() ? backPaintEdit : backPaintNormal);

                // button center
                canvas.drawRect(left + outline, top + outline, right - outline, bottom - outline, centerPaint);

                // button label
                canvas.drawText(sbd.getSoundData().getName(), left + buttonWidth/2, top + buttonHeight/2, textPaint);
            }
        }
        Globals.getInstance().setDataLoading(false);
    }

    public synchronized static SoundsPanelControl getInstance() {
        if (instance == null) {
            instance = new SoundsPanelControl();
        }
        return instance;
    }
}
