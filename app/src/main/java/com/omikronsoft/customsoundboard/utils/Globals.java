package com.omikronsoft.customsoundboard.utils;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.omikronsoft.customsoundboard.layouts.SoundBoardLayout;
import com.omikronsoft.customsoundboard.painting.PaintingResources;
import com.omikronsoft.customsoundboard.panels.HeadPanelControl;
import com.omikronsoft.customsoundboard.panels.SoundButtonData;
import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class Globals {
    private static Globals instance;
    private SharedPreferences prefs;
    private int rows, columns, screenWidth, screenHeight;
    private float pixelDensity;
    private Resources resources;
    private boolean dataPrepared, editMode, dataLoading;
    private SoundButtonData editedButton;

    public final static int ADD_HEIGHT = 50;
    public final static boolean ADS_ENABLED = false;
    private final static int MAX_FPS = 30;

    private Globals() {
        dataPrepared = false;
        editMode = false;
        dataLoading = false;
    }

    public boolean isDataLoading() {
        return dataLoading;
    }

    public void setDataLoading(boolean dataLoading) {
        this.dataLoading = dataLoading;
    }

    SharedPreferences getPrefs() {
        return prefs;
    }

    public void setScreenSizes(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public SoundButtonData getEditedButton() {
        return editedButton;
    }

    public void setEditedButton(SoundButtonData editedButton) {
        this.editedButton = editedButton;
    }

    public void prepareData() {
        // init singletons
        PaintingResources.getInstance();

        SoundBoardLayout.getInstance();
        HeadPanelControl.getInstance();
        SoundsPanelControl.getInstance();
        AudioPlayer.getInstance();

        dataPrepared = true;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void switchEditMode() {
        this.editMode = !editMode;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public boolean isDataPrepared() {
        return dataPrepared;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public static int getMaxFps() {
        return MAX_FPS;
    }

    public int getPixelSize(int value) {
        return (int) (value * pixelDensity);
    }

    public void setPixelDensity(float density) {
        this.pixelDensity = density;
    }

    public float getPixelDensity() {
        return pixelDensity;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public synchronized static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
