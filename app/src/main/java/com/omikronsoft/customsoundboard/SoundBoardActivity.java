package com.omikronsoft.customsoundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.omikronsoft.customsoundboard.utils.ApplicationContext;
import com.omikronsoft.customsoundboard.utils.AudioPlayer;
import com.omikronsoft.customsoundboard.utils.Globals;

public class SoundBoardActivity extends AppCompatActivity {
    private SoundBoardActivityControl soundBoardActivityControl;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ApplicationContext.getInstance().init(getApplicationContext());
        Globals globals = Globals.getInstance();

        SharedPreferences prefs = this.getSharedPreferences("CustomSoundBoard", Context.MODE_PRIVATE);

        globals.setColumns(prefs.getInt("Columns", getResources().getInteger(R.integer.sound_button_columns)));
        globals.setRows(prefs.getInt("Rows", getResources().getInteger(R.integer.sound_button_rows)));
        globals.setPrefs(prefs);
        globals.setScreenSizes(size.x, size.y);
        globals.setPixelDensity(getResources().getDisplayMetrics().density);
        globals.setResources(getResources());
        globals.loadDefaultSetup();

        RelativeLayout layout = new RelativeLayout(this);
        soundBoardActivityControl = new SoundBoardActivityControl(this);
        layout.addView(soundBoardActivityControl);

        if(Globals.ADS_ENABLED){
            MobileAds.initialize(this, getResources().getString(R.string.app_id));

            RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(getResources().getString(R.string.add_unit_id));
            adView.setBackgroundColor(Color.TRANSPARENT);
            adView.setLayoutParams(adParams);
            layout.addView(adView);

            // Test Ads
            //AdRequest adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.loadAd(adRequest);
        }

        setContentView(layout);
    }

    @Override
    protected void onDestroy() {
        AudioPlayer.getInstance().stopAll();

        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        soundBoardActivityControl.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundBoardActivityControl.pause();
    }
}
