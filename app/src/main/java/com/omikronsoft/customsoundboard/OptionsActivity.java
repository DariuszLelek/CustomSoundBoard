package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;
import com.omikronsoft.customsoundboard.utils.directorypicker.DirectoryPicker;

import java.io.File;
import java.util.List;

public class OptionsActivity extends Activity {
    private ListView folderList;
    private boolean modified, itemRemoved;
    private Intent directoryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        modified = false;
        itemRemoved = false;

        setContentView(R.layout.activity_options);

        folderList = (ListView) findViewById(R.id.list);
        List<String> userFolders = SoundDataStorageControl.getInstance().getUserFolders();
        folderList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userFolders));
        folderList.setLongClickable(true);

        directoryPicker = new Intent(ApplicationContext.get(), DirectoryPicker.class);
        directoryPicker.putExtra(DirectoryPicker.DIRS, false);
        directoryPicker.putExtra(DirectoryPicker.ROOT_FLAG, true);

        prepareButtonListeners();
        prepareListListener();

        Globals.getInstance().setDataLoading(false);
    }

    private void prepareListListener() {
        folderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedListItem = (String) (folderList.getItemAtPosition(position));
                SoundDataStorageControl.getInstance().removeUserFolder(selectedListItem);
                ((BaseAdapter) folderList.getAdapter()).notifyDataSetChanged();
                modified = true;
                itemRemoved = true;

                return true;
            }
        });
    }

    private void prepareButtonListeners() {
        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modified) {
                    SoundDataStorageControl.getInstance().saveUserFolders();
                    SoundsPanelControl.getInstance().prepareSoundsBoard();
                    modified = false;
                }
                if (itemRemoved) {
                    SoundDataStorageControl.getInstance().loadUserFoldersSounds();
                    SoundsPanelControl.getInstance().readSoundData();
                    SoundsPanelControl.getInstance().prepareSoundsBoard();
                    itemRemoved = false;
                }
                finish();
            }
        });

        (findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modified) {
                    SoundDataStorageControl.getInstance().saveUserFolders();
                }
                finishAffinity();
            }
        });

        (findViewById(R.id.add_folder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(directoryPicker, DirectoryPicker.PICK_DIRECTORY);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String folderName = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);

            if (!folderName.isEmpty()) {
                File f = new File(Environment.getExternalStorageDirectory(), folderName);
                if (f.exists()) {
                    SoundDataStorageControl.getInstance().addUserFolder(folderName);
                    MediaScannerConnection.scanFile(ApplicationContext.get(), new String[]{f.toString()}, null, null);
                    ((BaseAdapter) folderList.getAdapter()).notifyDataSetChanged();
                    modified = true;
                }
            }
        }
    }

}
