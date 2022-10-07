package com.gemastik.dentistsmile.ui.review.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.maps.ResponseMapsById;
import com.gemastik.dentistsmile.data.model.review.ResponseReview;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetailActivity extends AppCompatActivity {
    private Integer idMaps;
    private String desc, place_name, address, photo_ref;
    private Float avg_rating;
    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();
    private ListReviewAdapter adapter;
    private SharedPreferences sharedPref;
    private FloatingActionButton fabTambah;

    public ProgressBar pbRating1;
    public ProgressBar pbRating2;
    public ProgressBar pbRating3;
    public ProgressBar pbRating4;
    public ProgressBar pbRating5;

    public TextView tvTotalReview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        ImageView btnBack = findViewById(R.id.btnBack);
//        ImageView ivArticle = findViewById(R.id.ivArticle);
        btnBack.setOnClickListener(v -> finish());
//        TextView tvTitle = findViewById(R.id.tvTitle);
//        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
//        TextView tvAvgRating = findViewById(R.id.tvAvgRating);
//        RatingBar rbRating = findViewById(R.id.rbRating);
        FloatingActionButton fabTambah = findViewById(R.id.fabAddReview);


        idMaps = getIntent().getIntExtra("place_id", 0);
        place_name = getIntent().getStringExtra("place_name");
        address = getIntent().getStringExtra("address");
        avg_rating = getIntent().getFloatExtra("avg_rating", 0);
        photo_ref = getIntent().getStringExtra("photo_ref");

//        tvTitle.setText(place_name);
//        tvSubtitle.setText(address);
//        tvAvgRating.setText(avg_rating.toString());
//        rbRating.setRating(avg_rating.floatValue());
//        String url_photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyAN6a7kSklwRHnNojU72nDnCfhYGhrATh0&photo_reference=";
//        Picasso.get().load(url_photo + photo_ref).into(ivArticle);

        adapter = new ListReviewAdapter();
        RecyclerView rvDataReview = findViewById(R.id.rvListReviewData);
        rvDataReview.setAdapter(adapter);
        rvDataReview.setLayoutManager(new LinearLayoutManager(this));

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(ReviewDetailActivity.this, AddReviewActivity.class);
                intentAdd.putExtra("place_id", idMaps);
                startActivity(intentAdd);
            }
        });

        getLocationInformation(idMaps);
        getListReview();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    private void getLocationInformation(Integer placeId){
        try {
            JSONStringer json = new JSONStringer();
            json.object();
            json.key("get_type").value("registered_filter_ids");
            json.key("place_ids");
            json.array();
            json.value(placeId);
            json.endArray();
            json.endObject();

//            Call<ResponseMaps> mapsCall = endpoint.getMaps( body);
            Call<ResponseMapsById> mapsCall = endpoint.getMapsById( json.toString());
            mapsCall.enqueue(new Callback<ResponseMapsById>() {

                @Override
                public void onResponse(Call<ResponseMapsById> call, Response<ResponseMapsById> response) {
                    if (response.isSuccessful() && response.body().getDataMapsById() != null) {
                        ResponseMapsById mapsById = response.body();
                        TextView tvTitle = findViewById(R.id.tvTitle);
                        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
                        TextView tvAvgRating = findViewById(R.id.tvAvgRating);
                        RatingBar rbRating = findViewById(R.id.rbRating);
                        ImageView ivArticle = findViewById(R.id.ivArticle);
                        tvTitle.setText(place_name);
                        tvSubtitle.setText(address);
//                        tvAvgRating.setText(avg_rating.toString());
                        tvAvgRating.setText(String.format("%.1f", response.body().getDataMapsById().get(0).getAvg_rating()));
                        rbRating.setRating(response.body().getDataMapsById().get(0).getAvg_rating().floatValue());
                        String url_photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyAN6a7kSklwRHnNojU72nDnCfhYGhrATh0&photo_reference=";
                        Picasso.get().load(url_photo + photo_ref).into(ivArticle);
                    } else {
                        Log.d("desc", "error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseMapsById> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getListReview(){
        try {
            RequestBody body;
            JSONStringer json = new JSONStringer();
            json.object();
            json.key("filter");
            json.object();
            json.key("stuntplace_id").value(idMaps);
            json.key("user_email").value(null);
            json.endObject();
            json.endObject();
//            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
            endpoint.getReview(json.toString()).enqueue(new retrofit2.Callback<ResponseReview>() {
                @Override
                public void onResponse(Call<ResponseReview> call, retrofit2.Response<ResponseReview> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getReviews() != null) {
                        ProgressBar pbRating1 = findViewById(R.id.pbRating1);
                        ProgressBar pbRating2 = findViewById(R.id.pbRating2);
                        ProgressBar pbRating3 = findViewById(R.id.pbRating3);
                        ProgressBar pbRating4 = findViewById(R.id.pbRating4);
                        ProgressBar pbRating5 = findViewById(R.id.pbRating5);
                        TextView tvTotalReview = findViewById(R.id.tvTotalReview);
                        pbRating1.setProgress(response.body().getDataPerRating().getSatu());
                        pbRating1.setMax(response.body().getReviewCount());
                        pbRating2.setProgress(response.body().getDataPerRating().getDua());
                        pbRating2.setMax(response.body().getReviewCount());
                        pbRating3.setProgress(response.body().getDataPerRating().getTiga());
                        pbRating3.setMax(response.body().getReviewCount());
                        pbRating4.setProgress(response.body().getDataPerRating().getEmpat());
                        pbRating4.setMax(response.body().getReviewCount());
                        pbRating5.setProgress(response.body().getDataPerRating().getLima());
                        pbRating5.setMax(response.body().getReviewCount());
                        tvTotalReview.setText("("+response.body().getReviewCount().toString()+")");
                        adapter.insertDataList(response.body().getReviews());
                    }
                }

                @Override
                public void onFailure(Call<ResponseReview> call, Throwable t) {
                    if (Objects.equals(t.getMessage(), "closed")) {
                        getListReview();
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
