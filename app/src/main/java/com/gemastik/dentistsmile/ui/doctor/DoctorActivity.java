package com.gemastik.dentistsmile.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.article.DataArticle;
import com.gemastik.dentistsmile.data.model.article.ResponseArticle;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.article.ArticleAdapter;
import com.gemastik.dentistsmile.ui.article.detail.ArticleDetailActivity;
import com.gemastik.dentistsmile.ui.doctor.detail.DoctorDetailActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;

public class DoctorActivity extends AppCompatActivity implements DoctorInterface{
    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        adapter = new DoctorAdapter(this);
        RecyclerView rvInfo = findViewById(R.id.rvHelloData);
        rvInfo.setAdapter(adapter);
        rvInfo.setLayoutManager(new LinearLayoutManager(this));
        getArticle();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void getArticle() {
        try {
            RequestBody body;
            JSONStringer json = new JSONStringer();
            json.object();
            json.key("get_articles").value("filter_articles");
            json.key("article_type").value("hello_stunting");
            json.endObject();
            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
            endpoint.getArticle(body).enqueue(new retrofit2.Callback<ResponseArticle>() {
                @Override
                public void onResponse(Call<ResponseArticle> call, retrofit2.Response<ResponseArticle> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getArticles() != null) {
                        Log.e("TTSSTTS", "onResponse: " + response.body().getArticles().size());
                        adapter.insertDataList(response.body().getArticles());
                    }
                }

                @Override
                public void onFailure(Call<ResponseArticle> call, Throwable t) {
                    if (Objects.equals(t.getMessage(), "closed")) {
                        getArticle();
                    } else {
                        Log.e("ERRRORORO", "onFailure: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String capitalizeString(String str) {
        String words[] = str.split("\\s");
        String capitalizeStr = "";

        for (String word : words) {
            // Capitalize first letter
            String firstLetter = word.substring(0, 1);
            // Get remaining letter
            String remainingLetters = word.substring(1);
            capitalizeStr += firstLetter.toUpperCase() + remainingLetters + " ";
        }
        return capitalizeStr;
    }

    @Override
    public void onChildClick(DataArticle datainfo) {
        startActivity(new Intent(this, DoctorDetailActivity.class).putExtra("dataarticle", datainfo));
    }
}
