package com.gemastik.dentistsmile.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.register.ResponseRegister;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.get_started.GetStartedActivity;
import com.gemastik.dentistsmile.ui.register.profile.ProfileFirstActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etName, etUsername, etEmail, etPassword;
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);


        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");
        Button btnSubmit = findViewById(R.id.btn_submit_register);
        Button btntest_profile_field = findViewById(R.id.test_profile_field);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Silahkan lengkapi field yang ada!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REGISTER", "onClick: " + password);
                    spotsDialog.show();
                    register(email, password);
                }
            }
        });

        btntest_profile_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void register(String email, String password) {

        try {
            RequestBody email_body = RequestBody.create(okhttp3.MultipartBody.FORM, email);
            RequestBody password_body = RequestBody.create(okhttp3.MultipartBody.FORM, password);
            Call<ResponseRegister> userCall = endpoint.register(
                    email_body,
                    password_body
            );
            userCall.enqueue(new retrofit2.Callback<ResponseRegister>() {
                @Override
                public void onResponse(Call<ResponseRegister> call, retrofit2.Response<ResponseRegister> response) {
                    spotsDialog.dismiss();
                    try {

                        Log.d("TEST", "onResponse: "+response.body());
                        if (response.body()!=null && response.body().getMessage().equals("success")) {
                            Toast.makeText(getApplicationContext(), "Berhasil mendaftar!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), GetStartedActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if((response.body().getCode() != null && response.body().getCode() == 422)){
                                if (response.body().getErrorDetails().getEmail().get(0) != null) {
                                    Toast.makeText(getApplicationContext(), response.body().getErrorDetails().getEmail().get(0), Toast.LENGTH_SHORT).show();
                                }
                                if (response.body().getErrorDetails().getPassword().get(0) != null) {
                                    Toast.makeText(getApplicationContext(), response.body().getErrorDetails().getPassword().get(0), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Gagal mendaftar!", Toast.LENGTH_SHORT).show();
//                        }
                    } catch (Exception e) {
                        Log.d("TEST", "onResponse: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Gagal mendaftar!1", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseRegister> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mendaftar!2", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Gagal mendaftar!3", Toast.LENGTH_SHORT).show();
        }
    }
}
