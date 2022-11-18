package com.gemastik.dentistsmile.ui.reminder;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.reminder.DataReminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{
    private List<DataReminder> rvData = new ArrayList();
    ;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRTime, tvHospital, tvDescription, tvName;
        public LinearLayout llLabel;
        public ImageView ivBg;
        public CardView cvRoot;

        public ViewHolder(View v) {
            super(v);
            tvRTime = v.findViewById(R.id.tvRTime);
            tvHospital = v.findViewById(R.id.tvHospital);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvName = v.findViewById(R.id.tvName);
        }
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reminder, parent, false);
        return new ReminderAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int position) {
        DataReminder data = rvData.get(position);
        holder.tvRTime.setText(data.getTanggal());
        holder.tvHospital.setText(data.getPuskesmas());
        holder.tvDescription.setText(data.getDeskripsi());
        holder.tvName.setText(data.getAnak());
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    public void insertDataList(List<DataReminder> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }

}
