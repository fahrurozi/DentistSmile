package com.gemastik.dentistsmile.ui.register.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.kecamatan.DataKecamatan;
import com.gemastik.dentistsmile.data.model.kecamatan.ResponseGetKecamatanAll;
import com.gemastik.dentistsmile.data.model.kelurahan.DataKelurahan;
import com.gemastik.dentistsmile.data.model.kelurahan.ResponseGetKelurahanByIdKec;
import com.gemastik.dentistsmile.data.model.login.ResponseLogin;
import com.gemastik.dentistsmile.data.model.profil.ResponseGetProfile;
import com.gemastik.dentistsmile.data.model.profil.ResponseStoreProfil;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
//implements AdapterView.OnItemSelectedListener
public class ProfileSecondActivity extends AppCompatActivity  {
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private SpotsDialog spotsDialog;
    private Spinner spinnerKecamatan, spinnerKelurahan;
    ProgressDialog loading;
    Context mContext;
    private Button btn_submit_register;
    private Integer id_kecamatan, id_kelurahan;
    private String selected_kecamatan, selected_kelurahan;
    public String inputNama, inputPOB, inputDOB, inputPendidikan, inputAlamat;
    public String inputIdKecamatan, inputIdKelurahan;
    private EditText et_alamat;

    private HashMap<String, Integer> hashKecamatan = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashKelurahan = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile_2);

        mContext = this;

        btn_submit_register = findViewById(R.id.btn_submit_register);
        spinnerKecamatan = findViewById(R.id.spinnerKecamatan);
        spinnerKelurahan = findViewById(R.id.spinnerKelurahan);
        et_alamat = findViewById(R.id.et_alamat);


        inputNama = getIntent().getStringExtra("nama");
        inputPOB = getIntent().getStringExtra("tempat_lahir");
        inputDOB = getIntent().getStringExtra("tanggal_lahir");
        inputPendidikan = getIntent().getStringExtra("pendidikan");

        Log.d("TEST", "onCreate: " + inputNama + inputPOB + inputDOB + inputPendidikan);


        // Spinner click listener
//        spinnerKecamatan.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");
        spotsDialog.show();
        getKecamatan();

        btn_submit_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAlamat = et_alamat.getText().toString();
                storeProfile();
//                Log.d("TEST", "onClick: "+inputNama);
//                Log.d("TEST", "onClick: "+inputPOB);
//                Log.d("TEST", "onClick: "+inputDOB);
//                Log.d("TEST", "onClick: "+inputPendidikan);
//                Log.d("TEST", "onClick: "+inputAlamat);
//                Log.d("TEST", "onClick: "+inputIdKecamatan);
//                Log.d("TEST", "onClick: "+inputIdKelurahan);
            }
        });


    }

    class KecamatanSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kecamatan = item;
            id_kecamatan = hashKecamatan.get(selected_kecamatan);
            inputIdKecamatan = id_kecamatan.toString();
            // Showing selected spinner item
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            getKelurahan(id_kecamatan);

//            Toast.makeText(v.getContext(), "Your choose :" ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class KelurahanSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kelurahan = item;
            id_kelurahan = hashKelurahan.get(selected_kelurahan);
            inputIdKelurahan = id_kelurahan.toString();
            // Showing selected spinner item
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//            Log.d("TEST", "onItemSelected: Kelurahan - "+selected_kelurahan + " " + id_kelurahan);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // On selecting a spinner item
//        String item = parent.getItemAtPosition(position).toString();
//
//        selected_kecamatan = item;
//        id_kecamatan = hashKecamatan.get(selected_kecamatan);
//        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//
//        getKelurahan(id_kecamatan);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }

    public void getKecamatan(){
        try {
            Call<ResponseGetKecamatanAll> kecamatanCall = endpoint.getKecamatanAll();
            kecamatanCall.enqueue(new retrofit2.Callback<ResponseGetKecamatanAll>() {
                @Override
                public void onResponse(Call<ResponseGetKecamatanAll> call, retrofit2.Response<ResponseGetKecamatanAll> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                List<DataKecamatan> rawKecamatan = response.body().getData();
                                for(int i = 0; i<rawKecamatan.size(); i++){
                                    hashKecamatan.put(rawKecamatan.get(i).getNama(), rawKecamatan.get(i).getId());
//                                    Log.d("Value Kecamtan", rawKecamatan.get(0).getNama());
                                }
                                setSpinnerKecamatan();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Login! else", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal Login! catch 1", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKecamatanAll> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal Login! fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    public void setSpinnerKecamatan(){
        Set<String> keySetListKecamatan = hashKecamatan.keySet();
        ArrayList<String> listKecamatanName = new ArrayList<String>(keySetListKecamatan);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterKecamatan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listKecamatanName);

        // Drop down layout style - list view with radio button
        dataAdapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerKecamatan.setAdapter(dataAdapterKecamatan);
        spinnerKecamatan.setOnItemSelectedListener(new KecamatanSpinnerClass());
    }

    public void setSpinnerKelurahan(){
        Set<String> keySetListKelurahan = hashKelurahan.keySet();
        ArrayList<String> listKelurahanName = new ArrayList<String>(keySetListKelurahan);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterKelurahan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listKelurahanName);

        // Drop down layout style - list view with radio button
        dataAdapterKelurahan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
//        spinnerKelurahan.setAdapter(dataAdapterKelurahan);
        spinnerKelurahan.setAdapter(dataAdapterKelurahan);
        spinnerKelurahan.setOnItemSelectedListener(new KelurahanSpinnerClass());
    }

    public void getKelurahan(Integer id_kecamatan){
        try {
            Call<ResponseGetKelurahanByIdKec> kelurahanCall = endpoint.getKelurahanByIdKec(
                    id_kecamatan
            );
            kelurahanCall.enqueue(new retrofit2.Callback<ResponseGetKelurahanByIdKec>() {
                @Override
                public void onResponse(Call<ResponseGetKelurahanByIdKec> call, retrofit2.Response<ResponseGetKelurahanByIdKec> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessage().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashKelurahan.clear();
                                List<DataKelurahan> rawKelurahan = response.body().getData();
                                for(int i = 0; i<rawKelurahan.size(); i++){
                                    hashKelurahan.put(rawKelurahan.get(i).getNama(), rawKelurahan.get(i).getId());
                                }
                                setSpinnerKelurahan();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "kel Gagal Login! else", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "kel Gagal Login! catch 1", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKelurahanByIdKec> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "kel Gagal Login! fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    public void storeProfile(){
        try {
            Call<ResponseStoreProfil> storeProfileCall = endpoint.storeProfile(
                    inputNama, inputIdKecamatan, inputIdKelurahan, inputPOB, inputDOB, inputPendidikan, inputAlamat
            );
            storeProfileCall.enqueue(new retrofit2.Callback<ResponseStoreProfil>() {
                @Override
                public void onResponse(Call<ResponseStoreProfil> call, retrofit2.Response<ResponseStoreProfil> response) {
                    try {
                        if(response.body().getMessages().equals("data berhasil ditambahkan")){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Gagal Login! profile else", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal Login!profile catch 1", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseStoreProfil> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal Login!profile fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }


}
