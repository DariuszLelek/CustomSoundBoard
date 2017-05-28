package com.omikronsoft.customsoundboard.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.SoundData;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundDataStorageControl {
    private static SoundDataStorageControl instance;
    private SharedPreferences prefs;
    private Map<String, Integer> soundFiles;
    private File defaultFileDirectory;
    private Resources res;

    private final String SETUP = "sound_data_storage";
    private final String SOUND_SAVE_FORMAT = "Sound"; // + "col,row" -> "1,2"
    private final String SOUND_SAVE_FORMAT_SPLITTER = ",";
    private Context context;

    private SoundDataStorageControl(){
        prefs = Globals.getInstance().getPrefs();
        res = Globals.getInstance().getResources();
        context = ApplicationContext.get();

        soundFiles = new HashMap<>();
        defaultFileDirectory = new File(Globals.getInstance().getResources().getString(R.string.default_sound_directory));
        loadSoundFilesLoc();
    }

    public void saveSoundData(SoundData sd){
        SharedPreferences.Editor editor = prefs.edit();
        String prefName = SOUND_SAVE_FORMAT + sd.getColumn() + SOUND_SAVE_FORMAT_SPLITTER + sd.getRow();
        editor.putString(prefName, getSoundDataString(sd));
        editor.apply();
    }

    private String getSoundDataString(SoundData sd){
        StringBuilder result = new StringBuilder();
        result.append(sd.getName()).append(SOUND_SAVE_FORMAT_SPLITTER);
        result.append(sd.getPath()).append(SOUND_SAVE_FORMAT_SPLITTER);
        result.append(sd.getDelay());
        return result.toString();
    }

    public SoundData[][] readSavedSoundsData(){
        int columns =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_columns);
        int rows =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_rows);

        SoundData soundData[][] = new SoundData[columns][rows];
        String prefName;

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                prefName = SOUND_SAVE_FORMAT + i + SOUND_SAVE_FORMAT_SPLITTER + j;
                String savedData = prefs.getString(prefName, "");
                String[] parts = savedData.split(SOUND_SAVE_FORMAT_SPLITTER);
                if(!savedData.isEmpty()){
                    SoundData sd = new SoundData(i, j, parts[0], Uri.parse(parts[1]), Integer.parseInt(parts[2]));
                    soundData[i][j] = sd;
                }
            }
        }

        return soundData;
    }

    private void loadSoundFilesLoc(){
        java.lang.reflect.Field[] fields = R.raw.class.getDeclaredFields();
        for(int count=0; count < fields.length; count++){
            ContentResolver cR = context.getContentResolver();
            int rid = 0;
            try {
                rid = fields[count].getInt(fields[count]);
                String filename = fields[count].getName();
                Uri url = Uri.parse("android.resource://com.omikronsoft.customsoundboard/raw/" + filename);


                if(!soundFiles.containsKey(filename)){
                    soundFiles.put(filename, rid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(soundFiles != null){

        }
    }

    public synchronized static SoundDataStorageControl getInstance() {
        if (instance == null) {
            instance = new SoundDataStorageControl();
        }
        return instance;
    }
}
