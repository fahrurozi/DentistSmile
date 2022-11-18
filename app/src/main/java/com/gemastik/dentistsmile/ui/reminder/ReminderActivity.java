package com.gemastik.dentistsmile.ui.reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.reminder.ResponseReminder;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;

public class ReminderActivity extends AppCompatActivity {
    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();
    private ReminderAdapter adapter;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        adapter = new ReminderAdapter();
        RecyclerView rvReminderData = findViewById(R.id.rvReminderData);
        rvReminderData.setAdapter(adapter);
        rvReminderData.setLayoutManager(new LinearLayoutManager(this));
        getReminder();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


    }

    private void getReminder() {
        endpoint.getReminder().enqueue(new retrofit2.Callback<ResponseReminder>() {
            @Override
            public void onResponse(Call<ResponseReminder> call, retrofit2.Response<ResponseReminder> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMessages().equals("success")) {
                    adapter.insertDataList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseReminder> call, Throwable t) {
                if (Objects.equals(t.getMessage(), "closed")) {
                    getReminder();
                } else {
                    Log.e("ERRRORORO", "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


}
