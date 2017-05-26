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


    private SoundBoardLayout(){
        globals = Globals.getInstance();
        int screenWidth = globals.getScreenWidth();
        int screenHeight = globals.getScreenHeight();
        int editAreaHeight =  screenWidth / (100 / globals.getResources().getInteger(R.integer.hp_height_width_percent));
        int bottomAddOffset = Globals.ADS_ENABLED ? Globals.getInstance().getPixelSize(Globals.ADD_HEIGHT) : 0;

        headPanelArea = new RectF(0,0, screenWidth, editAreaHeight);
        soundsPanelArea = new RectF(0, editAreaHeight, screenWidth, screenHeight - bottomAddOffset);
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
