package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;

import java.io.File;
import java.util.List;

import static java.lang.Integer.parseInt;


public class OptionsActivity extends Activity {
    private Button removeFolder;
    private List<String> userFolders;
    private ListView folderList;
    private EditText editText;
    private boolean modified, itemRemoved;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        modified = false;
        itemRemoved = false;

        setContentView(R.layout.activity_options);

        listView = (ListView)findViewById(R.id.list);
        editText = (EditText)findViewById(R.id.editText);
        userFolders = SoundDataStorageControl.getInstance().getUserFolders();

        folderList = (ListView)findViewById(R.id.list);
        folderList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userFolders));


        prepareButtonListeners();
        prepareListListener();

        Globals.getInstance().setDataLoading(false);
    }

    private void prepareListListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedListItem =(String) (listView.getItemAtPosition(position));
                SoundDataStorageControl.getInstance().removeUserFolder(selectedListItem);
                ((BaseAdapter) folderList.getAdapter()).notifyDataSetChanged();
                modified = true;
                itemRemoved = true;

                return true;
            }
        });
    }

    private void  prepareButtonListeners(){
        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modified){
                    SoundDataStorageControl.getInstance().saveUserFolders();
                    SoundsPanelControl.getInstance().prepareSoundsBoard();
                    modified = false;
                }
                if(itemRemoved){
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
                if(modified){
                    SoundDataStorageControl.getInstance().saveUserFolders();
                }
                finishAffinity();
            }
        });

        (findViewById(R.id.add_folder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = editText.getText().toString();
                if(!folderName.isEmpty()){
                    File f = new File(Environment.getExternalStorageDirectory(), folderName);
                    if (f.exists()) {
                        SoundDataStorageControl.getInstance().addUserFolder(folderName);
                        MediaScannerConnection.scanFile(ApplicationContext.get(), new String[] {f.toString()}, null, null);
                        ((BaseAdapter) folderList.getAdapter()).notifyDataSetChanged();
                        modified = true;
                    }
                    editText.setText("");
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
