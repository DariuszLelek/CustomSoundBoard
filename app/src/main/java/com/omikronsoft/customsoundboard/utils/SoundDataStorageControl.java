package com.omikronsoft.customsoundboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;

import com.omikronsoft.customsoundboard.R;
import com.omikronsoft.customsoundboard.SoundData;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundDataStorageControl {
    private static SoundDataStorageControl instance;
    private SharedPreferences prefs;
    private Map<String, Integer> soundFiles;
    private List<String> defaultFolderFiles;
    private File defaultSoundsDir;
    private Resources res;
    private String defSoundFilePrefix, recordedSoundFilePrefix;

    private final String SETUP = "sound_data_storage";
    private final String SOUND_SAVE_PREFIX = "Sound"; // + "col,row" -> "1,2"
    private final String SOUND_SAVE_FORMAT_SPLITTER = ",";
    private Context context;

    private SoundDataStorageControl(){
        prefs = Globals.getInstance().getPrefs();
        res = Globals.getInstance().getResources();
        context = ApplicationContext.get();

        defaultFolderFiles = new ArrayList<>();
        defSoundFilePrefix = res.getString(R.string.default_sound_file_prefix);
        recordedSoundFilePrefix = res.getString(R.string.recorded_sound_file_prefix);

        soundFiles = new HashMap<>();
        defaultSoundsDir = new File("/default_sounds/");
        loadSoundFilesLoc();
    }

    public void saveSoundData(SoundData sd){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getPrefSoundKey(sd), getPrefSoundValue(sd));
        editor.apply();
    }

    private String getPrefSoundKey(SoundData sd){
        StringBuilder result = new StringBuilder();
        result.append(SOUND_SAVE_PREFIX).append(sd.getColumn()).append(SOUND_SAVE_FORMAT_SPLITTER).append(sd.getRow());
        return result.toString();
    }

    private String getPrefSoundKey(int column, int row){
        StringBuilder result = new StringBuilder();
        result.append(SOUND_SAVE_PREFIX).append(column).append(SOUND_SAVE_FORMAT_SPLITTER).append(row);
        return result.toString();
    }

    private String getPrefSoundValue(SoundData sd){
        StringBuilder result = new StringBuilder();
        result.append(sd.getName()).append(SOUND_SAVE_FORMAT_SPLITTER);
        result.append(sd.getStorageLoc().value).append(SOUND_SAVE_FORMAT_SPLITTER);
        result.append(sd.getFileName()).append(SOUND_SAVE_FORMAT_SPLITTER);
        result.append(sd.getOffset());
        return result.toString();
    }

    private SoundData readSavedSoundData(int column, int row){
        SoundData result = null;
        String prefSoundKey = getPrefSoundKey(column, row);
        String savedData = prefs.getString(prefSoundKey, "");

        if(!savedData.isEmpty()){
            String[] parts = savedData.split(SOUND_SAVE_FORMAT_SPLITTER);
            if(dataIsValid(parts)){
                String name = parts[0];
                StorageLocation storageLoc = StorageLocation.fromInteger(parseInt(parts[1]));
                String fileName = parts[2];
                int delay = parseInt(parts[3]);

                MediaPlayer media = getMedia(storageLoc, fileName);
                result = new SoundData(column, row, name, media, delay, fileName);
            }else{
                deleteSavedData(prefSoundKey);
            }
        }

        return result;
    }

    public MediaPlayer getMedia(StorageLocation storageLoc, String fileName){
        MediaPlayer media = null;
        switch (storageLoc){
            case DEFAULT_FOLDER:
                int resID = res.getIdentifier(fileName, "raw", context.getPackageName());
                media = MediaPlayer.create(context, resID);
                break;
            case USER_FOLDER:
                break;
            case REC_FORDER:
                break;
            default:
                break;
        }
        return media;
    }

    public SoundData[][] readSavedSoundsData(){
        int columns =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_columns);
        int rows =  Globals.getInstance().getResources().getInteger(R.integer.sound_button_rows);

        SoundData soundData[][] = new SoundData[columns][rows];

        for(int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                SoundData sd = readSavedSoundData(i, j);
                if(sd != null){
                    soundData[i][j] = sd;
                }
            }
        }

        return soundData;
    }

    public List<String> getDefaultFolderFiles(){
        return defaultFolderFiles;
    }

    private boolean dataIsValid(String[] parts){
        boolean valid = true;
        for(int i=0; i<parts.length; i++){
            if(parts[i].isEmpty()){
                valid = false;
                break;
            }
        }

        try{
            Integer.parseInt(parts[1]);
            Integer.parseInt(parts[3]);
        }catch(NumberFormatException e){
            // add log
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }

    public StorageLocation getSorageLocation(String fileName){
        if(fileName.startsWith(defSoundFilePrefix)){
            return StorageLocation.DEFAULT_FOLDER;
        }else if(fileName.startsWith(recordedSoundFilePrefix)){
            return StorageLocation.REC_FORDER;
        }else{
            return StorageLocation.USER_FOLDER;
        }
    }

    public String truncFilePrefix(String fileName){
        if(fileName.startsWith(defSoundFilePrefix)){
            return fileName.replace(defSoundFilePrefix, "");
        }else if(fileName.startsWith(recordedSoundFilePrefix)){
            return fileName.replace(recordedSoundFilePrefix, "");
        }else{
            return fileName;
        }
    }

    private void deleteSavedData(String prefSoundKey){
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(prefSoundKey);
        editor.apply();
    }

    private void loadSoundFilesLoc(){
        Globals.getInstance().setDataLoading(true);

        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            String fileName = fields[count].getName();
            if(fileName.startsWith(defSoundFilePrefix)){
                defaultFolderFiles.add(fileName);
            }
        }

        Globals.getInstance().setDataLoading(false);
    }

    public synchronized static SoundDataStorageControl getInstance() {
        if (instance == null) {
            instance = new SoundDataStorageControl();
        }
        return instance;
    }
}
