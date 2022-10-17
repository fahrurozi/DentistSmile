package com.gemastik.dentistsmile.ui.child.management;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.MainActivity;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.children.DataChildren;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.child.ChildMenuFragment;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildManagementAdapter extends RecyclerView.Adapter<ChildManagementAdapter.ViewHolder>{
private List<DataChildren> rvData = new ArrayList();;

private ApiEndpoint endpoint = ApiService.getRetrofitInstance();


private Context ctx;


public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public TextView tvGender, tvDOB;
    public CardView cvRoot;

    public ViewHolder(View v) {
        super(v);
        tvName = v.findViewById(R.id.tvName);
        tvGender = v.findViewById(R.id.tvGender);
        tvDOB = v.findViewById(R.id.tvDOB);
        cvRoot = v.findViewById(R.id.cvRoot);

    }
}

    @Override
    public ChildManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataChildren data = rvData.get(position);
        holder.tvName.setText(data.getNama());
        holder.tvGender.setText(data.getJenis_kelamin());
        holder.tvDOB.setText(data.getTanggal_lahir());

        holder.cvRoot.setOnClickListener(r -> {
                Bundle bundle = new Bundle();
                bundle.putInt("childId", data.getId());
//                ChildFragment fragmentobj = new ChildFragment();
//                fragmentobj.setArguments(bundle);
//                FragmentManager manager = ((MainActivity)r.getContext()).getSupportFragmentManager();
//                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
                ChildMenuFragment fragmentobj = new ChildMenuFragment();
                fragmentobj.setArguments(bundle);
                FragmentManager manager = ((MainActivity)r.getContext()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
        });

        holder.cvRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialogPesan = new AlertDialog.Builder(holder.cvRoot.getContext());
                dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                dialogPesan.setTitle("Perhatian");
                dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                dialogPesan.setCancelable(true);

//                dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        try {
//                            deleteData(data.getDataChildren().getId(), holder.cvRoot.getContext());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        dialogInterface.dismiss();
//                        Handler hand = new Handler();
//                        hand.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                FragmentManager manager = ((MainActivity)v.getContext()).getSupportFragmentManager();
//                                manager.beginTransaction().replace(R.id.flHome, new ChildManagementFragment()).addToBackStack(null).commit();
//                            }
//                        }, 1000);
//                    }
//                });

                dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        getData(data.getDataChildren().getId(), ((MainActivity)v.getContext()));
                        dialogInterface.dismiss();
                    }
                });

                dialogPesan.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }

    public void insertDataList(List<DataChildren> inputData) {
        this.rvData.clear();
        this.rvData.addAll(inputData);
        notifyDataSetChanged();
    }

//    private void deleteData(Integer childId, Context ctxDelete) throws JSONException {
//        RequestBody body;
//        JSONStringer json = new JSONStringer();
//        json.object();
//        json.key("child_id").value(childId);
//        json.endObject();
//
//        body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
//        Call<ResponseDeleteChildren> deleteChildrenCall = endpoint.deleteChildren(body);
//
//        deleteChildrenCall.enqueue(new Callback<ResponseDeleteChildren>() {
//
//            @Override
//            public void onResponse(Call<ResponseDeleteChildren> call, Response<ResponseDeleteChildren> response) {
//                try {
//                    if (response.body().getData() != null) {
//                        Toast.makeText(ctxDelete, "Berhasil Menghapus Anak!", Toast.LENGTH_SHORT).show();
//                        Log.d("HAI", "onResponse: "+response.body().getData().getName());
//
//                    } else {
//                        Toast.makeText(ctxDelete, "Gagal", Toast.LENGTH_SHORT).show();
//                        Log.d("HAI", "onResponse: data kosong ");
//                    }
//                } catch (Exception e) {
//                    Log.d("HAI", "onResponse: data catch ");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseDeleteChildren> call, Throwable t) {
////                Toast.makeText(AddReviewActivity.this, "Gagal mengirim data"+t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("GAGAL", "onFailure: "+t.getMessage());
//            }
//        });
//    }

//    private void getData(Integer childId, Context ctxGetData){
//        endpoint.getChildren("by_id",childId).enqueue(new Callback<ResponseChildren>() {
//            @Override
//            public void onResponse(Call<ResponseChildren> call, Response<ResponseChildren> response) {
//                if (response.isSuccessful()) {
//                    String namaAnak = response.body().getChildrens().get(0).getDataChildren().getName();
//                    String dob = response.body().getChildrens().get(0).getDataChildren().getBornDate();
//                    Integer gender = response.body().getChildrens().get(0).getDataChildren().getGender();
//                    Boolean active = response.body().getChildrens().get(0).getDataChildren().getStatus();
//                    Integer parent = response.body().getChildrens().get(0).getDataChildren().getParent();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("childId", childId);
//                    bundle.putString("childName", namaAnak);
//                    bundle.putString("childDOB", dob);
//                    bundle.putInt("childGender", gender);
//                    bundle.putBoolean("childActive", active);
//                    bundle.putInt("childParent", parent);
//                    ChildEditFragment fragmentobj = new ChildEditFragment();
//                    fragmentobj.setArguments(bundle);
//                    FragmentManager manager = ((MainActivity)ctxGetData).getSupportFragmentManager();
//                    manager.beginTransaction().replace(R.id.flHome, fragmentobj).addToBackStack(null).commit();
////                    Toast.makeText(holder.cvRoot.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseChildren> call, Throwable t) {
//                Toast.makeText(ctxGetData, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
