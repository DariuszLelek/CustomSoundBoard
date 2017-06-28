package com.omikronsoft.customsoundboard.painting;

/**
 * Created by Dariusz Lelek on 5/22/2017.
 * dariusz.lelek@gmail.com
 */

public enum Transparency {
    OPAQUE(255),
    HALF(120),
    LOW(40),
    V_LOW(10);

    int value;

    Transparency(int value) {
        this.value = value;
    }
}
