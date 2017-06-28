package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.omikronsoft.customsoundboard.panels.SoundButtonData;
import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.SoundData;
import com.omikronsoft.customsoundboard.utils.SoundDataStorageControl;
import com.omikronsoft.customsoundboard.utils.StorageLocation;

import java.util.ArrayList;
import java.util.List;

public class EditButtonActivity extends Activity {
    private List<String> itemsToDisplay;
    private CheckBox defaultCheck;
    private CheckBox userCheck;
    private ListView listView;
    private SoundButtonData sbd;
    private EditText editTextName;
    private String selectedListItem;
    private MediaPlayer media;
    private Dialog editSoundDialog;
    private TextView editSoundVolume, editSoundStartOff, editSoundEndOff;
    private SeekBar volumeBar, startBar, endBar;
    private int soundEditDuration;
    private boolean lockEndBar, lockStartBar;

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
        editTextName = (EditText) findViewById(R.id.sound_name_edit);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(10);
        editTextName.setFilters(filterArray);

        TextView t = (TextView) findViewById(R.id.button_label);
        listView = (ListView) findViewById(R.id.list);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int) (height / 2.2f);
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

        prepareEditButton();

        Globals.getInstance().setDataLoading(false);
    }

    private void prepareEditSoundDialog() {
        editSoundDialog = new Dialog(this);
        editSoundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editSoundDialog.setContentView(R.layout.edit_sound_layout);
        editSoundDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        editSoundVolume = (TextView) (editSoundDialog).findViewById(R.id.txt_volume);
        editSoundStartOff = (TextView) (editSoundDialog).findViewById(R.id.txt_start_offset);
        editSoundEndOff = (TextView) (editSoundDialog).findViewById(R.id.txt_end_offset);

        volumeBar = (SeekBar) (editSoundDialog).findViewById(R.id.seek_bar_volume);
        startBar = (SeekBar) (editSoundDialog).findViewById(R.id.seek_bar_start);
        endBar = (SeekBar) (editSoundDialog).findViewById(R.id.seek_bar_end);

        final Button btnPlay = (Button) (editSoundDialog).findViewById(R.id.btn_play);
        final Button btnOk = (Button) (editSoundDialog).findViewById(R.id.btnOk);
        final Button btnCancel = (Button) (editSoundDialog).findViewById(R.id.btnCancel);
        final Switch switchLooping = (Switch) (editSoundDialog).findViewById(R.id.switch_looping);

        //((TextView) (editSoundDialog).findViewById(R.id.txt_sound_name)).setText(sbd.getSoundData().getName());
        endBar.setRotation(180);
        volumeBar.setMax(100);

        int soundEditMinDurationPercent;
        if (media.equals(sbd.getSoundData().getMedia())) {
            soundEditDuration = sbd.getSoundData().getClipDuration();
            soundEditMinDurationPercent = 10;

            startBar.setMax(soundEditDuration);
            endBar.setMax(soundEditDuration);
            volumeBar.setProgress(sbd.getSoundData().getVolume());
            startBar.setProgress(sbd.getSoundData().getStartOffset());
            endBar.setProgress(sbd.getSoundData().getEndOffset());
            String volumeString = String.valueOf(volumeBar.getProgress()) + "%";
            editSoundVolume.setText(volumeString);
            editSoundStartOff.setText(String.valueOf(startBar.getProgress()));
            editSoundEndOff.setText(String.valueOf(endBar.getProgress()));
            switchLooping.setChecked(sbd.getSoundData().isLooping());
        } else {
            soundEditDuration = media.getDuration();
            soundEditMinDurationPercent = 10;

            startBar.setMax(soundEditDuration);
            endBar.setMax(soundEditDuration);
            volumeBar.setProgress(100);
            startBar.setProgress(0);
            endBar.setProgress(0);
            String volumeString = String.valueOf(volumeBar.getProgress()) + "%";
            editSoundVolume.setText(volumeString);
            editSoundStartOff.setText(String.valueOf(startBar.getProgress()));
            editSoundEndOff.setText(String.valueOf(endBar.getProgress()));
            switchLooping.setChecked(false);
        }

        lockEndBar = false;
        lockStartBar = false;

        final int minSoundDuration = soundEditDuration / soundEditMinDurationPercent;

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String volumeString = progress + "%";
                editSoundVolume.setText(volumeString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editSoundStartOff.setText(progress);

                if (!lockStartBar) {
                    if (progress + minSoundDuration + endBar.getProgress() > soundEditDuration) {
                        endBar.setProgress(soundEditDuration - progress - minSoundDuration);
                    }

                    if (progress > soundEditDuration - minSoundDuration) {
                        startBar.setProgress(soundEditDuration - minSoundDuration);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lockEndBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lockEndBar = false;
            }
        });

        endBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editSoundEndOff.setText(progress);

                if (!lockEndBar) {
                    if (progress + minSoundDuration + startBar.getProgress() > soundEditDuration) {
                        startBar.setProgress(soundEditDuration - progress - minSoundDuration);
                    }

                    if (progress > soundEditDuration - minSoundDuration) {
                        endBar.setProgress(soundEditDuration - minSoundDuration);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lockStartBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lockStartBar = false;
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media.isPlaying()) {
                    AudioPlayer.getInstance().stopMedia(media);
                } else {
                    float volume = volumeBar.getProgress() / 100f;
                    media.setVolume(volume, volume);
                    AudioPlayer.getInstance().playWithOffset(media, startBar.getProgress(), endBar.getProgress());
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundData sd = sbd.getSoundData();
                sd.setLooping(switchLooping.isChecked());
                sd.setVolume(volumeBar.getProgress());
                sd.setStartOffset(startBar.getProgress());
                sd.setEndOffset(endBar.getProgress());

                hideDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
    }

    private void hideDialog() {
        editSoundDialog.hide();
        AudioPlayer.getInstance().stopMedia(media);
    }

    private void prepareEditButton() {
        if (media != null || (sbd.getSoundData() != null && sbd.getSoundData().getMedia() != null)) {
            media = media == null ? sbd.getSoundData().getMedia() : media;
            (findViewById(R.id.btn_edit)).setEnabled(true);
        } else {
            (findViewById(R.id.btn_edit)).setEnabled(false);
        }

    }

    private void prepareListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioPlayer.getInstance().stopPlayingListItem();
                selectedListItem = (String) (listView.getItemAtPosition(position));
                editTextName.setText(SoundDataStorageControl.getInstance().truncFilePrefix(selectedListItem));

                StorageLocation storageLoc = SoundDataStorageControl.getInstance().getStorageLocation(selectedListItem);
                media = SoundDataStorageControl.getInstance().getMedia(storageLoc, selectedListItem);

                prepareEditButton();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedListItem = (String) (listView.getItemAtPosition(position));
                editTextName.setText(SoundDataStorageControl.getInstance().truncFilePrefix(selectedListItem));

                StorageLocation storageLoc = SoundDataStorageControl.getInstance().getStorageLocation(selectedListItem);
                media = SoundDataStorageControl.getInstance().getMedia(storageLoc, selectedListItem);

                prepareEditButton();

                if (media != null) {
                    try {
                        AudioPlayer.getInstance().playListItem(media, 0);
                    } catch (NumberFormatException e) {
                        // todo add log
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    private void prepareButtonListeners() {
        (findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        (findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validForm()) {
                    SoundData sd = sbd.getSoundData();
                    StorageLocation storLoc = SoundDataStorageControl.getInstance().getStorageLocation(selectedListItem);
                    MediaPlayer media = SoundDataStorageControl.getInstance().getMedia(storLoc, selectedListItem);

                    String name = editTextName.getText().toString().replaceAll(",", "");

                    sd.setName(name);
                    sd.setFileName(selectedListItem);
                    sd.setStorageLoc(storLoc);
                    sd.setMedia(media);

                    SoundsPanelControl.getInstance().updateButtonData(sbd);
                    finishActivity();
                }
            }
        });

        (findViewById(R.id.btn_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareEditSoundDialog();
                editSoundDialog.show();
            }
        });
    }

    private boolean validForm() {
        return !editTextName.getText().toString().isEmpty() && !selectedListItem.isEmpty();
    }

    private void prepareCheckBoxListeners() {
        defaultCheck = (CheckBox) findViewById(R.id.checkBox);
        defaultCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDisplayList();
            }
        });

        userCheck = (CheckBox) findViewById(R.id.checkBox2);
        userCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDisplayList();
            }
        });
    }

    private void updateDisplayList() {
        itemsToDisplay.clear();

        if (defaultCheck.isChecked()) {
            itemsToDisplay.addAll(SoundDataStorageControl.getInstance().getDefaultFolderFiles());
        }

        if (userCheck.isChecked()) {
            itemsToDisplay.addAll(SoundDataStorageControl.getInstance().getUserFolderFiles());
        }

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void finishActivity() {
        AudioPlayer.getInstance().stopMedia(media);
        Globals.getInstance().setEditedButton(null);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
