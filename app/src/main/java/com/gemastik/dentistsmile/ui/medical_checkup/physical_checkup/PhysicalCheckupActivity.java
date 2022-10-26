package com.gemastik.dentistsmile.ui.medical_checkup.physical_checkup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.checkup_physic.ResponseCheckupPhysic;
import com.gemastik.dentistsmile.data.model.children.add.ResponseAddChild;
import com.gemastik.dentistsmile.data.model.kecamatan.DataKecamatan;
import com.gemastik.dentistsmile.data.model.kecamatan.ResponseGetKecamatanAll;
import com.gemastik.dentistsmile.data.model.kelas.DataKelas;
import com.gemastik.dentistsmile.data.model.kelas.ResponseGetKelasByIdSek;
import com.gemastik.dentistsmile.data.model.kelurahan.DataKelurahan;
import com.gemastik.dentistsmile.data.model.kelurahan.ResponseGetKelurahanByIdKec;
import com.gemastik.dentistsmile.data.model.sekolah.DataSekolah;
import com.gemastik.dentistsmile.data.model.sekolah.ResponseGetSekolahByIdKel;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.child.management.ChildAddFragment;
import com.gemastik.dentistsmile.ui.child.management.ChildManagementFragment;
import com.gemastik.dentistsmile.ui.register.profile.ProfileSecondActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Response;

public class PhysicalCheckupActivity extends AppCompatActivity {

    private TextView tv_childName;
    private FloatingActionButton fabSimpan;
    private EditText etMSoal7;
    private EditText etTinggiBadan, etBeratBadan, etSistole, etDiastole;
    public String childName ;
    public String childId;

    private RadioGroup radioMsoal1Group, radioMsoal2Group, radioMsoal3Group, radioMsoal4Group, radioMsoal5Group, radioMsoal6Group;
    private RadioButton radioMsoal1Button, radioMsoal2Button, radioMsoal3Button, radioMsoal4Button, radioMsoal5Button, radioMsoal6Button;

    private RadioGroup radioTsoal1Group, radioTsoal2Group, radioTsoal3Group, radioTsoal4Group, radioTsoal5Group, radioTsoal6Group, radioTsoal7Group, radioTsoal8Group, radioTsoal9Group;
    private RadioButton radioTsoal1Button, radioTsoal2Button, radioTsoal3Button, radioTsoal4Button, radioTsoal5Button, radioTsoal6Button, radioTsoal7Button, radioTsoal8Button,radioTsoal9Button;

    private HashMap<Integer, String> hashMsoal = new HashMap<Integer, String>();
    private HashMap<Integer, String> hashTsoal = new HashMap<Integer, String>();

    public Boolean emptyAnswer;

    public String inputTB, inputBB, inputSistole, inputDiastole, inputMsoal7;

    public String inputIdKecamatan, inputIdKelurahan, inputIdSekolah, inputIdKelas;
    private Integer id_kecamatan, id_kelurahan, id_sekolah, id_kelas;
    private String selected_kecamatan, selected_kelurahan, selected_sekolah, selected_kelas;

    private Spinner spinnerKecamatan, spinnerKelurahan, spinnerSekolah, spinnerKelas;

    private SpotsDialog spotsDialog;

    private HashMap<String, Integer> hashKecamatan = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashKelurahan = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashSekolah = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashKelas = new HashMap<String, Integer>();


    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_physical);

        etTinggiBadan = findViewById(R.id.etTinggiBadan);
        etBeratBadan = findViewById(R.id.etBeratBadan);
        etSistole = findViewById(R.id.etSistole);
        etDiastole = findViewById(R.id.etDiastole);
        spinnerKecamatan = findViewById(R.id.spinnerKecamatan);
        spinnerKelurahan = findViewById(R.id.spinnerKelurahan);
        spinnerSekolah = findViewById(R.id.spinnerSekolah);
        spinnerKelas = findViewById(R.id.spinnerKelas);

        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");
        spotsDialog.show();
        getKecamatan();


        tv_childName = findViewById(R.id.tv_childName);
        fabSimpan = findViewById(R.id.fabSimpan);
        radioMsoal1Group=(RadioGroup)findViewById(R.id.radioMsoal1Group);
        radioMsoal2Group=(RadioGroup)findViewById(R.id.radioMsoal2Group);
        radioMsoal3Group=(RadioGroup)findViewById(R.id.radioMsoal3Group);
        radioMsoal4Group=(RadioGroup)findViewById(R.id.radioMsoal4Group);
        radioMsoal5Group=(RadioGroup)findViewById(R.id.radioMsoal5Group);
        radioMsoal6Group=(RadioGroup)findViewById(R.id.radioMsoal6Group);
        etMSoal7=findViewById(R.id.etMSoal7);

        radioTsoal1Group=(RadioGroup)findViewById(R.id.radioTsoal1Group);
        radioTsoal2Group=(RadioGroup)findViewById(R.id.radioTsoal2Group);
        radioTsoal3Group=(RadioGroup)findViewById(R.id.radioTsoal3Group);
        radioTsoal4Group=(RadioGroup)findViewById(R.id.radioTsoal4Group);
        radioTsoal5Group=(RadioGroup)findViewById(R.id.radioTsoal5Group);
        radioTsoal6Group=(RadioGroup)findViewById(R.id.radioTsoal6Group);
        radioTsoal7Group=(RadioGroup)findViewById(R.id.radioTsoal7Group);
        radioTsoal8Group=(RadioGroup)findViewById(R.id.radioTsoal8Group);
        radioTsoal9Group=(RadioGroup)findViewById(R.id.radioTsoal9Group);


        childName = getIntent().getStringExtra("childName");
        childId = getIntent().getStringExtra("childId");

        tv_childName.setText(childName);

        fabSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int selectedId=radioMsoal1Group.getCheckedRadioButtonId();
