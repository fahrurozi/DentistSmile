package com.gemastik.dentistsmile.ui.review.detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.review.ResponseAddReview;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONStringer;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReviewActivity extends AppCompatActivity {

    private RatingBar rbInputRating;
    private FloatingActionButton fabSimpan;
    private EditText etInputDescription;

    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();

    private Integer place_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        rbInputRating = findViewById(R.id.rbInputRating);
        fabSimpan = findViewById(R.id.fabSimpan);
        etInputDescription = findViewById(R.id.etInputDescription);

        place_id = getIntent().getIntExtra("place_id", 0);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        fabSimpan.setOnClickListener(v -> {
            float rating = rbInputRating.getRating();
            String description = etInputDescription.getText().toString();
            if (rating == 0) {
                Toast.makeText(this, "Rating tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()) {
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    addReview(rating, description, place_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            Toast.makeText(this, "Rating: " + rating + " | Description : "+description + " | " + place_id, Toast.LENGTH_SHORT).show();
        });
    }

    private void addReview(float rating, String description, Integer mapId) throws JSONException {
        RequestBody body;
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("stuntmap_id").value(mapId);
        json.key("rating").value(rating);
        json.key("desc").value(description);
        json.endObject();

        body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<ResponseAddReview> addReviewCall = endpoint.addReview(body);

        addReviewCall.enqueue(new Callback<ResponseAddReview>() {

            @Override
            public void onResponse(Call<ResponseAddReview> call, Response<ResponseAddReview> response) {
                try {
                    if (response.body().getSuccess() == true) {
                        Toast.makeText(getApplicationContext(), "Berhasil mendaftar!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Anda Sudah Pernah Mereview Tempat Ini", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Gagal Mengirim Data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAddReview> call, Throwable t) {
                Toast.makeText(AddReviewActivity.this, "Gagal mengirim data"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GAGAL", "onFailure: "+t.getMessage());
            }
        });
    }
}
