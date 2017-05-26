package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.painting.Transparency;
import com.omikronsoft.customsoundboard.utils.Globals;

import static android.R.attr.offset;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class Panel {
    RectF area;
    int backGroundOffset;
    RectF backGroundArea;

    public Panel() {
        backGroundOffset = Globals.getInstance().getPixelSize(Globals.getInstance().getResources().getInteger(R.integer.background_edge_offset));
    }

    public void drawPanelBackGround(Canvas canvas) {
        canvas.drawRect(backGroundArea, PaintingResources.getInstance().getFillPaint(Color.WHITE, Transparency.V_LOW));
    }
}
