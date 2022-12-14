package com.gemastik.dentistsmile.ui.test_yolo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gemastik.dentistsmile.R;

import com.gemastik.dentistsmile.components.yolo.analysis.FullImageAnalyse;
import com.gemastik.dentistsmile.components.yolo.analysis.FullScreenAnalyse;
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector;
import com.gemastik.dentistsmile.components.yolo.utils.CameraProcess;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.text.BreakIterator;
import java.util.Date;
import java.util.concurrent.Executor;

public class TestYolo extends AppCompatActivity implements ImageAnalysis.Analyzer, View.OnClickListener {

    private boolean IS_FULL_SCREEN = false;

    public static TextView warnVideo;
    private PreviewView cameraPreviewMatch;
    private PreviewView cameraPreviewWrap;
    private ImageView boxLabelCanvas;
    private int rotation;
    private Spinner modelSpinner;
    private Switch immersive;
    private TextView inferenceTimeTextView;
    private TextView frameSizeTextView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Yolov5TFLiteDetector yolov5TFLiteDetector;

    private CameraProcess cameraProcess = new CameraProcess();

    private boolean frontCamera = false;
    private Button btnRecordVideo;

    private ImageView btnInfo;


    private VideoCapture videoCapture;

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    private void initModel(String modelName) {
        // ????????????
        try {
            this.yolov5TFLiteDetector = new Yolov5TFLiteDetector();
            this.yolov5TFLiteDetector.setModelFile(modelName);
//            this.yolov5TFLiteDetector.addNNApiDelegate();
            this.yolov5TFLiteDetector.addGPUDelegate();
            this.yolov5TFLiteDetector.initialModel(this);
            Log.i("model", "Success loading model" + this.yolov5TFLiteDetector.getModelFile());
        } catch (Exception e) {
            Log.e("image", "load model error: " + e.getMessage() + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // ??????app??????????????????????????????
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // ????????????
        cameraPreviewMatch = findViewById(R.id.camera_preview_match);
        cameraPreviewMatch.setScaleType(PreviewView.ScaleType.FILL_START);

        // ????????????
        cameraPreviewWrap = findViewById(R.id.camera_preview_wrap);
        cameraPreviewWrap.setScaleType(PreviewView.ScaleType.FILL_START);


        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

//         box/label??????
        boxLabelCanvas = findViewById(R.id.box_label_canvas);

        warnVideo = findViewById(R.id.warnVideo);

        // ????????????
        modelSpinner = findViewById(R.id.model);

        // ?????????????????????
        immersive = findViewById(R.id.immersive);

        // ?????????????????????view
        inferenceTimeTextView = findViewById(R.id.inference_time);
        frameSizeTextView = findViewById(R.id.frame_size);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // ?????????????????????
        if (!cameraProcess.allPermissionsGranted(this)) {
            cameraProcess.requestPermissions(this);
        }

        // ???????????????????????????????????????
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.i("image", "rotation: " + rotation);

        cameraProcess.showCameraSupportSize(TestYolo.this);

        // ???????????????yolov5s
        initModel("yolov5s");

        // ????????????????????????
//        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String model = (String) adapterView.getItemAtPosition(0);
        String model = "yolocustom";
        Toast.makeText(TestYolo.this, "loading model: " + model, Toast.LENGTH_LONG).show();
        initModel(model);
        runCamera();

        ImageView btnChangeCamera = findViewById(R.id.btnChangeCamera);
        btnChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frontCamera = !frontCamera;
                runCamera();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(TestYolo.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(TestYolo.this).inflate(R.layout.custom_dialog_guide, viewGroup, false);

                ImageView btnClose = dialogView.findViewById(R.id.btnClose);

                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();
                btnClose.setOnClickListener(view -> alertDialog.dismiss());
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });


//        Button btnRecordVideo = findViewById(R.id.btnRecordVideo);
//        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onClick(View v) {
//                if (btnRecordVideo.getText() == "RECORD"){
//                    Toast.makeText(TestYolo.this, "Start Recording", Toast.LENGTH_LONG).show();
//                    btnRecordVideo.setText("stop recording");
//                    recordVideo();
//                }
//                else {
//                    btnRecordVideo.setText("RECORD");
//                    videoCapture.stopRecording();
//                }
//            }
//        });


        Button ivRecordVideo = findViewById(R.id.ivRecordVideo);
        ivRecordVideo.setOnClickListener(this);
    }

    public void runCamera() {
        Log.d("TEST", "runCamera: " + frontCamera);
//        if(!IS_FULL_SCREEN){
//            Log.d("TEST", "in if");
//            cameraPreviewWrap.removeAllViews();
//
//            FullScreenAnalyse fullScreenAnalyse = new FullScreenAnalyse(TestYolo.this,
//                    cameraPreviewMatch,
//                    boxLabelCanvas,
//                    rotation,
//                    inferenceTimeTextView,
//                    frameSizeTextView,
//                    yolov5TFLiteDetector);
//            if (!frontCamera) {
//                cameraProcess.startCamera(TestYolo.this, fullScreenAnalyse, cameraPreviewMatch);
//            } else {
//                cameraProcess.changeFrontCamera(TestYolo.this, fullScreenAnalyse, cameraPreviewMatch);
//            }
//        }else
        {
            cameraPreviewMatch.removeAllViews();
            FullImageAnalyse fullImageAnalyse = new FullImageAnalyse(
                    TestYolo.this,
                    cameraPreviewWrap,
                    boxLabelCanvas,
                    rotation,
                    inferenceTimeTextView,
                    frameSizeTextView,
                    yolov5TFLiteDetector);
//            cameraProcess.startCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            if (!frontCamera) {
                cameraProcess.startCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            } else {
                cameraProcess.changeFrontCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            }

        }
    }


