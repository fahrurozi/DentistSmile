package com.gemastik.dentistsmile.ui.child.management;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.view.DatePickerFragment;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private SharedPreferences sharedPref;
    private TextView etDOB, etName;
    private FloatingActionButton fabSimpan;

    private SpotsDialog spotsDialog;

    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();

    public Integer dates, months, years;
    public Integer intGender;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

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
        String currentDate = DateFormat.getDateInstance().format(c.getTime());

//        dates = Integer.toString(dayOfMonth);
//        months = Integer.toString(month);
//        years = Integer.toString(year);

        dates = dayOfMonth;
        months = month+1;
        years = year;

        Log.d("HAI", "onDateSet: "+dates+"-"+months+"-"+years);

        etDOB.setText(currentDate);
    }
}
