package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.Globals;

import static android.R.attr.offset;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class HeadPanelControl extends Panel implements IPanelControl {
    private static HeadPanelControl instance;

    private HeadPanelControl(){
        super();

        area = SoundBoardLayout.getInstance().getHeadPanelArea();
        int offset = backGroundOffset;
        super.backGroundArea = new RectF(area.left + offset, area.top + offset, area.right - offset, area.bottom - offset/2);
    }

    @Override
    public void drawPanel(Canvas canvas) {
        super.drawPanelBackGround(canvas);
    }

    @Override
    public RectF getPanelArea() {
        return area;
    }

    public synchronized static HeadPanelControl getInstance() {
        if (instance == null) {
            instance = new HeadPanelControl();
        }
        return instance;
    }
}
