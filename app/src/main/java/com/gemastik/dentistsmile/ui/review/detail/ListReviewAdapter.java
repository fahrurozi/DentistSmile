package com.gemastik.dentistsmile.ui.review.detail;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.review.DataReview;

import java.util.ArrayList;
import java.util.List;

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.ViewHolder>{
    private List<DataReview> rvDataReview = new ArrayList();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDescription;
        public RatingBar rbRating;
        public CardView cvRoot;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDescription = v.findViewById(R.id.tvDescription);
            cvRoot = v.findViewById(R.id.cvRoot);
            rbRating = v.findViewById(R.id.rbRating);
        }
    }

    @Override
    public ListReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_data, parent, false);
        return new ListReviewAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ListReviewAdapter.ViewHolder holder, int position) {
        DataReview data = rvDataReview.get(position);
        holder.tvName.setText(data.getUser());
        holder.tvDescription.setText(data.getDesc());
        holder.rbRating.setRating(data.getRating());
    }

    @Override
    public int getItemCount() {
        return rvDataReview.size();
    }

    public void insertDataList(List<DataReview> inputData) {
        this.rvDataReview.clear();
        this.rvDataReview.addAll(inputData);
        notifyDataSetChanged();
    }
}
