package com.gemastik.dentistsmile.components.yolo.analysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;

//import com.example.yolov5tfliteandroid.detector.Yolov5TFLiteDetector;
import com.gemastik.dentistsmile.components.tflite.ColorLabel;
import com.gemastik.dentistsmile.components.tflite.ImageSegmentationHelper;
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector;
//import com.example.yolov5tfliteandroid.utils.ImageProcess;
import com.gemastik.dentistsmile.components.yolo.utils.ImageProcess;
//import com.example.yolov5tfliteandroid.utils.Recognition;
import com.gemastik.dentistsmile.components.yolo.utils.Recognition;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.vision.segmenter.ColoredLabel;
import org.tensorflow.lite.task.vision.segmenter.Segmentation;

public class FullImageAnalyse implements ImageAnalysis.Analyzer {

    public static class Result{

        public Result(long costTime, Bitmap bitmap) {
            this.costTime = costTime;
            this.bitmap = bitmap;
        }
        long costTime;
        Bitmap bitmap;
    }

    ImageView boxLabelCanvas;
    public static PreviewView previewView;
    int rotation;
    private TextView inferenceTimeTextView;
    private TextView frameSizeTextView;
    ImageProcess imageProcess;
    private Yolov5TFLiteDetector yolov5TFLiteDetector;

    public static Bitmap lastBitmapPhoto;
    Context context;

