package com.gemastik.dentistsmile.ui.review;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.maps.DataPlace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private List<DataPlace> rvData = new ArrayList();
    private ReviewInterface reviewInterface;

    public ReviewAdapter(ReviewInterface reviewInterface) {
        this.reviewInterface = reviewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivTribute;
        public TextView tvName;
        public TextView tvSubtitle;
        public RatingBar rbRating;
        public TextView tvRating;
        public CardView cvRoot;


        public ViewHolder(View v) {
            super(v);
            ivTribute = v.findViewById(R.id.ivTribute);
            tvName = v.findViewById(R.id.tvName);
            tvSubtitle = v.findViewById(R.id.tvSubtitle);
            rbRating = v.findViewById(R.id.rbRating);
            tvRating = v.findViewById(R.id.tvRating);
            cvRoot = v.findViewById(R.id.cvRoot);

        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        return new ReviewAdapter.ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        DataPlace data = rvData.get(position);
        holder.tvName.setText(data.getPlaceDetail().getName());
        holder.tvSubtitle.setText(data.getPlaceDetail().getTypes().get(0));
        holder.rbRating.setRating(data.getDbData().getAvg_rating());
        if (data.getPlaceDetail().getPhotos() != null && data.getPlaceDetail().getPhotos().size() > 0) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyAN6a7kSklwRHnNojU72nDnCfhYGhrATh0&photo_reference=";
            Picasso.get().load(
                    url + data.getPlaceDetail().getPhotos().get(0).getPhotoReference()).into(holder.ivTribute);
        }
//        holder.ivTribute.setOnClickListener(v -> tributeInterface.onClick(data));
        holder.cvRoot.setOnClickListener(v -> reviewInterface.onClick(data));
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    public void insertDataList(List<DataPlace> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }


    private int getCurrentWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
