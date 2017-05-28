package com.omikronsoft.customsoundboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.SoundData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundDataStorageControl {
    private static SoundDataStorageControl instance;
    private SharedPreferences prefs;

    private final String SETUP = "sound_data_storage";
    private final String SOUND_SAVE_FORMAT = "Sound"; // + "col,row" -> "1,2"
    private final String SOUND_SAVE_FORMAT_SPLITTER = ",";
    private Context context;

    private SoundDataStorageControl(){
        prefs = Globals.getInstance().getPrefs();
        context = ApplicationContext.get();
    }

    private InputStream getFileStream(String name){
        InputStream is = null;
        try {
            is = context.openFileInput(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }

    public SoundData[][] readSavedSoundsData(){
        int columns =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_columns);
        int rows =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_rows);

//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("soundEnabled", soundEnabled);
//        editor.apply();
        //prefs.getFloat("bestScore", 0.0f);

        SoundData soundData[][] = new SoundData[columns][rows];
        String prefName;

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                prefName = SOUND_SAVE_FORMAT + i + SOUND_SAVE_FORMAT_SPLITTER + j;
                String savedData = prefs.getString(prefName, "");
                if(!savedData.isEmpty()){

                }
            }
        }

        return soundData;
    }

    public synchronized static SoundDataStorageControl getInstance() {
        if (instance == null) {
            instance = new SoundDataStorageControl();
        }
        return instance;
    }
}
