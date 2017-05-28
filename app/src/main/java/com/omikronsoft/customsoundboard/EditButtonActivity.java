package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.omikronsoft.customsoundboard.panels.SoundButtonData;
import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;
import com.omikronsoft.customsoundboard.utils.StorageLocation;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class EditButtonActivity extends Activity {
    private List<String> itemsToDisplay;
    private CheckBox defaultCheck, userCheck, recordedCheck;
    private ListView listView;
    private SoundButtonData sbd;
    private EditText editTextName, editDelay;
    private String selectedListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        setContentView(R.layout.activity_edit_button);

        sbd = Globals.getInstance().getEditedButton();
        editTextName = (EditText)findViewById(R.id.sound_name_edit);
        editDelay = (EditText)findViewById(R.id.delay_edit);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(10);
        editTextName.setFilters(filterArray);

        InputFilter[] filterArray2 = new InputFilter[1];
        filterArray2[0] = new InputFilter.LengthFilter(4);
        editDelay.setFilters(filterArray2);

        TextView t = (TextView)findViewById(R.id.button_label);
        listView = (ListView)findViewById(R.id.list);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int)(height/2.2f);
        listView.setLayoutParams(new LinearLayout.LayoutParams(params));

        itemsToDisplay = new ArrayList<>();
        selectedListItem = sbd.getSoundData().getFileName();

        t.setText("Edit Button " + (sbd.getColumn() + 1) + ", " + (sbd.getRow() + 1));
        editTextName.setText(sbd.getSoundData().getName());
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsToDisplay));
        listView.setLongClickable(true);

        prepareCheckBoxListeners();
        prepareButtonListeners();
        prepareListViewListener();

        Globals.getInstance().setDataLoading(false);
    }

    private void prepareListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioPlayer.getInstance().stopPlayingListItem();
                selectedListItem =(String) (listView.getItemAtPosition(position));
                editTextName.setText(SoundDataStorageControl.getInstance().truncFilePrefix(selectedListItem));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedListItem =(String) (listView.getItemAtPosition(position));
                editTextName.setText(SoundDataStorageControl.getInstance().truncFilePrefix(selectedListItem));

                StorageLocation storLoc = SoundDataStorageControl.getInstance().getSorageLocation(selectedListItem);
                MediaPlayer media = SoundDataStorageControl.getInstance().getMedia(storLoc, selectedListItem);
                if(media != null && validateOffset()){
                    try{
                        int offset = parseInt(editDelay.getText().toString());
                        AudioPlayer.getInstance().playListItem(media, offset);
                    }catch(NumberFormatException e){
                        // add log
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    private void prepareButtonListeners(){
        (findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        (findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validForm()){
                    SoundData sd = sbd.getSoundData();
                    StorageLocation storLoc = SoundDataStorageControl.getInstance().getSorageLocation(selectedListItem);
                    MediaPlayer media = SoundDataStorageControl.getInstance().getMedia(storLoc, selectedListItem);

                    sd.setName(editTextName.getText().toString());
                    sd.setOffset(parseInt(editDelay.getText().toString()));
                    sd.setFileName(selectedListItem);
                    sd.setStorageLoc(storLoc);
                    sd.setMedia(media);
                    SoundsPanelControl.getInstance().updateButtonData(sbd);
                    finishActivity();
                }
            }
        });
    }

    private boolean validForm(){
        return validateOffset() && !editTextName.getText().toString().isEmpty() && !selectedListItem.isEmpty();
    }

    private boolean validateOffset(){
        boolean valid = true;
        try{
            String offset = editDelay.getText().toString();
            if(offset.isEmpty()){
                editDelay.setText("0");
            }
            parseInt(editDelay.getText().toString());
        }catch(NumberFormatException e){
            // add log
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    private void prepareCheckBoxListeners(){
        defaultCheck = (CheckBox)findViewById(R.id.checkBox);
        defaultCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDisplayList();
            }
        });

        userCheck = (CheckBox)findViewById(R.id.checkBox2);
        defaultCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDisplayList();
            }
        });

        recordedCheck = (CheckBox)findViewById(R.id.checkBox3);
        recordedCheck.setEnabled(false);
    }

    private void updateDisplayList(){
        itemsToDisplay.clear();

        if(defaultCheck.isChecked()){
            itemsToDisplay.addAll(SoundDataStorageControl.getInstance().getDefaultFolderFiles());
        }

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void finishActivity(){
        Globals.getInstance().setEditedButton(null);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
