package com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.eye;

import android.annotation.SuppressLint;
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
import com.gemastik.dentistsmile.data.model.history.eye.DataHistoryEye;
import com.gemastik.dentistsmile.data.model.history.physic.DataHistoryPhysic;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic.HistoryPhysicAdapter;
import com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic.HistoryPhysicInterface;

import java.util.ArrayList;
import java.util.List;

public class HistoryEyeAdapter extends RecyclerView.Adapter<HistoryEyeAdapter.ViewHolder>{
    private List<DataHistoryEye> rvData = new ArrayList();

    private HistoryEyeInterface historyEyeInterface;

    public HistoryEyeAdapter(HistoryEyeInterface historyEyeInterface) {
        this.historyEyeInterface = historyEyeInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvCheckupDate, tvHasil;
        public LinearLayout llLabel;
        public ImageView ivBg;
        public CardView cvRoot;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvCheckupDate = v.findViewById(R.id.tvCheckupDate);
            tvHasil = v.findViewById(R.id.tvHasil);
        }
    }

    @Override
    public HistoryEyeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        return new HistoryEyeAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(HistoryEyeAdapter.ViewHolder holder, int position) {
        DataHistoryEye data = rvData.get(position);
        holder.tvName.setText("Pemeriksaan Mata "+(position+1));
        holder.tvCheckupDate.setText(data.getWaktu_pemeriksaan());
        holder.tvHasil.setText("Hasil : "+data.getHasil());
//        String[] label = data.getArticleTags().split("\\|");

//        holder.cvRoot.setOnClickListener(r -> {
//            if (historyPhysicInterface != null) {
//                historyPhysicInterface.onChildClick(data);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    public void insertDataList(List<DataHistoryEye> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }
}
