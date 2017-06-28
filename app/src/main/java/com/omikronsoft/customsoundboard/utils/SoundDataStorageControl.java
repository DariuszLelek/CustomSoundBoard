package com.omikronsoft.customsoundboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.omikronsoft.customsoundboard.R;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.omikronsoft.customsoundboard.utils.StorageLocation.REC_FOLDER;
import static java.lang.Integer.parseInt;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class SoundDataStorageControl {
    private static SoundDataStorageControl instance;
    private SharedPreferences prefs;
    private List<String> defaultFolderFiles;
    private Map<String, String> userFiles;
    private List<String> userFolders;
    private List<String> audioFileExtensions;
    private Resources res;
    private String defSoundFilePrefix, recordedSoundFilePrefix, userFoldersPrefName;

    public static final String SOUND_SAVE_PREFIX = "Sound"; // + "col,row" -> "1,2"
    public static final String SAVE_FORMAT_SPLITTER = ",";
    private Context context;

    private SoundDataStorageControl() {
        prefs = Globals.getInstance().getPrefs();
        res = Globals.getInstance().getResources();
        context = ApplicationContext.get();

        defaultFolderFiles = new ArrayList<>();
        userFiles = new HashMap<>();
        userFolders = new ArrayList<>();
        audioFileExtensions = new ArrayList<>();
        defSoundFilePrefix = res.getString(R.string.default_sound_file_prefix);
        recordedSoundFilePrefix = res.getString(R.string.recorded_sound_file_prefix);
        userFoldersPrefName = res.getString(R.string.user_folder_pref_name);
        audioFileExtensions.addAll(Arrays.asList(res.getString(R.string.audio_file_types).split(SAVE_FORMAT_SPLITTER)));

        loadUserFolders();

        loadData();
    }

    public void loadData() {
        Globals.getInstance().setDataLoading(true);
        loadDefaultSounds();
        loadUserFoldersSounds();
        Globals.getInstance().setDataLoading(false);
    }

    public void saveSoundData(SoundData sd) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getPrefSoundKey(sd), getPrefSoundValue(sd));
        editor.apply();
    }

    public MediaPlayer getMedia(StorageLocation storageLoc, String fileName) {
        MediaPlayer media = null;
        switch (storageLoc) {
            case DEFAULT_FOLDER:
                int resID = res.getIdentifier(fileName, "raw", context.getPackageName());
                media = MediaPlayer.create(context, resID);
                break;
            case USER_FOLDER:
                if (userFiles.containsKey(fileName)) {
                    try {
                        Uri url = Uri.parse(userFiles.get(fileName));
                        media = MediaPlayer.create(context, url);
                    } catch (NullPointerException e) {
                        // todo add logger
                        e.printStackTrace();
                    }
                }
                break;
            case REC_FOLDER:
                // todo in paid version
                break;
            default:
                break;
        }
        return media;
    }

    public SoundData[][] readSavedSoundsData() {
        int columns = Globals.getInstance().getColumns();
        int rows = Globals.getInstance().getRows();

        SoundData soundData[][] = new SoundData[columns][rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                SoundData sd = readSavedSoundData(i, j);
                if (sd != null) {
                    soundData[i][j] = sd;
                }
            }
        }

        return soundData;
    }

    public List<String> getDefaultFolderFiles() {
        return defaultFolderFiles;
    }

    public Set<String> getUserFolderFiles() {
        return userFiles.keySet();
    }

    public void addUserFolder(String folderName) {
        if (!userFolders.contains(folderName)) {
            userFolders.add(folderName);
        }
    }

    public void removeUserFolder(String folderName) {
        if (userFolders.contains(folderName)) {
            userFolders.remove(folderName);
        }
    }

    public void saveUserFolders() {
        String saveData = android.text.TextUtils.join(",", userFolders);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(userFoldersPrefName, saveData);
        editor.apply();
    }

    public List<String> getUserFolders() {
        return userFolders;
    }

    public StorageLocation getStorageLocation(String fileName) {
        if (fileName.startsWith(defSoundFilePrefix)) {
            return StorageLocation.DEFAULT_FOLDER;
        } else if (fileName.startsWith(recordedSoundFilePrefix)) {
            return REC_FOLDER;
        } else {
            return StorageLocation.USER_FOLDER;
        }
    }

    public String truncFilePrefix(String fileName) {
        if (fileName.startsWith(defSoundFilePrefix)) {
            return fileName.replace(defSoundFilePrefix, "");
        } else if (fileName.startsWith(recordedSoundFilePrefix)) {
            return fileName.replace(recordedSoundFilePrefix, "");
        } else {
            return fileName;
        }
    }

    public void loadUserFoldersSounds() {
        userFiles.clear();
        for (String folderName : userFolders) {
            File folder = new File(Environment.getExternalStorageDirectory(), folderName);
            if (folder.exists()) {
                File[] files = folder.listFiles();
                if(files != null){
                    for (File file : files) {
                        if (file.isFile() && isAudioFile(file) && !userFiles.containsKey(file.getName())) {
                            userFiles.put(getFileName(file), file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    @NonNull
    private String getPrefSoundKey(SoundData sd) {
        StringBuilder result = new StringBuilder();
        result.append(SOUND_SAVE_PREFIX).append(sd.getColumn()).append(SAVE_FORMAT_SPLITTER).append(sd.getRow());
        return result.toString();
    }

    @NonNull
    private String getPrefSoundKey(int column, int row) {
        StringBuilder result = new StringBuilder();
        result.append(SOUND_SAVE_PREFIX).append(column).append(SAVE_FORMAT_SPLITTER).append(row);
        return result.toString();
    }

    @NonNull
    private String getPrefSoundValue(SoundData sd) {
        StringBuilder result = new StringBuilder();
        result.append(sd.getName()).append(SAVE_FORMAT_SPLITTER);
        result.append(sd.getStorageLoc().value).append(SAVE_FORMAT_SPLITTER);
        result.append(sd.getFileName()).append(SAVE_FORMAT_SPLITTER);
        result.append(sd.getOffset()).append(SAVE_FORMAT_SPLITTER);
        result.append(sd.isLooping() ? "Y" : "N");
        return result.toString();
    }

    private boolean dataIsValid(String[] parts) {
        boolean valid = true;
        for (String part : parts) {
            if (part.isEmpty()) {
                valid = false;
                break;
            }
        }

        try {
            Integer.parseInt(parts[1]);
            Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            // todo add log
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }

    private void loadUserFolders() {
        userFolders.clear();
        for (String folder : prefs.getString(userFoldersPrefName, "").split(SAVE_FORMAT_SPLITTER)) {
            if (!folder.isEmpty()) {
                addUserFolder(folder);
            }
        }
    }

    private void deleteSavedData(String prefSoundKey) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(prefSoundKey);
        editor.apply();
    }

    private SoundData readSavedSoundData(int column, int row) {
        SoundData result = null;
        String prefSoundKey = getPrefSoundKey(column, row);
        String savedData = prefs.getString(prefSoundKey, "");

        if (!savedData.isEmpty()) {
            String[] parts = savedData.split(SAVE_FORMAT_SPLITTER);
            if (dataIsValid(parts)) {
                String name = parts[0];
                StorageLocation storageLoc = StorageLocation.fromInteger(parseInt(parts[1]));
                String fileName = parts[2];
                int delay = parseInt(parts[3]);
                boolean looping = parts.length > 4 ? parts[4].equals("Y") : false;

                MediaPlayer media = getMedia(storageLoc, fileName);

                // check if sound is available in storage
                if (media != null) {
                    result = new SoundData(column, row, name, media, delay, fileName, looping);
                } else {
                    deleteSavedData(prefSoundKey);
                }
            } else {
                deleteSavedData(prefSoundKey);
            }
        }

        return result;
    }

    private String getFileName(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name;
    }

    private String getFileExtension(File file) {
        String extension = "";
        String path = file.getAbsolutePath();
        int idx = path.lastIndexOf(".") + 1;
        if (idx < path.length()) {
            extension = path.substring(idx);
        }
        return extension;
    }

    private boolean isAudioFile(File file) {
        return audioFileExtensions.contains(getFileExtension(file));
    }

    private void loadDefaultSounds() {
        defaultFolderFiles.clear();
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            String fileName = field.getName();
            if (fileName.startsWith(defSoundFilePrefix) && !defaultFolderFiles.contains(fileName)) {
                defaultFolderFiles.add(fileName);
            }
        }
    }

    public synchronized static SoundDataStorageControl getInstance() {
        if (instance == null) {
            instance = new SoundDataStorageControl();
        }
        return instance;
    }
}
