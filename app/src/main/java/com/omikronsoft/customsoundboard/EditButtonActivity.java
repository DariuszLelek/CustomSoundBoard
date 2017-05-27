package com.omikronsoft.customsoundboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EditButtonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        setContentView(R.layout.activity_edit_button);

        String buttonLabel = "Edit Button " + i.getStringExtra("ButtonCoords");
        TextView t = (TextView)findViewById(R.id.button_label);
        t.setText(buttonLabel);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String[] pre = {"aaaa", "ssss", "Asasas", "assasa", "Asasa", "asassss"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pre);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
