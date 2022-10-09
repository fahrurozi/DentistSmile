package com.gemastik.dentistsmile.ui.child.management;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.child.ChildMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;

public class ChildManagementFragment extends Fragment {

//    List<DataChildren> data = new ArrayList();
    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();
    private SharedPreferences sharedPref;
//    private ChildManagementAdapter adapter;
    private FloatingActionButton fabButton;
    private SpotsDialog spotsDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        CardView cvHelloRoot = view.findViewById(R.id.cvHelloRoot);
        cvHelloRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildMenu fragmentobj = new ChildMenu();
                FragmentManager manager = ((MainActivity)v.getContext()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
            }
        });

        FloatingActionButton fabButton = view.findViewById(R.id.fabAddChildren);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ChildAddFragment fragmentobj = new ChildAddFragment();
                FragmentManager manager = ((MainActivity)v.getContext()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
            }
        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_list, container, false);
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
