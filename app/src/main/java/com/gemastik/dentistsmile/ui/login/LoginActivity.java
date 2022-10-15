package com.gemastik.dentistsmile.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.login.ResponseLogin;
import com.gemastik.dentistsmile.data.model.register.ResponseRegister;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.root.App;

import org.json.JSONException;
import org.json.JSONStringer;

import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, test_without_login;
    private EditText etEmail, etPassword;

    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.button_submit_login);
        test_without_login = findViewById(R.id.test_without_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        test_without_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Silahkan lengkapi field yang ada!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("REGISTER", "onClick: "+password);
                    login(email, password);
//                    String test_token = App.sharedPref.getString("token", null);
//                    Log.d("TOKEN", "onClick: "+test_token);
                }
            }
        });
    }

    private void login(String email, String password) {
        try {
            RequestBody email_body = RequestBody.create(okhttp3.MultipartBody.FORM, email);
            RequestBody password_body = RequestBody.create(okhttp3.MultipartBody.FORM, password);
            Call<ResponseLogin> userCall = endpoint.login(
                    email_body,
                    password_body
            );
            userCall.enqueue(new retrofit2.Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, retrofit2.Response<ResponseLogin> response) {
                    try {
                        if (response.body().getCode() == 200) {
                            Toast.makeText(getApplicationContext(), "Berhasil mendaftar!", Toast.LENGTH_SHORT).show();
                            editor.putString(getString(R.string.token), response.body().getData().getToken());
                            editor.apply();
                            Log.d("TOKEN", "inside function: "+response.body().getData().getToken());
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Login! else", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal Login! catch 1", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal Login! fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Gagal mendaftar! catch 2", Toast.LENGTH_SHORT).show();
        }
    }

    


}
