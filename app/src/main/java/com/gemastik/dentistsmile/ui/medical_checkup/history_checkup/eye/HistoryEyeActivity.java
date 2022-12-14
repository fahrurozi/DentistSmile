package com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.eye;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.history.eye.DataHistoryEye;
import com.gemastik.dentistsmile.data.model.history.eye.ResponseHistoryEye;
import com.gemastik.dentistsmile.data.model.history.physic.DataHistoryPhysic;
import com.gemastik.dentistsmile.data.model.history.physic.ResponseHistoryPhysic;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic.HistoryPhysicAdapter;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic.HistoryPhysicInterface;

import retrofit2.Call;
import retrofit2.Response;

public class HistoryEyeActivity extends AppCompatActivity implements HistoryEyeInterface {
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private HistoryEyeAdapter adapter;

    private String childName, childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_checkup);

        childName = getIntent().getStringExtra("childName");
        childId = getIntent().getStringExtra("childId");

        TextView tvTitleMenu = findViewById(R.id.tvTitleMenu);
        tvTitleMenu.setText(R.string.pemeriksaan_mata);


        adapter = new HistoryEyeAdapter(this);
        RecyclerView rvData = findViewById(R.id.rvData);
        rvData.setAdapter(adapter);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        getHistoryCheckup();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


    }

    private void getHistoryCheckup() {
        try {
            Call<ResponseHistoryEye> getHistoryEyeCall = endpoint.getHistoryEye(
                    childId
            );
            getHistoryEyeCall.enqueue(new retrofit2.Callback<ResponseHistoryEye>() {
                @Override
                public void onResponse(Call<ResponseHistoryEye> call, Response<ResponseHistoryEye> response) {
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
                public void onFailure(Call<ResponseHistoryEye> call, Throwable t) {
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
    public void onChildClick(DataHistoryEye datainfo) {
//        startActivity(new Intent(this, HelloDetailActivity.class).putExtra("datacare", datainfo));
    }
}
