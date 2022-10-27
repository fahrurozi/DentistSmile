package com.gemastik.dentistsmile.ui.register.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.profil.ResponseEditProfile;
import com.gemastik.dentistsmile.data.model.profil.ResponseStoreProfil;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;

public class AddProfileDataV2Activity extends AppCompatActivity {


    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();

    private TextView et_tanggalLahir;
    private EditText et_nama, et_tempatLahir, et_pendidikan, et_alamat;
    final Calendar myCalendar= Calendar.getInstance();
    public String inputNama, inputPOB, inputDOB, inputPendidikan, inputAlamat;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile_new);

        Button btn_submit = findViewById(R.id.btn_submit);
        et_tanggalLahir = findViewById(R.id.et_tanggalLahir);
        et_nama = findViewById(R.id.et_nama);
        et_tempatLahir = findViewById(R.id.et_tempatLahir);
        et_pendidikan = findViewById(R.id.et_pendidikan);
        et_alamat = findViewById(R.id.et_alamat);

        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        et_tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddProfileDataV2Activity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNama = et_nama.getText().toString();
                inputPOB = et_tempatLahir.getText().toString();
                inputPendidikan = et_pendidikan.getText().toString();
                inputAlamat = et_alamat.getText().toString();

                if(inputNama.isEmpty() || inputPOB.isEmpty() || inputDOB.isEmpty() || inputPendidikan.isEmpty() || inputAlamat.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Silahkan lengkapi field yang ada!", Toast.LENGTH_SHORT).show();
                }else{
                    storeProfile();
                }
            }
        });

    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_tanggalLahir.setText(dateFormat.format(myCalendar.getTime()));
        inputDOB = dateFormat.format(myCalendar.getTime());
    }

    public void storeProfile(){
        try {
            Call<ResponseEditProfile> storeProfileCall = endpoint.updateProfile(
                    inputNama, inputPOB, inputDOB, inputAlamat, inputPendidikan
            );
            storeProfileCall.enqueue(new retrofit2.Callback<ResponseEditProfile>() {
                @Override
                public void onResponse(Call<ResponseEditProfile> call, retrofit2.Response<ResponseEditProfile> response) {
                    try {
                        if(response.body().getMessage().equals("success")){
                            editor.putString(getString(R.string.profile_name), response.body().getData().getProfilorangtua().getNama());
                            editor.putString(getString(R.string.pendidikan), response.body().getData().getProfilorangtua().getPendidikan());
                            editor.putString(getString(R.string.email), response.body().getData().getEmail());
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Gagal Update Data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal Update Data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal Update Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
