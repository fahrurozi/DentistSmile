package com.gemastik.dentistsmile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.get_started.GetStartedActivity;

public class SplashScreenActivity extends AppCompatActivity {
    Animation app_splash, btt;
    ImageView app_logo;
    TextView app_subtitle;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Session
        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        // load element
        app_logo = findViewById(R.id.app_logo_splash);

        // run animation
        app_logo.startAnimation(app_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // merubah activity ke activity lain
            String ID = sharedPref.getString(getString(R.string.id), "");
            if (ID.equals("")) {
                Intent gogetstarted = new Intent(SplashScreenActivity.this, GetStartedActivity.class);
                startActivity(gogetstarted);
                finish();
            } else {
                Intent gomain = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(gomain);
                finish();
            }
        }, 2000); // 2000 ms = 2s
    }
}
