package com.gemastik.dentistsmile.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.MainInterface;
import com.gemastik.dentistsmile.ui.article.ArticleActivity;
import com.gemastik.dentistsmile.ui.doctor.DoctorActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class HomeFragment extends Fragment {


    private SharedPreferences sharedPref;
    private MainInterface parent;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Button btnTest = view.findViewById(R.id.btn_new_account_create);



        //Session
        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        CardView cardMenuArticle = view.findViewById(R.id.cardMenuArticle);
        CardView cardMenuDoctor = view.findViewById(R.id.cardMenuDoctor);

//        tvName.setText(sharedPref.getString(getString(R.string.name), ""));
//        tvUsername.setText(sharedPref.getString(getString(R.string.username), ""));

//        btnStuntingTrace.setOnClickListener(v -> parent.openMenuNav(R.id.nav_child));
//
//        btn_go_to_trace.setOnClickListener(v -> parent.openMenuNav(R.id.nav_child));
//
        cardMenuArticle.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ArticleActivity.class))
        );

        cardMenuDoctor.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), DoctorActivity.class))
        );


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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (MainInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


}
