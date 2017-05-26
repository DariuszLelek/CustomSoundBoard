package com.omikronsoft.customsoundboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private AnimationControl animationControl;
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

        MobileAds.initialize(this, getResources().getString(R.string.dev_id));
        SharedPreferences prefs = this.getSharedPreferences("FidgetSpinnerDefensePrefs", Context.MODE_PRIVATE);

        globals.setPrefs(prefs);
        globals.setScreenSizes(size.x, size.y);
        globals.setPixelDensity(getResources().getDisplayMetrics().density);
        globals.setResources(getResources());

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getResources().getString(R.string.banner_id));
        adView.setBackgroundColor(Color.TRANSPARENT);
        adView.setLayoutParams(adParams);

        RelativeLayout layout = new RelativeLayout(this);
        animationControl = new AnimationControl(this);
        layout.addView(animationControl);
        layout.addView(adView);

        setContentView(layout);

        // Test Ads
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        animationControl.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationControl.pause();
    }
}