    @SuppressLint("RestrictedApi")
    private void recordVideo() {
//            if (videoCapture != null) {
        Log.d("RECORD", "onClick: " + "Start Recording inside function recordVideo");

        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(
                            getContentResolver(),
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                    ).build(),
                    getExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            Toast.makeText(TestYolo.this, "Video has been saved successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("RECORD", "onVideoSaved: SUCCESS");
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            Toast.makeText(TestYolo.this, "Error saving video: " + message, Toast.LENGTH_SHORT).show();
                            Log.d("RECORD", "onVideoSaved: FAILED");
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

//            }
//            Log.d("RECORD" ,"onClick: "+"Start Recording skipped function recordVideo");
    }

    private void capturePhoto() {

        long timestamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

//        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Dentistsmile/");
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        File fileOri = new File(folder, "dentistsmile_" + timestamp + ".jpeg");
//        File fileAi = new File(folder, "dentistsmile_ai_" + timestamp + ".jpeg");
//        try {
//            FileOutputStream fOutOri = new FileOutputStream(fileOri);
//            FileOutputStream fOutAi = new FileOutputStream(fileAi);
//
////                            Log.d("HAI", "onImageSaved: "+fOut);
//
//            Bitmap oriResized = FullImageAnalyse.oriResized;
//            Bitmap clfResult = FullImageAnalyse.clfResult;
//
//            oriResized.compress(Bitmap.CompressFormat.JPEG, 100, fOutOri);
//            clfResult.compress(Bitmap.CompressFormat.JPEG, 100, fOutAi);
////            get file uri
//            Uri uriOri = Uri.fromFile(fileOri);
//            Log.d("HAI", "capturePhoto: "+uriOri);
//            fOutOri.flush();
//            fOutOri.close();
//            fOutAi.flush();
//            fOutAi.close();
//            Toast.makeText(TestYolo.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(TestYolo.this, "Error saving photo.", Toast.LENGTH_SHORT).show();
//        }

        cameraProcess.imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(TestYolo.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(TestYolo.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        );


    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRecordVideo:
                Log.d("TESTT", "Press");
                capturePhoto();
                Log.d("YoloRresult", Yolov5TFLiteDetector.teethProblemCounts.toString());
                break;
        }
//        switch (view.getId()) {
//            case R.id.btnRecordVideo:
//                if (btnRecordVideo.getText() == "RECORD"){
//                    Log.d("RECORD" ,"onClick: "+"Start Recording");
//                    btnRecordVideo.setText("stop recording");
//                    recordVideo();
//                } else {
//                    Log.d("RECORD" ,"onClick: "+"STOP Recording");
//                    btnRecordVideo.setText("RECORD");
//                    videoCapture.stopRecording();
//                }
//                break;
//
//        }
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
// image processing here for the current frame
        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
        image.close();
    }


}
