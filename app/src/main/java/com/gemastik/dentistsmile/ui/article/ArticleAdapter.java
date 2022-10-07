package com.gemastik.dentistsmile.ui.article;


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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
    private List<DataArticle> rvData = new ArrayList();
    ;
    private ArticleInterface articleInterface;

    public ArticleAdapter(ArticleInterface articleInterface) {
        this.articleInterface = articleInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public LinearLayout llLabel;
        public ImageView ivBg;
        public CardView cvRoot;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvInfoName);
            llLabel = v.findViewById(R.id.llInfoLabel);
            ivBg = v.findViewById(R.id.ivInfoBackground);
            cvRoot = v.findViewById(R.id.cvInfoRoot);
        }
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_info, parent, false);
        return new ArticleAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        DataArticle data = rvData.get(position);
        holder.tvName.setText(data.getTitle());
        String[] label = data.getArticleTags().split("\\|");
        Log.e("TAG", "onBindViewHolder: " + ApiService.BASE_URL +"/static/"+ data.getArticleCoverFile());
        Log.e("TAG", "onBindViewHolder: " + data.toString());
        Picasso.get().load(ApiService.BASE_URL +"static/"+ data.getArticleCoverFile()).into(holder.ivBg);


        holder.cvRoot.setOnClickListener(r -> {
            if (articleInterface != null) {
                articleInterface.onChildClick(data);
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
