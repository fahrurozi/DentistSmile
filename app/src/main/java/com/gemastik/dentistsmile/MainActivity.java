package com.gemastik.dentistsmile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gemastik.dentistsmile.ui.MainInterface;
import com.gemastik.dentistsmile.ui.child.management.ChildManagementFragment;
import com.gemastik.dentistsmile.ui.home.HomeFragment;
import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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


    BottomNavigationView bnHome;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnHome = findViewById(R.id.bnHome);

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
//                getSupportFragmentManager().beginTransaction().replace(R.id.flHome, new AccountFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public void openMenuNav(Integer menuID) {
        bnHome.setSelectedItemId(menuID);
        openFragment(menuID);
    }
}