    public FullImageAnalyse(Context context,
                            PreviewView previewView,
                            ImageView boxLabelCanvas,
                            int rotation,
                            TextView inferenceTimeTextView,
                            TextView frameSizeTextView,
                            Yolov5TFLiteDetector yolov5TFLiteDetector) {
//        this.previewView = previewView;
        FullImageAnalyse.previewView = previewView;
        this.boxLabelCanvas = boxLabelCanvas;
        this.rotation = rotation;
        this.inferenceTimeTextView = inferenceTimeTextView;
        this.frameSizeTextView = frameSizeTextView;
        this.imageProcess = new ImageProcess();
        this.yolov5TFLiteDetector = yolov5TFLiteDetector;
        this.context = context;
    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
        ImageSegmentationHelper.SegmentationListener listener = new ImageSegmentationHelper.SegmentationListener() {
            @Override
            public void onResults(@Nullable List<? extends Segmentation> segmentResult, long inferenceTime, int imageHeight, int imageWidth) {
                Log.d("SegmentResult", "OnResult");
                if (segmentResult != null && (!segmentResult.isEmpty())) {
                    List<ColoredLabel> colorLabelsIndex = segmentResult.get(0).getColoredLabels();
                    List<ColorLabel> colorLabels = new ArrayList<>();
                    int index = 0;
                    for (ColoredLabel cl: colorLabelsIndex){
                        colorLabels.add(new ColorLabel(index, cl.getlabel(), cl.getArgb(), true));
                        index++;
                    }

                    TensorImage maskTensor = segmentResult.get(0).getMasks().get(0);
                    byte[] maskArray = maskTensor.getBuffer().array();
                    int[] pixels = new int[maskArray.length];
                    Map<Integer, Integer> pixelCounts = new HashMap<>();
                    for (int i = 0; i<maskArray.length; i++){
                        int pixelClassification = maskArray[i]; //?
                        if (!pixelCounts.containsKey(pixelClassification)){
                            pixelCounts.put(pixelClassification, 1);
                        }else{
                            pixelCounts.put(pixelClassification, pixelCounts.get(pixelClassification));
                        }

                        int index1 = ((int)maskArray[i]);
                        ColorLabel colorLabelTemp = colorLabels.get(index1);
                        colorLabelTemp.isExist = true;
                        colorLabels.set(index1, colorLabelTemp);
                        int color = colorLabelTemp.getColor();
                        pixels[i] = color;
                    }

                    Bitmap imageBitmap = Bitmap.createBitmap(pixels, maskTensor.getWidth(), maskTensor.getHeight(), Bitmap.Config.ARGB_8888);


                    int previewHeight = FullImageAnalyse.previewView.getHeight();
                    int previewWidth = FullImageAnalyse.previewView.getWidth();

                    // 这里Observable将image analyse的逻辑放到子线程计算, 渲染UI的时候再拿回来对应的数据, 避免前端UI卡顿
                    Observable.create( (ObservableEmitter<Result> emitter) -> {
                                long start = System.currentTimeMillis();

//                                byte[][] yuvBytes = new byte[3][];
//                                ImageProxy.PlaneProxy[] planes = image.getPlanes();
//                                int imageHeight = image.getHeight();
//                                int imageWidth = image.getWidth();
//
//                                imageProcess.fillBytes(planes, yuvBytes);
//                                int yRowStride = planes[0].getRowStride();
//                                final int uvRowStride = planes[1].getRowStride();
//                                final int uvPixelStride = planes[1].getPixelStride();
//
//                                int[] rgbBytes = new int[imageHeight * imageWidth];
//                                imageProcess.YUV420ToARGB8888(
//                                        yuvBytes[0],
//                                        yuvBytes[1],
//                                        yuvBytes[2],
//                                        imageWidth,
//                                        imageHeight,
//                                        yRowStride,
//                                        uvRowStride,
//                                        uvPixelStride,
//                                        rgbBytes);
//
//                                // 原图bitmap
//                                Bitmap imageBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
//                                imageBitmap.setPixels(rgbBytes, 0, imageWidth, 0, 0, imageWidth, imageHeight);

                                // 图片适应屏幕fill_start格式的bitmap
                                double scale = Math.max(
                                        previewHeight / (double) (rotation % 180 == 0 ? imageWidth : imageHeight),
                                        previewWidth / (double) (rotation % 180 == 0 ? imageHeight : imageWidth)
                                );

                                Matrix fullScreenTransform = imageProcess.getTransformationMatrix(
                                        imageWidth, imageHeight,
                                        (int) (scale * imageHeight), (int) (scale * imageWidth),
                                        rotation % 180 == 0 ? 90 : 0, false
                                );

                                // 适应preview的全尺寸bitmap
                                Bitmap fullImageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageWidth, imageHeight, fullScreenTransform, false);
                                // 裁剪出跟preview在屏幕上一样大小的bitmap
                                Bitmap cropImageBitmap = Bitmap.createBitmap(fullImageBitmap, 0, 0, previewWidth, previewHeight);

                                // 模型输入的bitmap
                                Matrix previewToModelTransform =
                                        imageProcess.getTransformationMatrix(
                                                cropImageBitmap.getWidth(), cropImageBitmap.getHeight(),
                                                yolov5TFLiteDetector.getInputSize().getWidth(),
                                                yolov5TFLiteDetector.getInputSize().getHeight(),
                                                0, false);
                                Bitmap modelInputBitmap = Bitmap.createBitmap(cropImageBitmap, 0, 0,
                                        cropImageBitmap.getWidth(), cropImageBitmap.getHeight(),
                                        previewToModelTransform, false);

                                Matrix modelToPreviewTransform = new Matrix();
                                previewToModelTransform.invert(modelToPreviewTransform);

                                ArrayList<Recognition> recognitions = yolov5TFLiteDetector.detect(modelInputBitmap);
//            ArrayList<Recognition> recognitions = yolov5TFLiteDetector.detect(imageBitmap);

                                Bitmap emptyCropSizeBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
                                Canvas cropCanvas = new Canvas(emptyCropSizeBitmap);
//            Paint white = new Paint();
//            white.setColor(Color.WHITE);
//            white.setStyle(Paint.Style.FILL);
//            cropCanvas.drawRect(new RectF(0,0,previewWidth, previewHeight), white);
                                // 边框画笔
                                Paint boxPaint = new Paint();
                                boxPaint.setStrokeWidth(5);
                                boxPaint.setStyle(Paint.Style.STROKE);
                                boxPaint.setColor(Color.GREEN);
                                // 字体画笔
                                Paint textPain = new Paint();
                                textPain.setTextSize(50);
                                textPain.setColor(Color.GREEN);
                                textPain.setStyle(Paint.Style.FILL);

                                for (Recognition res : recognitions) {
                                    RectF location = res.getLocation();
                                    String label = res.getLabelName();
                                    float confidence = res.getConfidence();
                                    modelToPreviewTransform.mapRect(location);
                                    cropCanvas.drawRect(location, boxPaint);
                                    cropCanvas.drawText(label + ":" + String.format("%.2f", confidence), location.left, location.top, textPain);
                                }
                                long end = System.currentTimeMillis();
                                long costTime = (end - start);
                                image.close();
                                emitter.onNext(new Result(costTime, emptyCropSizeBitmap));
//            emitter.onNext(new Result(costTime, imageBitmap));

                            }).subscribeOn(Schedulers.io()) // 这里定义被观察者,也就是上面代码的线程, 如果没定义就是主线程同步, 非异步
                            // 这里就是回到主线程, 观察者接受到emitter发送的数据进行处理
                            .observeOn(AndroidSchedulers.mainThread())
                            // 这里就是回到主线程处理子线程的回调数据.
                            .subscribe((Result result) -> {
                                boxLabelCanvas.setImageBitmap(result.bitmap);
                                frameSizeTextView.setText(previewHeight + "x" + previewWidth);
                                inferenceTimeTextView.setText(Long.toString(result.costTime) + "ms");
                                FullImageAnalyse.lastBitmapPhoto = result.bitmap;
                            });                    

                }
            }

            @Override
            public void onError(@NotNull String error) {
                Log.d("SegmentResult", "OnError");
            }
        };
        ImageSegmentationHelper ISH = new ImageSegmentationHelper(2, 0, context, listener);
        ISH.segment(toBitmap(image.getImage()), 0);



    }
}
