package com.gemastik.dentistsmile.ui.capture_camera;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.yolo.analysis.FullImageAnalyse;
import com.gemastik.dentistsmile.components.yolo.analysis.FullScreenAnalyse;
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector;
import com.gemastik.dentistsmile.components.yolo.utils.CameraProcess;
import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;

public class CaptureCameraActivity extends AppCompatActivity implements ImageAnalysis.Analyzer, View.OnClickListener {

    public static final int PHOTO_REQUEST = 1;
    public static final int PHOTO_PICK_REQUEST = 2;
    public static final int TEXT_REQUEST = 3;

    private boolean IS_FULL_SCREEN = false;

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
        // 加载模型
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


        // 打开app的时候隐藏顶部状态栏
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // 全屏画面
        cameraPreviewMatch = findViewById(R.id.camera_preview_match);
        cameraPreviewMatch.setScaleType(PreviewView.ScaleType.FILL_START);

        // 全图画面
        cameraPreviewWrap = findViewById(R.id.camera_preview_wrap);
        cameraPreviewWrap.setScaleType(PreviewView.ScaleType.FILL_START);


        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

//         box/label画面
        boxLabelCanvas = findViewById(R.id.box_label_canvas);

        // 下拉按钮
        modelSpinner = findViewById(R.id.model);

        // 沉浸式体验按钮
        immersive = findViewById(R.id.immersive);

        // 实时更新的一些view
        inferenceTimeTextView = findViewById(R.id.inference_time);
        frameSizeTextView = findViewById(R.id.frame_size);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // 申请摄像头权限
        if (!cameraProcess.allPermissionsGranted(this)) {
            cameraProcess.requestPermissions(this);
        }

        // 获取手机摄像头拍照旋转参数
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.i("image", "rotation: " + rotation);

        cameraProcess.showCameraSupportSize(CaptureCameraActivity.this);

        // 初始化加载yolov5s
        initModel("yolov5s");

        // 监听模型切换按钮
//        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String model = (String) adapterView.getItemAtPosition(0);
        String model = "yolocustom";
        Toast.makeText(CaptureCameraActivity.this, "loading model: " + model, Toast.LENGTH_LONG).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCameraActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(CaptureCameraActivity.this).inflate(R.layout.custom_dialog_guide, viewGroup, false);

                ImageView btnClose = dialogView.findViewById(R.id.btnClose);

                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();
                btnClose.setOnClickListener(view -> alertDialog.dismiss());
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });




        Button ivRecordVideo = findViewById(R.id.ivRecordVideo);
        ivRecordVideo.setOnClickListener(this);
    }

    public void runCamera() {
        Log.d("TEST", "runCamera: " + frontCamera);
        if (IS_FULL_SCREEN) {
            cameraPreviewWrap.removeAllViews();

            FullScreenAnalyse fullScreenAnalyse = new FullScreenAnalyse(CaptureCameraActivity.this,
                    cameraPreviewMatch,
                    boxLabelCanvas,
                    rotation,
                    inferenceTimeTextView,
                    frameSizeTextView,
                    yolov5TFLiteDetector);
            if (!frontCamera) {
                cameraProcess.startCamera(CaptureCameraActivity.this, fullScreenAnalyse, cameraPreviewMatch);
            } else {
                cameraProcess.changeFrontCamera(CaptureCameraActivity.this, fullScreenAnalyse, cameraPreviewMatch);
            }
        } else {
            cameraPreviewMatch.removeAllViews();
            FullImageAnalyse fullImageAnalyse = new FullImageAnalyse(
                    CaptureCameraActivity.this,
                    cameraPreviewWrap,
                    boxLabelCanvas,
                    rotation,
                    inferenceTimeTextView,
                    frameSizeTextView,
                    yolov5TFLiteDetector);
//            cameraProcess.startCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            if (!frontCamera) {
                cameraProcess.startCamera(CaptureCameraActivity.this, fullImageAnalyse, cameraPreviewWrap);
            } else {
                cameraProcess.changeFrontCamera(CaptureCameraActivity.this, fullImageAnalyse, cameraPreviewWrap);
            }

        }
    }


    private void capturePhoto() {

        long timestamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Dentistsmile/");
//        if (!folder.exists()) {
//
//            folder.mkdirs();
//            if (!folder.exists()) {
//                folder.mkdir();
//            }
//        }
        try{
            folder.mkdirs();
        } catch (Exception e){

            try {
                folder.mkdir();
            } catch (Exception e1){

            }
        }
        File fileOri = new File(folder, "dentistsmile_" + timestamp + ".jpeg");
        File fileAi = new File(folder, "dentistsmile_" + timestamp + "_ai.jpeg");
        try {
            FileOutputStream fOutOri = new FileOutputStream(fileOri);
            FileOutputStream fOutAi = new FileOutputStream(fileAi);

            Bitmap oriResized = FullImageAnalyse.oriResized;
            Bitmap clfResult = FullImageAnalyse.clfResult;

            oriResized.compress(Bitmap.CompressFormat.JPEG, 50, fOutOri);
            clfResult.compress(Bitmap.CompressFormat.JPEG, 50, fOutAi);
//            get file uri
            Uri uriOri = Uri.fromFile(fileOri);
            Uri uriAi = Uri.fromFile(fileAi);
            Log.d("HAI", "capturePhoto: "+uriOri);
            fOutOri.flush();
            fOutOri.close();
            fOutAi.flush();
            fOutAi.close();

            Intent intent = new Intent();
            intent.putExtra("uriOri", uriOri.toString());
            intent.putExtra("uriAi", uriAi.toString());
            intent.putExtra("halo", "halo");
            intent.putExtra("result", Yolov5TFLiteDetector.teethProblemCounts.toString());
//            intent.setData(outputFileResults.getSavedUri());
            intent.setData(uriOri);
//            intent.setData(uriAi);
            Log.d("YoloRresult", Yolov5TFLiteDetector.teethProblemCounts.toString());
            setResult(RESULT_OK, intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        cameraProcess.imageCapture.takePicture(
//                new ImageCapture.OutputFileOptions.Builder(
//                        getContentResolver(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        contentValues
//                ).build(),
//                getExecutor(),
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        Intent intent = new Intent();
//                        intent.putExtra("image", outputFileResults.getSavedUri());
//                        intent.putExtra("halo", "halo");
//                        intent.setData(outputFileResults.getSavedUri());
//                        Log.d("YoloRresult", Yolov5TFLiteDetector.teethProblemCounts.toString());
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//
//
//                    @Override
//                    public void onError(@NonNull ImageCaptureException exception) {
//                        Toast.makeText(CaptureCameraActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
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
