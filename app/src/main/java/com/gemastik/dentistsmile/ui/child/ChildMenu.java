package com.gemastik.dentistsmile.ui.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.child.management.ChildAddFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dmax.dialog.SpotsDialog;

public class ChildMenu extends Fragment {

    private SharedPreferences sharedPref;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_menu, container, false);
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.flHome, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
