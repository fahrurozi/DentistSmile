package com.gemastik.dentistsmile.ui.child.management;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.view.DatePickerFragment;
import com.gemastik.dentistsmile.data.model.children.add.ResponseAddChild;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private SharedPreferences sharedPref;
    private TextView etDOB, etName, etPOB;
    private FloatingActionButton fabSimpan;

    private SpotsDialog spotsDialog;
    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();

    public Integer dates, months, years;
    public Integer intGender;

    public String inputDOB;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        etPOB = view.findViewById(R.id.etPOB);
        etDOB = view.findViewById(R.id.etDOB);
        fabSimpan = view.findViewById(R.id.fabSimpan);
        etName = view.findViewById(R.id.etName);

        spotsDialog = new SpotsDialog(getContext(), "Mohon Tunggu...");

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(ChildAddFragment.this, 0);
                datePicker.show(getFragmentManager(), "date picker");

            }
        });

        fabSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner drop_jenisKelamin=(Spinner) getView().findViewById(R.id.spinnerGender);
                String inputGender=drop_jenisKelamin.getSelectedItem().toString();
                String inputName = etName.getText().toString();
                String inputDOB = etDOB.getText().toString();
                String inputPOB = etPOB.getText().toString();
                if(inputName.isEmpty() || inputDOB.isEmpty() || inputPOB.isEmpty()) {
                    Toast.makeText(getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    spotsDialog.show();
                    addChildren(inputName, inputPOB, inputDOB, inputGender);
                }
            }
        });



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_add, container, false);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        etDOB.setText(dateFormat.format(c.getTime()));
        inputDOB = dateFormat.format(c.getTime());

//        dates = Integer.toString(dayOfMonth);
//        months = Integer.toString(month);
//        years = Integer.toString(year);

        dates = dayOfMonth;
        months = month+1;
        years = year;

        Log.d("HAI", "onDateSet: "+dates+"-"+months+"-"+years);


    }

    private void addChildren(String nama, String tempat_lahir, String tanggal_lahir, String jenis_kelamin){
        try {
            Call<ResponseAddChild> addChildrenCall = endpoint.storeChildren(
                    nama,
                    tempat_lahir,
                    tanggal_lahir,
                    jenis_kelamin
            );
            addChildrenCall.enqueue(new retrofit2.Callback<ResponseAddChild>() {
                @Override
                public void onResponse(Call<ResponseAddChild> call, Response<ResponseAddChild> response) {
                    spotsDialog.dismiss();
                    try {
                        if(response.body().getMessage().equals("success")){
                            getActivity().getSupportFragmentManager().beginTransaction().remove(ChildAddFragment.this).commit();
                            loadFragment(new ChildManagementFragment());
                        }
                        else {
                            Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAddChild> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
