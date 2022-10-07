package com.gemastik.dentistsmile.ui.doctor;

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
import com.gemastik.dentistsmile.data.model.article.DataArticle;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.article.ArticleAdapter;
import com.gemastik.dentistsmile.ui.article.ArticleInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{
    private List<DataArticle> rvData = new ArrayList();
    ;
    private DoctorInterface doctorInterface;

    public DoctorAdapter(DoctorInterface doctorInterface) {
        this.doctorInterface = doctorInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDescription;
        public LinearLayout llLabel;
        public ImageView ivBg;
        public CardView cvRoot;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvHelloName);
            tvDescription = v.findViewById(R.id.tv_subtitle);
            llLabel = v.findViewById(R.id.llHelloLabel);
            ivBg = v.findViewById(R.id.ivHelloBackground);
            cvRoot = v.findViewById(R.id.cvHelloRoot);
        }
    }

    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_doctor, parent, false);
        return new DoctorAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(DoctorAdapter.ViewHolder holder, int position) {
        DataArticle data = rvData.get(position);
        holder.tvName.setText(data.getTitle());
        holder.tvDescription.setText(data.getArticleTags());
//        String[] label = data.getArticleTags().split("\\|");
        Log.e("TAG", "onBindViewHolder: " + ApiService.BASE_URL +"/static/"+ data.getArticleCoverFile());
        Log.e("TAG", "onBindViewHolder: " + data.toString());
        Picasso.get().load(ApiService.BASE_URL +"static/"+ data.getArticleCoverFile()).into(holder.ivBg);


        holder.cvRoot.setOnClickListener(r -> {
            if (doctorInterface != null) {
                doctorInterface.onChildClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    public void insertDataList(List<DataArticle> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }

}
