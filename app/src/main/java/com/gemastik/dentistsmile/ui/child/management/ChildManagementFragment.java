package com.gemastik.dentistsmile.ui.child.management;

import android.content.Context;
import android.content.Intent;
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
import com.gemastik.dentistsmile.data.model.children.DataChildren;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.model.profil.ResponseGetProfile;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.ui.child.ChildMenu;
import com.gemastik.dentistsmile.ui.register.profile.ProfileFirstActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Response;

public class ChildManagementFragment extends Fragment {

    List<DataChildren> data = new ArrayList();
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();
    private SharedPreferences sharedPref;
    private ChildManagementAdapter adapter;
    private FloatingActionButton fabButton;
    private SpotsDialog spotsDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        adapter = new ChildManagementAdapter();

//        CardView cvHelloRoot = view.findViewById(R.id.cvHelloRoot);
//        cvHelloRoot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChildMenu fragmentobj = new ChildMenu();
//                FragmentManager manager = ((MainActivity)v.getContext()).getSupportFragmentManager();
//                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
//            }
//        });

        FloatingActionButton fabButton = view.findViewById(R.id.fabAddChildren);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ChildAddFragment fragmentobj = new ChildAddFragment();
                FragmentManager manager = ((MainActivity)v.getContext()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
            }
        });

        RecyclerView rvData = view.findViewById(R.id.rvChildData);
        rvData.setAdapter(adapter);
        rvData.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.insertDataList(data);
        spotsDialog = new SpotsDialog(getContext(), "Mohon Tunggu...");
        spotsDialog.show();
        getChildren();

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

    private void getChildren(){
        try {
            Call<ResponseGetChildren> getChildrenCall = endpoint.getChildren( );
            getChildrenCall.enqueue(new retrofit2.Callback<ResponseGetChildren>() {
                @Override
                public void onResponse(Call<ResponseGetChildren> call, Response<ResponseGetChildren> response) {
                    spotsDialog.dismiss();
                    try {
                        if(response.body().getMessage().equals("Success")){
                           adapter.insertDataList(response.body().getData());
                        }
                        else {
                            Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetChildren> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
