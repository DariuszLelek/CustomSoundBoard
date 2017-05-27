package com.omikronsoft.customsoundboard.layouts;

import android.graphics.RectF;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.utils.Globals;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundBoardLayout {
    private static SoundBoardLayout instance;
    private Globals globals;
    private RectF soundsPanelArea, headPanelArea;
    int editAreaHeight, bottomAddHeight;


    private SoundBoardLayout(){
        globals = Globals.getInstance();
        int screenWidth = globals.getScreenWidth();
        int screenHeight = globals.getScreenHeight();
        editAreaHeight =  screenWidth / (100 / globals.getResources().getInteger(R.integer.hp_height_width_percent));
        bottomAddHeight = Globals.ADS_ENABLED ? Globals.getInstance().getPixelSize(Globals.ADD_HEIGHT) : 0;

        headPanelArea = new RectF(0,0, screenWidth, editAreaHeight);
        soundsPanelArea = new RectF(0, editAreaHeight, screenWidth, screenHeight - bottomAddHeight);
    }

    public int getEditAreaHeight() {
        return editAreaHeight;
    }

    public int getBottomAddHeight() {
        return bottomAddHeight;
    }

    public RectF getSoundsPanelArea() {
        return soundsPanelArea;
    }

    public RectF getHeadPanelArea() {
        return headPanelArea;
    }

    public synchronized static SoundBoardLayout getInstance() {
        if (instance == null) {
            instance = new SoundBoardLayout();
        }
        return instance;
    }
}
