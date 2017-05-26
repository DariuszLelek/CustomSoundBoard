package com.omikronsoft.customsoundboard.panels;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public interface IPanelControl {
    void drawPanel(Canvas canvas);
    RectF getPanelArea();
}
