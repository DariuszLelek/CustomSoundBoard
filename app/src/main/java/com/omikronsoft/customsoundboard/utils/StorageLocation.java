package com.omikronsoft.customsoundboard.utils;

/**
 * Created by Dariusz Lelek on 5/28/2017.
 * dariusz.lelek@gmail.com
 */

public enum StorageLocation {
    DEFAULT_FOLDER(0),
    USER_FOLDER(1),
    REC_FOLDER(2);

    public int value;

    StorageLocation(int value) {
        this.value = value;
    }

    public static StorageLocation fromInteger(int value) {
        StorageLocation result = DEFAULT_FOLDER;
        switch (value) {
            case 0:
                result = DEFAULT_FOLDER;
                break;
            case 1:
                result = USER_FOLDER;
                break;
            case 2:
                result = REC_FOLDER;
                break;
            default:
                break;
        }
        return result;
    }
}
