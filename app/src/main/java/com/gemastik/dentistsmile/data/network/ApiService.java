package com.gemastik.dentistsmile.data.network;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.gemastik.dentistsmile.data.network.interceptor.AuthInterceptorStn;
import com.gemastik.dentistsmile.root.App;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiService {

    private static ApiEndpoint retrofit;
//    public final static String BASE_URL = "http://192.168.1.6:8000/";
//    public final static String BASE_URL = "https://mocki.io/v1/";
    public final static String BASE_URL = "http://dentistsmile.hellobetterfuture.com:8000/";

    public static ApiEndpoint getRetrofitInstance() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new AuthInterceptorStn())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new ChuckerInterceptor.Builder(App.instance).build())
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build()
                    .create(ApiEndpoint.class);
        }

        return retrofit;
    }
}
