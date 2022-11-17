package com.gemastik.dentistsmile.ui.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.ui.get_started.GetStartedActivity;

public class AccountFragment extends Fragment {

    private SharedPreferences sharedPref;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Session
        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvAlamat = view.findViewById(R.id.tvAlamat);
        TextView tvTempatLahir = view.findViewById(R.id.tvTempatLahir);
        TextView tvTanggalLahir = view.findViewById(R.id.tvTanggalLahir);
        TextView tvPendidikan = view.findViewById(R.id.tvPendidikan);


        CardView btnLogout = view.findViewById(R.id.btn_logout);

        tvName.setText(sharedPref.getString(getString(R.string.profile_name), ""));
        tvPendidikan.setText(sharedPref.getString(getString(R.string.pendidikan), ""));
        tvTempatLahir.setText(sharedPref.getString(getString(R.string.tempat_lahir), ""));
        tvTanggalLahir.setText(sharedPref.getString(getString(R.string.tanggal_lahir), ""));
        tvAlamat.setText(sharedPref.getString(getString(R.string.alamat), ""));




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getContext(), GetStartedActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }
}
