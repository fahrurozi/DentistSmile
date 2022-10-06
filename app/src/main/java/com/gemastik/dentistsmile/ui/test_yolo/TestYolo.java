package com.gemastik.dentistsmile.ui.test_yolo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
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

public class TestYolo extends AppCompatActivity {

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
        setContentView(R.layout.activity_test_yolo);

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

        cameraProcess.showCameraSupportSize(TestYolo.this);

        // 初始化加载yolov5s
        initModel("yolov5s");

        // 监听模型切换按钮
//        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String model = (String) adapterView.getItemAtPosition(0);
        String model = "yolocustom";
                Toast.makeText(TestYolo.this, "loading model: " + model, Toast.LENGTH_LONG).show();
                initModel(model);
                runCamera();

        Button btnChangeCamera = findViewById(R.id.btnChangeCamera);
        btnChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frontCamera = !frontCamera;
                runCamera();
            }
        });
    }

    public void runCamera() {
        Log.d("TEST", "runCamera: "+frontCamera);
        if(IS_FULL_SCREEN){
            cameraPreviewWrap.removeAllViews();

            FullScreenAnalyse fullScreenAnalyse = new FullScreenAnalyse(TestYolo.this,
                    cameraPreviewMatch,
                    boxLabelCanvas,
                    rotation,
                    inferenceTimeTextView,
                    frameSizeTextView,
                    yolov5TFLiteDetector);
            if(!frontCamera) {
                cameraProcess.startCamera(TestYolo.this, fullScreenAnalyse, cameraPreviewMatch);
            }else{
                cameraProcess.changeFrontCamera(TestYolo.this, fullScreenAnalyse, cameraPreviewMatch);
            }
        }else{
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
            if(!frontCamera) {
                cameraProcess.startCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            }else{
                cameraProcess.changeFrontCamera(TestYolo.this, fullImageAnalyse, cameraPreviewWrap);
            }

        }
    }


}
