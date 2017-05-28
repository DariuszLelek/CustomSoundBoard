package com.omikronsoft.customsoundboard.utils;

import android.net.Uri;

/**
 * Created by Dariusz Lelek on 5/28/2017.
 * dariusz.lelek@gmail.com
 */

public class UpdateData {
    private int column, row;
    private String name;
    private int delay;
    private Uri path;

    public UpdateData(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }
}
