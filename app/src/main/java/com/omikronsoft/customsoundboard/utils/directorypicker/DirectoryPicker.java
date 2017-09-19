package com.omikronsoft.customsoundboard.utils.directorypicker;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.omikronsoft.customsoundboard.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DirectoryPicker extends ListActivity {

    private File dir, rootDir;
    private boolean onlyDirs = true ;

    public static final String ROOT_FLAG = "fromRoot";
    public static final String DIR_STARTING = "starting";
    public static final String DIRS = "onlyDirs";
    public static final String CHOSEN_DIRECTORY = "chosenDir";
    public static final int PICK_DIRECTORY = 43522432;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        rootDir = Environment.getExternalStorageDirectory();
        dir = Environment.getExternalStorageDirectory();
        if (extras != null) {
            String preferredStartDir = extras.getString(DIR_STARTING);
            onlyDirs = extras.getBoolean(DIRS, true);
            if(preferredStartDir != null) {
                File startDir = new File(preferredStartDir);
                if(startDir.isDirectory()) {
                    dir = startDir;
                }
            }
        }

        setContentView(R.layout.chooser_list);
        setTitle(dir.getAbsolutePath());
        Button btnChoose = (Button) findViewById(R.id.btnChoose);

        if(extras.getBoolean(ROOT_FLAG, true)){
            btnChoose.setEnabled(false);
            btnChoose.setText("Select Directory");
        }else{
            String name = dir.getName();
            if(name.length() == 0)
                name = "/";
            btnChoose.setText("Choose " + "'" + name + "'");
            btnChoose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    returnDir(dir.getAbsolutePath());
                }
            });
        }

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnDir("");
            }
        });

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        if(!dir.canRead()) {
            Context context = getApplicationContext();
            String msg = "Could not read folder contents.";
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        final ArrayList<File> files = filter(dir.listFiles(), onlyDirs, false);
        String[] names = names(files);
        setListAdapter(new ArrayAdapter<>(this, R.layout.list_item, names));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!files.get(position).isDirectory())
                    return;
                String path = files.get(position).getAbsolutePath();
                Intent intent = new Intent(DirectoryPicker.this, DirectoryPicker.class);
                intent.putExtra(DirectoryPicker.DIR_STARTING, path);
                intent.putExtra(DirectoryPicker.DIRS, onlyDirs);
                intent.putExtra(DirectoryPicker.ROOT_FLAG, false);
                startActivityForResult(intent, PICK_DIRECTORY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            returnDir(path);
        }
    }

    private void returnDir(String path) {
        Intent result = new Intent();
        String p1 = rootDir.getAbsolutePath();
        result.putExtra(CHOSEN_DIRECTORY, path.replace(p1, ""));
        setResult(RESULT_OK, result);
        finish();
    }

    public ArrayList<File> filter(File[] file_list, boolean onlyDirs, boolean showHidden) {
        ArrayList<File> files = new ArrayList<>();
        for(File file: file_list) {
            if(onlyDirs && !file.isDirectory())
                continue;
            if(!showHidden && file.isHidden())
                continue;
            files.add(file);
        }
        Collections.sort(files);
        return files;
    }

    public String[] names(ArrayList<File> files) {
        String[] names = new String[files.size()];
        int i = 0;
        for(File file: files) {
            names[i] = file.getName();
            i++;
        }
        return names;
    }
}