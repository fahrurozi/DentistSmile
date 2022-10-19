package com.gemastik.dentistsmile.ui.medical_checkup.history_checkup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.ear.HistoryEarActivity;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.eye.HistoryEyeActivity;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic.HistoryPhysicActivity;

public class HistoryCheckupActivity extends AppCompatActivity {

    private CardView btnHistoryPhysic, btnHistoryEye, btnHistoryEar;

    private String childName, childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_menu);

        childName = getIntent().getStringExtra("childName");
        childId = getIntent().getStringExtra("childId");
        TextView tvName = findViewById(R.id.tvName);

        tvName.setText(childName);


        btnHistoryPhysic = findViewById(R.id.btnHistoryPhysic);
        btnHistoryEye = findViewById(R.id.btnHistoryEye);
        btnHistoryEar = findViewById(R.id.btnHistoryEar);

        btnHistoryPhysic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryPhysicActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });

        btnHistoryEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryEyeActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });

        btnHistoryEar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryEarActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });


    }
}