//                radioMsoal1Button=(RadioButton)findViewById(selectedId);

                inputBB = etBeratBadan.getText().toString();
                inputTB = etTinggiBadan.getText().toString();
                inputSistole = etSistole.getText().toString();
                inputDiastole = etDiastole.getText().toString();
                inputMsoal7 = etMSoal7.getText().toString();

                assignMsoaltoInput(radioMsoal1Group, radioMsoal1Button, 1);
                assignMsoaltoInput(radioMsoal2Group, radioMsoal2Button, 2);
                assignMsoaltoInput(radioMsoal3Group, radioMsoal3Button, 3);
                assignMsoaltoInput(radioMsoal4Group, radioMsoal4Button, 4);
                assignMsoaltoInput(radioMsoal5Group, radioMsoal5Button, 5);
                assignMsoaltoInput(radioMsoal6Group, radioMsoal6Button, 6);

                assignTsoaltoInput(radioTsoal1Group, radioTsoal1Button, 1);
                assignTsoaltoInput(radioTsoal2Group, radioTsoal2Button, 2);
                assignTsoaltoInput(radioTsoal3Group, radioTsoal3Button, 3);
                assignTsoaltoInput(radioTsoal4Group, radioTsoal4Button, 4);
                assignTsoaltoInput(radioTsoal5Group, radioTsoal5Button, 5);
                assignTsoaltoInput(radioTsoal6Group, radioTsoal6Button, 6);
                assignTsoaltoInput(radioTsoal7Group, radioTsoal7Button, 7);
                assignTsoaltoInput(radioTsoal8Group, radioTsoal8Button, 8);
                assignTsoaltoInput(radioTsoal9Group, radioTsoal9Button, 9);


                if(emptyAnswer==true || inputBB.isEmpty() || inputTB.isEmpty() || inputMsoal7.isEmpty()){
                    Toast.makeText(PhysicalCheckupActivity.this, "Harap isi semua pertanyaan", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("TEST", "onClick: " + childId);
                    storePhysicalCheckup();
                }
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

            getSekolah(id_kelurahan);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class SekolahSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_sekolah = item;
            id_sekolah = hashSekolah.get(selected_sekolah);
            inputIdSekolah = id_sekolah.toString();
            getKelas(id_sekolah);
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class KelasSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kelas = item;
            id_kelas = hashKelas.get(selected_kelas);
            inputIdKelas = id_kelas.toString();
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void assignMsoaltoInput(RadioGroup radioGroupQuestion, RadioButton radioButtonQuestion, Integer question){
        if(radioGroupQuestion.getCheckedRadioButtonId()==-1){
            emptyAnswer = true;
        }else{
            emptyAnswer = false;
            int selectedId=radioGroupQuestion.getCheckedRadioButtonId();
            radioButtonQuestion=(RadioButton)findViewById(selectedId);
            hashMsoal.put(question,radioButtonQuestion.getText().toString());
        }
    }

    private void assignTsoaltoInput(RadioGroup radioGroupQuestion, RadioButton radioButtonQuestion, Integer question){
        if(radioGroupQuestion.getCheckedRadioButtonId()==-1){
            emptyAnswer = true;
        }else {
            emptyAnswer = false;
            int selectedId = radioGroupQuestion.getCheckedRadioButtonId();
            radioButtonQuestion = (RadioButton) findViewById(selectedId);
            hashTsoal.put(question, radioButtonQuestion.getText().toString());
        }
    }



    public void setSpinner(String name, HashMap<String, Integer> hash, Spinner spinner) {
        Set<String> keySetList = hash.keySet();
        ArrayList<String> listName = new ArrayList<String>(keySetList);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listName);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (name.equals("kecamatan")) {
            spinnerKecamatan.setAdapter(dataAdapter);
            spinnerKecamatan.setOnItemSelectedListener(new KecamatanSpinnerClass());
        } else if (name.equals("kelurahan")) {
            spinnerKelurahan.setAdapter(dataAdapter);
            spinnerKelurahan.setOnItemSelectedListener(new KelurahanSpinnerClass());
        } else if (name.equals("sekolah")) {
            spinnerSekolah.setAdapter(dataAdapter);
            spinnerSekolah.setOnItemSelectedListener(new SekolahSpinnerClass());
        } else if (name.equals("kelas")) {
            spinnerKelas.setAdapter(dataAdapter);
            spinnerKelas.setOnItemSelectedListener(new KelasSpinnerClass());
        }
    }

    private void getKecamatan(){
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
                                setSpinner("kecamatan", hashKecamatan, spinnerKecamatan);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKecamatanAll> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

      private void getKelurahan(Integer id_kecamatan){
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
                                setSpinner("kelurahan", hashKelurahan, spinnerKelurahan);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKelurahanByIdKec> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void getSekolah(Integer id_kelurahan){
        try {
            Call<ResponseGetSekolahByIdKel> sekolahCall = endpoint.getSekolahByIdKel(
                    id_kelurahan
            );
            sekolahCall.enqueue(new retrofit2.Callback<ResponseGetSekolahByIdKel>() {
                @Override
                public void onResponse(Call<ResponseGetSekolahByIdKel> call, retrofit2.Response<ResponseGetSekolahByIdKel> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashSekolah.clear();
                                List<DataSekolah> rawSekolah = response.body().getData();
                                for(int i = 0; i<rawSekolah.size(); i++){
                                    hashSekolah.put(rawSekolah.get(i).getNama(), rawSekolah.get(i).getId());
                                    Log.d("TEST", "onResponse: "+rawSekolah.get(i).getNama());
                                }
                                setSpinner("sekolah", hashSekolah, spinnerSekolah);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetSekolahByIdKel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void getKelas(Integer id_sekolah){
        try {
            Call<ResponseGetKelasByIdSek> kelasCall = endpoint.getKelasByIdSek(
                    id_sekolah
            );
            kelasCall.enqueue(new retrofit2.Callback<ResponseGetKelasByIdSek>() {
                @Override
                public void onResponse(Call<ResponseGetKelasByIdSek> call, retrofit2.Response<ResponseGetKelasByIdSek> response) {
                    try {
                        Log.d("TEST", "onResponse kel: "+response.body().getMessages());
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashKelas.clear();
                                List<DataKelas> rawKelas = response.body().getData();
                                for(int i = 0; i<rawKelas.size(); i++){
                                    hashKelas.put(rawKelas.get(i).getKelas(), rawKelas.get(i).getId());
                                    Log.d("TEST", "onResponse: "+rawKelas.get(i).getKelas());
                                }
                                setSpinner("kelas", hashKelas, spinnerKelas);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKelasByIdSek> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void storePhysicalCheckup(){
        try {
            Call<ResponseCheckupPhysic> addPhysicalCheckupCall = endpoint.addPhysicalCheckup(
                    childId,
                    inputIdSekolah,
                    inputIdKelas,
                    inputTB,
                    inputBB,
                    inputSistole,
                    inputDiastole,
                    hashMsoal.get(1),
                    hashMsoal.get(2),
                    hashMsoal.get(3),
                    hashMsoal.get(4),
                    hashMsoal.get(5),
                    hashMsoal.get(6),
                    inputMsoal7,
                    hashTsoal.get(1),
                    hashTsoal.get(2),
                    hashTsoal.get(3),
                    hashTsoal.get(4),
                    hashTsoal.get(5),
                    hashTsoal.get(6),
                    hashTsoal.get(7),
                    hashTsoal.get(8),
                    hashTsoal.get(9)
            );
            addPhysicalCheckupCall.enqueue(new retrofit2.Callback<ResponseCheckupPhysic>() {
                @Override
                public void onResponse(Call<ResponseCheckupPhysic> call, Response<ResponseCheckupPhysic> response) {
                    try {
                        if(response.body().getMessage().equals("success")){
                            Toast.makeText(getApplicationContext(), "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseCheckupPhysic> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
