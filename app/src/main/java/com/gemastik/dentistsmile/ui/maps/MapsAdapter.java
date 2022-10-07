package com.gemastik.dentistsmile.ui.maps;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.maps.DataPlace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.ViewHolder> {
    private List<DataPlace> rvData = new ArrayList();
    private MapsInterface mapsInterface;

    public MapsAdapter(MapsInterface mapsInterface) {
        this.mapsInterface = mapsInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMaps;
        public TextView tvName;
        public TextView tvJarak;
        public TextView tvOpen;
        public TextView btnDirection;
        public TextView btnShare;
        public LinearLayout llRoot;

        public ViewHolder(View v) {
            super(v);
            ivMaps = v.findViewById(R.id.ivMaps);
            tvName = v.findViewById(R.id.tvName);
            tvJarak = v.findViewById(R.id.tvJarak);
            tvOpen = v.findViewById(R.id.tvOpen);
            btnDirection = v.findViewById(R.id.btnDirection);
            btnShare = v.findViewById(R.id.btnShare);
            llRoot = v.findViewById(R.id.llRoot);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_maps, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataPlace data = rvData.get(position);
        holder.tvName.setText(data.getPlaceDetail().getName());
        holder.tvJarak.setText(data.getPlaceDetail().getTypes().get(0));
        String open;
        if (getCurrentWeek() == 0) {
            open = data.getPlaceDetail().getOpening_hours().getWeekday_text().get(6).split(": ")[1];
        } else {
            open = data.getPlaceDetail().getOpening_hours().getWeekday_text().get(getCurrentWeek() - 1).split(": ")[1];
        }
        holder.tvOpen.setText(data.getPlaceDetail().getOpening_hours().getOpenNow() ? open : "Closed");
        holder.tvOpen.setTextColor(data.getPlaceDetail().getOpening_hours().getOpenNow() ? holder.tvOpen.getContext().getResources().getColor(R.color.blue) : holder.tvOpen.getContext().getResources().getColor(R.color.red_no));
        if (data.getPlaceDetail().getPhotos() != null && data.getPlaceDetail().getPhotos().size() > 0) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyAN6a7kSklwRHnNojU72nDnCfhYGhrATh0&photo_reference=";
            Picasso.get().load(
                    url + data.getPlaceDetail().getPhotos().get(0).getPhotoReference()).into(holder.ivMaps);
        }
        holder.ivMaps.setOnClickListener(v -> mapsInterface.onClick(data));
        holder.btnDirection.setOnClickListener(v -> mapsInterface.onDirection(data));
        holder.btnShare.setOnClickListener(v -> mapsInterface.onShare(data));
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
