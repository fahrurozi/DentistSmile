package com.gemastik.dentistsmile.ui.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.child.management.ChildManagementFragment;
import com.gemastik.dentistsmile.ui.dmft.DmftActivity;
import com.gemastik.dentistsmile.ui.home.HomeFragment;
import com.gemastik.dentistsmile.ui.medical_checkup.dentist_checkup.DentistCheckupActivity;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.HistoryCheckupActivity;
import com.gemastik.dentistsmile.ui.medical_checkup.physical_checkup.PhysicalCheckupActivity;
import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;

public class ChildMenuFragment extends Fragment {
    private SharedPreferences sharedPref;
    public Integer childId;
    public String childName;
    private TextView tvName;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        CardView btnPhysicalCheckup = view.findViewById(R.id.btnPhysicalCheckup);
        CardView btnDentistCheckup = view.findViewById(R.id.btnDentistCheckup);
        CardView btnLiveDentistAssistant = view.findViewById(R.id.btnLiveDentistAssistant);
//        CardView btnUploadDMFT = view.findViewById(R.id.btnUploadDMFT);
        CardView btnHistoryCheckup = view.findViewById(R.id.btnHistoryCheckup);

        tvName = view.findViewById(R.id.tvName);

        childId = getArguments().getInt("childId");
        childName = getArguments().getString("childName");

        tvName.setText(childName);

        ImageView btnBack =view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flHome, new ChildManagementFragment());
            fragmentTransaction.commit();
        });

//        btnPhysicalCheckup.setOnClickListener(v ->
//                Intent intent = new Intent(this, TributeDetailActivity.class);
//                startActivity(new Intent(requireContext(), PhysicalCheckupActivity.class))
//        );
        btnPhysicalCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PhysicalCheckupActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });

        btnDentistCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DentistCheckupActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });

        btnLiveDentistAssistant.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), TestYolo.class))
        );

//        btnUploadDMFT.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), DmftActivity.class))
//        );

        btnHistoryCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryCheckupActivity.class);
                intent.putExtra("childId", childId.toString());
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });
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
