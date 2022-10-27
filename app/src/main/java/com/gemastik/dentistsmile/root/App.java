package com.gemastik.dentistsmile.root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;

public class App extends Application implements CameraXConfig.Provider {
    public static App instance;
    public static ApiEndpoint apiServiceStn;
    public static ApiEndpoint apiServiceDentist;
    public static SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        apiServiceStn = ApiService.getRetrofitInstance();
        apiServiceDentist = ApiServiceDentist.getRetrofitInstance();
        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
                .setMinimumLoggingLevel(Log.ERROR).build();
    }
}
