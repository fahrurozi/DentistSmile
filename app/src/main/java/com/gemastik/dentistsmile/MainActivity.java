package com.gemastik.dentistsmile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTestYolo = findViewById(R.id.btnTestYolo);
        btnTestYolo.setOnClickListener(v ->
                startActivity(new Intent(this, TestYolo.class))
        );
    }
}