package com.gemastik.dentistsmile.ui.doctor.detail;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.article.DataArticle;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.squareup.picasso.Picasso;

public class DoctorDetailActivity extends AppCompatActivity {
    DataArticle dataarticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView ivArticle = findViewById(R.id.ivArticle);
        btnBack.setOnClickListener(v -> finish());
        TextView tvTitle = findViewById(R.id.tvTitle);
        dataarticle = (DataArticle) getIntent().getParcelableExtra("dataarticle");

        Log.e("TAG", "onCreate: " + dataarticle.toString());

        tvTitle.setText(dataarticle.getTitle());
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.clearCache(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(ApiService.BASE_URL + "static/" + dataarticle.getArticleFile());
        Picasso.get().load(ApiService.BASE_URL + "static/" + dataarticle.getArticleCoverFile()).into(ivArticle);

    }
}
