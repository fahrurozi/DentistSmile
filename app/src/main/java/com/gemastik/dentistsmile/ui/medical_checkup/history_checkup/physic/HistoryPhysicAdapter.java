package com.gemastik.dentistsmile.ui.medical_checkup.history_checkup.physic;

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
import com.gemastik.dentistsmile.data.model.history.physic.DataHistoryPhysic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryPhysicAdapter extends RecyclerView.Adapter<HistoryPhysicAdapter.ViewHolder>{
private List<DataHistoryPhysic> rvData = new ArrayList();

private HistoryPhysicInterface historyPhysicInterface;

public HistoryPhysicAdapter(HistoryPhysicInterface historyPhysicInterface) {
        this.historyPhysicInterface = historyPhysicInterface;
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvCheckupDate;
    public LinearLayout llLabel;
    public ImageView ivBg;
    public CardView cvRoot;

    public ViewHolder(View v) {
        super(v);
        tvName = v.findViewById(R.id.tvName);
        tvCheckupDate = v.findViewById(R.id.tvCheckupDate);
    }
}

    @Override
    public HistoryPhysicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        return new HistoryPhysicAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(HistoryPhysicAdapter.ViewHolder holder, int position) {
        DataHistoryPhysic data = rvData.get(position);
        holder.tvName.setText("Pemeriksaan Fisik "+position+1);
        holder.tvCheckupDate.setText(data.getWaktu_pemeriksaan());
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

    public void insertDataList(List<DataHistoryPhysic> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }
}
