package com.omikronsoft.customsoundboard;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PointF;

/**
 * Created by Dariusz Lelek on 5/26/2017.
 * dariusz.lelek@gmail.com
 */

public class Globals {
    private static Globals instance;
    private SharedPreferences prefs;
    private int screenWidth, screenHeight;
    private float screenWidth2, screenHeight2, pixelDensity;
    private PointF screenCenter;
    private Resources resources;

    private final static int ADD_HEIGHT = 50;
    private final static int MAX_FPS = 30;

    private Globals(){
        
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setScreenSizes(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        screenWidth2 = screenWidth / 2;
        screenHeight2 = screenHeight / 2;

        screenCenter = new PointF(screenWidth2, screenHeight2);
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

    public synchronized static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
