package com.gemastik.dentistsmile.ui.register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.R;

import org.json.JSONException;
import org.json.JSONStringer;

import okhttp3.RequestBody;
import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {
//    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etName, etUsername, etEmail, etPassword;
        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
//        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        Button btnSubmit = findViewById(R.id.btn_submit_register);
    }
}
