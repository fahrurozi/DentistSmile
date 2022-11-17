package com.gemastik.dentistsmile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.gemastik.dentistsmile.data.model.profil.ResponseGetProfile;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.MainInterface;
import com.gemastik.dentistsmile.ui.account.AccountFragment;
import com.gemastik.dentistsmile.ui.child.management.ChildManagementFragment;
import com.gemastik.dentistsmile.ui.get_started.GetStartedActivity;
import com.gemastik.dentistsmile.ui.home.HomeFragment;
import com.gemastik.dentistsmile.ui.register.profile.AddProfileDataV2Activity;
import com.gemastik.dentistsmile.ui.register.profile.ProfileFirstActivity;
import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements MainInterface {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button btnTestYolo = findViewById(R.id.btnTestYolo);
//        btnTestYolo.setOnClickListener(v ->
//                startActivity(new Intent(this, TestYolo.class))
//        );
//    }
    Context mContext;
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    BottomNavigationView bnHome;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getProfile();
        setContentView(R.layout.activity_main);
        bnHome = findViewById(R.id.bnHome);

        sharedPref = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        bnHome.setOnItemSelectedListener(item -> openFragment(item.getItemId()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Default fragment
        openFragment(R.id.nav_home);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean openFragment(Integer menuID) {
        switch (menuID) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, new HomeFragment()).commit();
                return true;
            case R.id.nav_child:
//                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, new ChildFragment()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, new ChildManagementFragment()).commit();
                return true;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, new AccountFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public void openMenuNav(Integer menuID) {
        bnHome.setSelectedItemId(menuID);
        openFragment(menuID);
    }

    private void getProfile(){
        try {
            Call<ResponseGetProfile> getProfileCall = endpoint.getProfile( );
            getProfileCall.enqueue(new retrofit2.Callback<ResponseGetProfile>() {
                @Override
                public void onResponse(Call<ResponseGetProfile> call, retrofit2.Response<ResponseGetProfile> response) {
                    try {
                        if(response.body().getMessages().equals("success")){
                            if (response.body().getData().get(0).getNama() == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Lengkapi Profil");
                                builder.setMessage("Mohon Lengkapi Biodata Profil Terlebih Dahulu");
                                builder.setPositiveButton("Ok", (dialog, which) -> {
                                    startActivity(new Intent(getApplicationContext(), AddProfileDataV2Activity.class));
                                    finish();
                                });
                                builder.show();
//                                startActivity(new Intent(getApplicationContext(), ProfileFirstActivity.class));
//                                finish();
                            }else{
                                editor.putString(getString(R.string.profile_name), response.body().getData().get(0).getNama());
                                editor.putString(getString(R.string.pendidikan), response.body().getData().get(0).getPendidikan());
                                editor.putString(getString(R.string.tempat_lahir), response.body().getData().get(0).getTempat_lahir());
                                editor.putString(getString(R.string.tanggal_lahir), response.body().getData().get(0).getTanggal_lahir());
                                editor.putString(getString(R.string.alamat), response.body().getData().get(0).getAlamat());
                                editor.apply();
                                Log.d("TEST", "editor: "+response.body().getData().get(0).getNama());
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d("TEST", "editor: "+e);
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetProfile> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}