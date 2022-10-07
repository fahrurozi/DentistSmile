package com.gemastik.dentistsmile.ui.review;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gemastik.dentistsmile.BuildConfig;
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.data.model.maps.DataPlace;
import com.gemastik.dentistsmile.data.model.maps.ResponseMaps;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiService;
import com.gemastik.dentistsmile.ui.review.detail.ReviewDetailActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Objects;

import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity implements LocationListener, ReviewInterface {
    private SharedPreferences sharedPref;
    private FusedLocationProviderClient mFusedLocationClient;
    private SpotsDialog spotsDialog;
    private SpotsDialog spotsDialogLocation;
    private Location location;
    private ReviewAdapter adapter;
    private LocationManager locationManager;

    private Integer lcoationPermissionCode = 2;

    private ApiEndpoint endpoint = ApiService.getRetrofitInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        sharedPref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");
        spotsDialogLocation = new SpotsDialog(this, "Get Location...");

        adapter = new ReviewAdapter(this);
        RecyclerView rvData = findViewById(R.id.rvTributeData);
        rvData.setAdapter(adapter);
        rvData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLocation();
//        Log.e("TESTT", "location: " + location.getLatitude() + " " + location.getLongitude());
    }


    private void searchLocation(String query) {
        try {
            spotsDialog.show();
            RequestBody body;
            JSONStringer json = new JSONStringer();
            json.object();
            json.key("get_type").value("registered_filter_users");
            json.key("location").value(location.getLatitude() + "," + location.getLongitude());
            json.key("keyword").value(query);
            json.key("radius").value(100000);
            json.endObject();
            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());

//            Call<ResponseMaps> mapsCall = endpoint.getMaps( body);
            Call<ResponseMaps> mapsCall = endpoint.getMaps( json.toString());
            mapsCall.enqueue(new Callback<ResponseMaps>() {

                @Override
                public void onResponse(Call<ResponseMaps> call, Response<ResponseMaps> response) {
                    spotsDialog.dismiss();
                    if (response.isSuccessful() && response.body().getPlaces() != null) {
                        if (response.body().getPlaces().size() == 0){
                            Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }else{
                            adapter.insertDataList(response.body().getPlaces());
                            Log.e("Data Lokasi", "onResponse: " + response.body().getPlaces().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMaps> call, Throwable t) {
                    spotsDialog.dismiss();
                    if (Objects.equals(t.getMessage(), "closed")) {
                        searchLocation(query);
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(DataPlace data) {
        Intent intent = new Intent(this, ReviewDetailActivity.class);
        Log.e("ISI", data.getPlaceDetail().getName())   ;
        Log.e("ISI", String.valueOf(data.getDbData().getId()))   ;
        intent.putExtra("place_id", data.getDbData().getId());
        intent.putExtra("desc", data.getDbData().getDesc());
        intent.putExtra("avg_rating", data.getDbData().getAvg_rating());
        intent.putExtra("place_name", data.getDbData().getPlace_name());
        intent.putExtra("address", data.getPlaceDetail().getFormattedAddress());
        intent.putExtra("photo_ref", data.getPlaceDetail().getPhotos().get(0).getPhotoReference());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLocationSettingsDialog();
    }

    private void showLocationSettingsDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
            } catch (ApiException exception) {
                if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        resolvable.startResolutionForResult(this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        spotsDialogLocation.show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, lcoationPermissionCode);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
        }
    }

    @SuppressLint("MissingPermission")
    private void setUserLocation() {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
        spotsDialogLocation.dismiss();
        locationManager.removeUpdates(this);
        Log.e("LOCATION", "location: "+location.getLatitude()+" "+location.getLongitude());
        setUserLocation();
        searchLocation("");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLocation();
    }


}
