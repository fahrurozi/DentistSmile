package com.gemastik.dentistsmile.ui.medical_checkup.physical_checkup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.checkup_physic.ResponseCheckupPhysic;
import com.gemastik.dentistsmile.data.model.children.add.ResponseAddChild;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.child.management.ChildAddFragment;
import com.gemastik.dentistsmile.ui.child.management.ChildManagementFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

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


    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_physical);

        etTinggiBadan = findViewById(R.id.etTinggiBadan);
        etBeratBadan = findViewById(R.id.etBeratBadan);
        etSistole = findViewById(R.id.etSistole);
        etDiastole = findViewById(R.id.etDiastole);

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


                if(emptyAnswer==true || inputBB.isEmpty() || inputTB.isEmpty() || inputSistole.isEmpty() || inputDiastole.isEmpty() || inputMsoal7.isEmpty()){
                    Toast.makeText(PhysicalCheckupActivity.this, "Harap isi semua pertanyaan", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("TEST", "onClick: "+childId);
                    storePhysicalCheckup();
//                    for (int i = 1; i<=6; i++){
//                        Log.d("TEST", "Msoal-"+i+" = "+hashMsoal.get(i));
//                    }
//
//                    for (int i = 1; i<=9; i++){
//                        Log.d("TEST", "Tsoal-"+i+" = "+hashTsoal.get(i));
//                    }
                }
//                Toast.makeText(PhysicalCheckupActivity.this,"hash"+hashMsoal.get(1),Toast.LENGTH_SHORT).show();

            }
        });
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

    private void storePhysicalCheckup(){
        try {
            Call<ResponseCheckupPhysic> addPhysicalCheckupCall = endpoint.addPhysicalCheckup(
                    childId,
                    "1",
                    "2",
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
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseCheckupPhysic> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
