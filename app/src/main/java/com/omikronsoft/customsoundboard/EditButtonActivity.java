package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.omikronsoft.customsoundboard.panels.SoundButtonData;
import com.omikronsoft.customsoundboard.panels.SoundsPanelControl;
import com.omikronsoft.customsoundboard.utils.Globals;
import com.omikronsoft.customsoundboard.utils.UpdateData;

import static android.R.attr.name;

public class EditButtonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_edit_button);

        final SoundButtonData sbd = Globals.getInstance().getEditedButton();
        final EditText editTextName = (EditText)findViewById(R.id.sound_name_edit);
        final EditText editDelay = (EditText)findViewById(R.id.delay_edit);
        TextView t = (TextView)findViewById(R.id.button_label);
        ListView listView = (ListView)findViewById(R.id.listView);
        String[] pre = {"aaaa", "ssss", "Asasas", "assasa", "Asasa", "asassss", "adsdasd", "asdsad"};
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pre);

        t.setText("Edit Button " + (sbd.getColumn() + 1) + ", " + (sbd.getRow() + 1));
        editTextName.setText(sbd.getSoundData().getName());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        (findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        (findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbd.getSoundData().setName(editTextName.getText().toString());
                sbd.getSoundData().setDelay(Integer.parseInt(editDelay.getText().toString()));
                // path
                SoundsPanelControl.getInstance().updateButtonData(sbd);
                finishActivity();
            }
        });

        Globals.getInstance().setDataLoading(false);
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
