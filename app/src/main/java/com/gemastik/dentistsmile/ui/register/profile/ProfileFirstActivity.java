package com.gemastik.dentistsmile.ui.register.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.view.DatePickerFragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFirstActivity extends AppCompatActivity  {
    public Integer dates, months, years;
    private TextView et_tanggalLahir;
    private EditText et_nama, et_tempatLahir, et_pendidikan;
    final Calendar myCalendar= Calendar.getInstance();
    public String inputNama, inputPOB, inputDOB, inputPendidikan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);

        Button btn_next = findViewById(R.id.btn_next);
        et_tanggalLahir = findViewById(R.id.et_tanggalLahir);
        et_nama = findViewById(R.id.et_nama);
        et_tempatLahir = findViewById(R.id.et_tempatLahir);
        et_pendidikan = findViewById(R.id.et_pendidikan);



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
                new DatePickerDialog(ProfileFirstActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNama = et_nama.getText().toString();
                inputPOB = et_tempatLahir.getText().toString();
                inputPendidikan = et_pendidikan.getText().toString();

                if(inputNama.isEmpty() || inputPOB.isEmpty() || inputDOB.isEmpty() || inputPendidikan.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Silahkan lengkapi field yang ada!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), ProfileSecondActivity.class);
                    intent.putExtra("nama", inputNama);
                    intent.putExtra("tempat_lahir", inputPOB);
                    intent.putExtra("tanggal_lahir", inputDOB);
                    intent.putExtra("pendidikan", inputPendidikan);
                    startActivity(intent);
                    finish();
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



}
