package com.gemastik.dentistsmile.ui.medical_checkup.dentist_checkup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.view.ImageFilePath;
import com.gemastik.dentistsmile.data.model.checkup_dentist.ResponseCheckupDentist;
import com.gemastik.dentistsmile.data.model.checkup_physic.ResponseCheckupPhysic;
import com.gemastik.dentistsmile.data.model.kecamatan.DataKecamatan;
import com.gemastik.dentistsmile.data.model.kecamatan.ResponseGetKecamatanAll;
import com.gemastik.dentistsmile.data.model.kelas.DataKelas;
import com.gemastik.dentistsmile.data.model.kelas.ResponseGetKelasByIdSek;
import com.gemastik.dentistsmile.data.model.kelurahan.DataKelurahan;
import com.gemastik.dentistsmile.data.model.kelurahan.ResponseGetKelurahanByIdKec;
import com.gemastik.dentistsmile.data.model.sekolah.DataSekolah;
import com.gemastik.dentistsmile.data.model.sekolah.ResponseGetSekolahByIdKel;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.data.network.ApiServiceDentist;
import com.gemastik.dentistsmile.databinding.ActivityCheckupDentistBinding;
import com.gemastik.dentistsmile.ui.medical_checkup.physical_checkup.PhysicalCheckupActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class DentistCheckupActivity extends AppCompatActivity {


    TextView imgPath;
    private static final int PICK_IMAGE_REQUEST = 9544;
//    private ImageView image, imgFront, imgRight, imgLeft, imgTop, imgBottom;
    Uri selectedImage;
    String part_image;
//    private TextView item_img_1, item_img_2, item_img_3, item_img_4, item_img_5;
    public File fGambar1, fGambar2, fGambar3, fGambar4, fGambar5;
//    public File fGambar1, fGambar2, fGambar3, fGambar4, fGambar5;
    public RequestBody partGambar1, partGambar2, partGambar3, partGambar4, partGambar5;
    Integer statePhoto;
    public String childName, childId;
    ActivityCheckupDentistBinding binding;

    LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();

    public String inputIdKecamatan, inputIdKelurahan, inputIdSekolah, inputIdKelas;
    private Integer id_kecamatan, id_kelurahan, id_sekolah, id_kelas;
    private String selected_kecamatan, selected_kelurahan, selected_sekolah, selected_kelas;


    private SpotsDialog spotsDialog;

    private HashMap<String, Integer> hashKecamatan = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashKelurahan = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashSekolah = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashKelas = new HashMap<String, Integer>();

    private ApiEndpoint endpoint = ApiServiceDentist.getRetrofitInstance();


    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckupDentistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");
        spotsDialog.show();
        getKecamatan();


        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        childName = getIntent().getStringExtra("childName");
        childId = getIntent().getStringExtra("childId");
        binding.tvChildName.setText(childName);



        binding.itemImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePhoto = 0;
                pickImage(statePhoto);
            }
        });

        binding.itemImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePhoto = 1;
                pickImage(statePhoto);
            }
        });

        binding.itemImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePhoto = 2;
                pickImage(statePhoto);
            }
        });

        binding.itemImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePhoto = 3;
                pickImage(statePhoto);
            }
        });

        binding.itemImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePhoto = 4;
                pickImage(statePhoto);
            }
        });

        binding.fabSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner drop_gsoal1 = (Spinner) findViewById(R.id.spinnerGsoal1);
                Spinner drop_gsoal2 = (Spinner) findViewById(R.id.spinnerGsoal2);


                map.put("id_anak", childId);
                map.put("id_sekolah", inputIdSekolah);
                map.put("id_kelas", inputIdKelas);

                map.put("gsoal1", drop_gsoal1.getSelectedItem().toString());
                map.put("gsoal2", drop_gsoal2.getSelectedItem().toString());

                if (fGambar1 != null && fGambar2 != null && fGambar3 != null && fGambar4 != null && fGambar5 != null) {
                    if (childId == null || inputIdSekolah == null || inputIdKelas == null || drop_gsoal1.getSelectedItem().toString() == null || drop_gsoal2.getSelectedItem().toString() == null) {
                        Toast.makeText(DentistCheckupActivity.this, "Mohon lengkapi data", Toast.LENGTH_SHORT).show();
                    } else {
                        spotsDialog.show();
                        postDentistCheckup();
                    }

                } else {
                    Toast.makeText(DentistCheckupActivity.this, "Mohon lengkapi semua foto", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    class KecamatanSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kecamatan = item;
            id_kecamatan = hashKecamatan.get(selected_kecamatan);
            inputIdKecamatan = id_kecamatan.toString();
            // Showing selected spinner item
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            getKelurahan(id_kecamatan);

//            Toast.makeText(v.getContext(), "Your choose :" ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class KelurahanSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kelurahan = item;
            id_kelurahan = hashKelurahan.get(selected_kelurahan);
            inputIdKelurahan = id_kelurahan.toString();

            getSekolah(id_kelurahan);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class SekolahSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_sekolah = item;
            id_sekolah = hashSekolah.get(selected_sekolah);
            inputIdSekolah = id_sekolah.toString();
            getKelas(id_sekolah);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class KelasSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();

            selected_kelas = item;
            id_kelas = hashKelas.get(selected_kelas);
            inputIdKelas = id_kelas.toString();
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void setSpinner(String name, HashMap<String, Integer> hash, Spinner spinner) {
        Set<String> keySetList = hash.keySet();
        ArrayList<String> listName = new ArrayList<String>(keySetList);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listName);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (name.equals("kecamatan")) {
            binding.spinnerKecamatan.setAdapter(dataAdapter);
            binding.spinnerKecamatan.setOnItemSelectedListener(new KecamatanSpinnerClass());
        } else if (name.equals("kelurahan")) {
            binding.spinnerKelurahan.setAdapter(dataAdapter);
            binding.spinnerKelurahan.setOnItemSelectedListener(new KelurahanSpinnerClass());
        } else if (name.equals("sekolah")) {
            binding.spinnerSekolah.setAdapter(dataAdapter);
            binding.spinnerSekolah.setOnItemSelectedListener(new SekolahSpinnerClass());
        } else if (name.equals("kelas")) {
            binding.spinnerKelas.setAdapter(dataAdapter);
            binding.spinnerKelas.setOnItemSelectedListener(new KelasSpinnerClass());
        }
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick1(View view) {
        verifyStoragePermissions(DentistCheckupActivity.this);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);
    }

    public void pickImage(int status) {
        statePhoto = status;
        verifyStoragePermissions(DentistCheckupActivity.this);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("TAG", "onActivityResult: "+statePhoto);
//            switch (statePhoto) {
//                case 0:
//                    setPhoto(binding.imgFront, data.getData());
////                    fGambar1 = new File(data.getData().getPath());
////                    String asdad = ImageFilePath.getPath(getApplicationContext(), data.getData());
//                    fGambar1 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
////                    Bitmap myBitmap = BitmapFactory.decodeFile(fGambar1.getAbsolutePath());
////
////                    binding.imgBottom.setVisibility(View.VISIBLE);
////                    binding.imgBottom.setImageBitmap(myBitmap);
////                    Log.d("TAG", "asdasd: "+asdad);
////                    if(testst.exists()){
////                        Log.d("TAG", "onActivityResult: "+fGambar1.getPath());
////                    }else{
////                        Log.d("TAG", "image doesnt exist");
////                    }
////                    break;
//                case 1:
//                    setPhoto(binding.imgRight, data.getData());
//                    Log.d("TEST" ,"onActivityResult: halo "+statePhoto);
////                    fGambar2 = new File(data.getData().getPath());
//                    fGambar2 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
//                    break;
//                case 2:
//                    setPhoto(binding.imgLeft, data.getData());
////                    fGambar3 = new File(data.getData().getPath());
//                    fGambar3 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
//                    break;
//                case 3:
//                    setPhoto(binding.imgTop, data.getData());
////                    fGambar4 = new File(data.getData().getPath());
//                    fGambar4 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
//                    break;
//                case 4:
//                    setPhoto(binding.imgBottom, data.getData());
////                    fGambar5 = new File(data.getData().getPath());
//                    fGambar5 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
//                    break;
//            }

            if (statePhoto == 0) {
                setPhoto(binding.imgFront, data.getData());
                fGambar1 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
            } else if (statePhoto == 1) {
                setPhoto(binding.imgRight, data.getData());
                fGambar2 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
            }else if (statePhoto == 2) {
                setPhoto(binding.imgLeft, data.getData());
                fGambar3 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
            }else if(statePhoto == 3){
                setPhoto(binding.imgTop, data.getData());
                fGambar4 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
            }else if(statePhoto==4){
                setPhoto(binding.imgBottom, data.getData());
                fGambar5 = new File(ImageFilePath.getPath(getApplicationContext(), data.getData()));
            }
        }
    }

    void setPhoto(ImageView imageView, Uri uri ) {
        imageView.setImageURI(uri);
        imageView.setVisibility(View.VISIBLE);
    }

    private void getKecamatan(){
        try {
            Call<ResponseGetKecamatanAll> kecamatanCall = endpoint.getKecamatanAll();
            kecamatanCall.enqueue(new retrofit2.Callback<ResponseGetKecamatanAll>() {
                @Override
                public void onResponse(Call<ResponseGetKecamatanAll> call, retrofit2.Response<ResponseGetKecamatanAll> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                List<DataKecamatan> rawKecamatan = response.body().getData();
                                for(int i = 0; i<rawKecamatan.size(); i++){
                                    hashKecamatan.put(rawKecamatan.get(i).getNama(), rawKecamatan.get(i).getId());
//                                    Log.d("Value Kecamtan", rawKecamatan.get(0).getNama());
                                }
                                setSpinner("kecamatan", hashKecamatan, binding.spinnerKecamatan);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKecamatanAll> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void getKelurahan(Integer id_kecamatan){
        try {
            Call<ResponseGetKelurahanByIdKec> kelurahanCall = endpoint.getKelurahanByIdKec(
                    id_kecamatan
            );
            kelurahanCall.enqueue(new retrofit2.Callback<ResponseGetKelurahanByIdKec>() {
                @Override
                public void onResponse(Call<ResponseGetKelurahanByIdKec> call, retrofit2.Response<ResponseGetKelurahanByIdKec> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessage().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashKelurahan.clear();
                                List<DataKelurahan> rawKelurahan = response.body().getData();
                                for(int i = 0; i<rawKelurahan.size(); i++){
                                    hashKelurahan.put(rawKelurahan.get(i).getNama(), rawKelurahan.get(i).getId());
                                }
                                setSpinner("kelurahan", hashKelurahan, binding.spinnerKelurahan);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKelurahanByIdKec> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void getSekolah(Integer id_kelurahan){
        try {
            Call<ResponseGetSekolahByIdKel> sekolahCall = endpoint.getSekolahByIdKel(
                    id_kelurahan
            );
            sekolahCall.enqueue(new retrofit2.Callback<ResponseGetSekolahByIdKel>() {
                @Override
                public void onResponse(Call<ResponseGetSekolahByIdKel> call, retrofit2.Response<ResponseGetSekolahByIdKel> response) {
                    try {
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashSekolah.clear();
                                List<DataSekolah> rawSekolah = response.body().getData();
                                for(int i = 0; i<rawSekolah.size(); i++){
                                    hashSekolah.put(rawSekolah.get(i).getNama(), rawSekolah.get(i).getId());
                                    Log.d("TEST", "onResponse: "+rawSekolah.get(i).getNama());
                                }
                                setSpinner("sekolah", hashSekolah, binding.spinnerSekolah);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetSekolahByIdKel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void getKelas(Integer id_sekolah){
        try {
            Call<ResponseGetKelasByIdSek> kelasCall = endpoint.getKelasByIdSek(
                    id_sekolah
            );
            kelasCall.enqueue(new retrofit2.Callback<ResponseGetKelasByIdSek>() {
                @Override
                public void onResponse(Call<ResponseGetKelasByIdSek> call, retrofit2.Response<ResponseGetKelasByIdSek> response) {
                    try {
                        Log.d("TEST", "onResponse kel: "+response.body().getMessages());
                        spotsDialog.dismiss();
                        if (response.body().getMessages().equals("Success")) {
                            if(!response.body().getData().isEmpty()){
                                hashKelas.clear();
                                List<DataKelas> rawKelas = response.body().getData();
                                for(int i = 0; i<rawKelas.size(); i++){
                                    hashKelas.put(rawKelas.get(i).getKelas(), rawKelas.get(i).getId());
                                    Log.d("TEST", "onResponse: "+rawKelas.get(i).getKelas());
                                }
                                setSpinner("kelas", hashKelas, binding.spinnerKelas);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onResponse kel: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetKelasByIdSek> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }

    private void postDentistCheckup(){
        try {
            Call<ResponseCheckupDentist> addPhysicalCheckupCall = endpoint.addDentistCheckup(
//                    map,
                    RequestBody.create(okhttp3.MultipartBody.FORM,  map.get("id_anak")),
                    RequestBody.create(okhttp3.MultipartBody.FORM, map.get("id_sekolah")),
                    RequestBody.create(okhttp3.MultipartBody.FORM, map.get("id_kelas")),

                    MultipartBody.Part.createFormData("gambar1", fGambar1.getName(), RequestBody.create(MediaType.parse("image/*"),
                            fGambar1)),
                    MultipartBody.Part.createFormData("gambar2", fGambar2.getName(), RequestBody.create(MediaType.parse("image/*"),
                            fGambar2)),
                    MultipartBody.Part.createFormData("gambar3", fGambar3.getName(), RequestBody.create(MediaType.parse("image/*"),
                            fGambar3)),
                    MultipartBody.Part.createFormData("gambar4", fGambar4.getName(), RequestBody.create(MediaType.parse("image/*"),
                            fGambar4)),
                    MultipartBody.Part.createFormData("gambar5", fGambar5.getName(), RequestBody.create(MediaType.parse("image/*"),
                            fGambar5)),

                    RequestBody.create(okhttp3.MultipartBody.FORM,  map.get("gsoal1")),
                    RequestBody.create(okhttp3.MultipartBody.FORM,  map.get("gsoal2"))
//                    RequestBody.create(MediaType.parse("image/*"),
//                            fGambar1),
//                    RequestBody.create(MediaType.parse("image/*"),
//                            fGambar2),
//                    RequestBody.create(MediaType.parse("image/*"),
//                            fGambar3),
//                    RequestBody.create(MediaType.parse("image/*"),
//                            fGambar4),
//                    RequestBody.create(MediaType.parse("image/*"),
//                            fGambar5)
//                    fGambar1,
//                    fGambar2,
//                    fGambar3,
//                    fGambar4,
//                    fGambar5
            );
            addPhysicalCheckupCall.enqueue(new retrofit2.Callback<ResponseCheckupDentist>() {
                @Override
                public void onResponse(Call<ResponseCheckupDentist> call, Response<ResponseCheckupDentist> response) {
                    try {
                        spotsDialog.dismiss();
                        if(response.body().getMessages().equals("success")){

                            Toast.makeText(getApplicationContext(), "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "onFailure: "+e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseCheckupDentist> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Gagal mengirim data!", Toast.LENGTH_SHORT).show();
                    Log.d("TEST", "onFailure: "+t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Gagal", e.getMessage());
        }
    }
}
