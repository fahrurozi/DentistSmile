package com.gemastik.dentistsmile.ui.get_started;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.login.LoginActivity;
import com.gemastik.dentistsmile.ui.register.RegisterActivity;

public class GetStartedActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btnLogin = findViewById(R.id.btn_sign_in);
        btnRegister = findViewById(R.id.btn_new_account_create);

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }
}
