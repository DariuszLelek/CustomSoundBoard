package com.omikronsoft.customsoundboard.utils;

import android.content.Context;
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

    private final String SETUP = "sound_data_storage";
    private Context context;

    private SoundDataStorageControl(){
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



        SoundData soundData[][] = new SoundData[columns][rows];
        try {
            InputStream is = getFileStream(SETUP);
            if(is == null){
                is = Globals.getInstance().getResources().openRawResource(R.raw.default_sound_data);
            }
            if(is != null){
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String parts[] = receiveString.split(",");
                    int column = Integer.parseInt(parts[0]);
                    int row = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    Uri path = Uri.parse(parts[3]);

                    SoundData sd = new SoundData(column, row, name, path);
                    if(column < columns && row < rows){
                        soundData[column][row] = sd;
                    }
                }

                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
