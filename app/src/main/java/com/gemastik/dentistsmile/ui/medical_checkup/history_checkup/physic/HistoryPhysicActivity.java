package com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.model.history.physic.DataHistoryPhysic;
import com.gemastik.dentistsmile.data.model.history.physic.ResponseHistoryPhysic;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class HistoryPhysicActivity extends AppCompatActivity implements HistoryPhysicInterface {
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private HistoryPhysicAdapter adapter;

    private String childName, childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_checkup);

        childName = getIntent().getStringExtra("childName");
        childId = getIntent().getStringExtra("childId");

        TextView tvTitleMenu = findViewById(R.id.tvTitleMenu);
        tvTitleMenu.setText(R.string.pemeriksaan_fisik);


        adapter = new HistoryPhysicAdapter(this);
        RecyclerView rvData = findViewById(R.id.rvData);
        rvData.setAdapter(adapter);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        getHistoryCheckup();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


    }

    private void getHistoryCheckup() {
        try {
            Call<ResponseHistoryPhysic> getHistoryPhisicCall = endpoint.getHistoryPhysic(
                    childId
            );
            getHistoryPhisicCall.enqueue(new retrofit2.Callback<ResponseHistoryPhysic>() {
                @Override
                public void onResponse(Call<ResponseHistoryPhysic> call, Response<ResponseHistoryPhysic> response) {
                    try {
                        if(response.body().getMessages().equals("Success")){
                            adapter.insertDataList(response.body().getData());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseHistoryPhysic> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
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
    public void onChildClick(DataHistoryPhysic datainfo) {
//        startActivity(new Intent(this, HelloDetailActivity.class).putExtra("datacare", datainfo));
    }



}
