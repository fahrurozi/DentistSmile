package com.gemastik.dentistsmile.components.yolo.analysis;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.gemastik.dentistsmile.R;
import com.gemastik.dentistsmile.components.imagefilter.ImageFilter;
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

import com.gemastik.dentistsmile.ui.test_yolo.TestYolo;
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

        public Result(long costTime, Bitmap bitmap, String message) {
            this.costTime = costTime;
            this.bitmap = bitmap;
            this.message = message;
        }
        long costTime;
        Bitmap bitmap;
        String message;
    }

    ImageView boxLabelCanvas;
    public static PreviewView previewView;
    int rotation;
    private TextView inferenceTimeTextView;
    private TextView frameSizeTextView;
    ImageProcess imageProcess;
    private Yolov5TFLiteDetector yolov5TFLiteDetector;

    public static Bitmap lastBitmapPhoto;

    public static Image lastImage;
    Context context;

    public static Bitmap segmentationResult, oriResized, clfResult;

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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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

    public Bitmap rotateBitmap(Bitmap bitmap){
        Matrix matrix = new Matrix();

        matrix.postRotate(-90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }
    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
        TextView warnVideo = ((Activity) context).findViewById(R.id.warnVideo1);
        FullImageAnalyse.lastImage = image.getImage();
        ImageSegmentationHelper.SegmentationListener listener = new ImageSegmentationHelper.SegmentationListener() {
            @Override
            public void onResults(@Nullable List<? extends Segmentation> segmentResult, long inferenceTime, int imageHeight, int imagewWidth) {
                ImageFilter.ImageFilterResult filterResult = ImageFilter.getImageStatus(FullImageAnalyse.lastBitmapPhoto);
                Log.d("filter", filterResult.brightness + " " + filterResult.contrast);

                Log.d("SegmentResult", "OnResult");
                if (segmentResult != null && (!segmentResult.isEmpty())) {

                    if (true){
                    /* SEGMENTATION PROCESS */
                    Log.d("SegmentResult", "process");
                    List<ColoredLabel> colorLabelsIndex = segmentResult.get(0).getColoredLabels();
                    List<ColorLabel> colorLabels = new ArrayList<>();
                    int index = 0;
                    for (ColoredLabel cl : colorLabelsIndex) {
                        colorLabels.add(new ColorLabel(index, cl.getlabel(), cl.getArgb(), true));
                        index++;
                    }

                    TensorImage maskTensor = segmentResult.get(0).getMasks().get(0);
                    byte[] maskArray = maskTensor.getBuffer().array();
                    int[] pixels = new int[maskArray.length];
                    Map<Integer, Integer> pixelCounts = new HashMap<>();
                    for (int i = 0; i < maskArray.length; i++) {
                        int pixelClassification = maskArray[i]; //?
                        if (!pixelCounts.containsKey(pixelClassification)) {
                            pixelCounts.put(pixelClassification, 1);
                        } else {
                            pixelCounts.put(pixelClassification, pixelCounts.get(pixelClassification));
                        }

                        int index1 = ((int) maskArray[i]);
                        ColorLabel colorLabelTemp = colorLabels.get(index1);
                        colorLabelTemp.isExist = true;
                        colorLabels.set(index1, colorLabelTemp);
                        int color = colorLabelTemp.getColor();
                        pixels[i] = color;
                    }

                    Bitmap imageBitmapSegmentation = rotateBitmap(getResizedBitmap(Bitmap.createBitmap(pixels, maskTensor.getWidth(), maskTensor.getHeight(), Bitmap.Config.ARGB_8888), previewView.getWidth(), previewView.getHeight()));

                    FullImageAnalyse.segmentationResult = imageBitmapSegmentation;
                    Bitmap imageBitmap = FullImageAnalyse.lastBitmapPhoto;

                    // 0 = mouth, 1 = background, 2=border.
                    double borderCount = pixelCounts.containsKey(2) ? pixelCounts.get(2) : 0;
                    double objectCount = pixelCounts.containsKey(0) ? pixelCounts.get(0) : 0;
                    double backgroundCount = pixelCounts.containsKey(2) ? pixelCounts.get(2) : 0;
                    double scaleDown = 4.5;
                    double mouthPercentage = (borderCount + objectCount) / (borderCount + objectCount + backgroundCount);
                    //                    var mouthPercentage = ((((borderCount!!)+(objectCount!!)).toDouble())/(borderCount+objectCount+backgroundCount!!).toDouble()).toDouble()
                    Log.d("PixelCounts and percentage", pixelCounts.toString() + " - " + mouthPercentage);


                    /* YOLO PROCESS */
                    int previewHeight = previewView.getHeight();
                    int previewWidth = previewView.getWidth();
                    //                    int imageHeight1 = FullImageAnalyse.lastImage.getHeight();
                    //                    int imagewWidth1 = FullImageAnalyse.lastImage.getWidth();
                    int imageHeight1 = FullImageAnalyse.lastBitmapPhoto.getHeight();//imageBitmap.getHeight();
                    int imagewWidth1 = FullImageAnalyse.lastBitmapPhoto.getWidth();//imageBitmap.getWidth();
                    Observable.create((ObservableEmitter<Result> emitter) -> {

                                //                    Bitmap imageBitmap = imageBitmap0;


                                // ??????Observable???image analyse??????????????????????????????, ??????UI????????????????????????????????????, ????????????UI??????
                                long start = System.currentTimeMillis();

                                // ??????????????????fill_start?????????bitmap
                                double scale = Math.max(
                                        previewHeight / (double) (rotation % 180 == 0 ? imagewWidth1 : imageHeight1),
                                        previewWidth / (double) (rotation % 180 == 0 ? imageHeight1 : imagewWidth1)
                                );
                                Matrix fullScreenTransform = imageProcess.getTransformationMatrix(
                                        imagewWidth1, imageHeight1,
                                        (int) (scale * imageHeight1), (int) (scale * imagewWidth1),
                                        rotation % 180 == 0 ? 90 : 0, false
                                );

                                // ??????preview????????????bitmap
                                Bitmap fullImageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imagewWidth1, imageHeight1, fullScreenTransform, false);
                                // ????????????preview???????????????????????????bitmap
                                Bitmap cropImageBitmap = Bitmap.createBitmap(fullImageBitmap, 0, 0, previewWidth, previewHeight);
                                FullImageAnalyse.oriResized = cropImageBitmap;
                                // ???????????????bitmap
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

                                Bitmap segmentCropBitmap = getResizedBitmap(imageBitmapSegmentation, previewWidth, previewHeight);//Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
                                Bitmap segmentCropBitmap1 = oriResized.copy(oriResized.getConfig(), true);
                                //                    Bitmap emptyCrop = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
                                Canvas cropCanvas = new Canvas(segmentCropBitmap);
                                Canvas cropCanvas1 = new Canvas(segmentCropBitmap1);

                                //                    Canvas cropCanvas1 = new Canvas(emptyCrop);
                                //                    Canvas cropCanvas = new Canvas(getResizedBitmap(imageBitmapSegmentation, previewWidth, previewHeight));
                                //            Paint white = new Paint();
                                //            white.setColor(Color.WHITE);
                                //            white.setStyle(Paint.Style.FILL);
                                //            cropCanvas.drawRect(new RectF(0,0,previewWidth, previewHeight), white);
                                // ????????????
                                Paint boxPaint = new Paint();
                                boxPaint.setStrokeWidth(5);
                                boxPaint.setStyle(Paint.Style.STROKE);
                                boxPaint.setColor(Color.GREEN);
                                // ????????????
                                Paint textPain = new Paint();
                                textPain.setTextSize(50);
                                textPain.setColor(Color.GREEN);
                                textPain.setStyle(Paint.Style.FILL);
                                Log.d("CLFResult", "" + recognitions.size());
                                for (Recognition res : recognitions) {
                                    RectF location = res.getLocation();
                                    String label = res.getLabelName();
                                    float confidence = res.getConfidence();
                                    modelToPreviewTransform.mapRect(location);
                                    cropCanvas.drawRect(location, boxPaint);
                                    cropCanvas1.drawRect(location, boxPaint);
                                    //                        cropCanvas1.drawBitmap(segmentCropBitmap1, location.left, location.top, boxPaint);


                                    //                        cropCanvas1.drawRect(location, boxPaint);
                                    //                        cropCanvas.drawText(label + ":" + String.format("%.2f", confidence), location.left, location.top, textPain);

                                    cropCanvas.drawText(label, location.left, location.top, textPain);
                                    cropCanvas1.drawText(label, location.left, location.top, textPain);
                                    //                        cropCanvas1.drawBitmap(segmentCropBitmap1, location.left, location.top, textPain);
                                    //                        cropCanvas1.drawText(label, location.left, location.top, textPain);
                                }

                                long end = System.currentTimeMillis();
                                long costTime = (end - start);

                                FullImageAnalyse.clfResult = segmentCropBitmap1;


                                image.close();

                                Log.d("ClfResult", "Done");
                                emitter.onNext(new Result(costTime, segmentCropBitmap, filterResult.message));

                                //            emitter.onNext(new Result(costTime, imageBitmap));

                            }).subscribeOn(Schedulers.io()) // ????????????????????????,??????????????????????????????, ????????????????????????????????????, ?????????
                            // ???????????????????????????, ??????????????????emitter???????????????????????????
                            .observeOn(AndroidSchedulers.mainThread())
                            // ?????????????????????????????????????????????????????????.
                            .subscribe((Result result) -> {
                                Bitmap clfResult = result.bitmap;
                                //                            for (int j=0; j < clfResult.getHeight(); j++){
                                //                                for (int i=0; i<clfResult.getWidth(); i++){
                                //                                    if (imageBitmapSegmentation.getPixel(i, j)<2){
                                //                                        clfResult.setPixel(i, j, imageBitmapSegmentation.getPixel(i, j));
                                //                                    }
                                //
                                //                                }
                                //                            }
                                warnVideo.setText(result.message);
                                if (filterResult.pass){
                                    boxLabelCanvas.setImageBitmap(clfResult);
                                }else{
                                    boxLabelCanvas.setImageBitmap(FullImageAnalyse.oriResized);
                                }

                                //                            boxLabelCanvas.setImageBitmap(imageBitmapSegmentation);
                                frameSizeTextView.setText(previewHeight + "x" + previewWidth);
                                inferenceTimeTextView.setText(Long.toString(result.costTime) + "ms");
                            });
                     }
                    else{
                        Observable.create((ObservableEmitter<Result> emitter) -> {
                                    int previewHeight = previewView.getHeight();
                                    int previewWidth = previewView.getWidth();
                                Bitmap imageOriResized = getResizedBitmap(FullImageAnalyse.lastBitmapPhoto, previewWidth, previewHeight);
                                    emitter.onNext(new Result(0, imageOriResized, "FAIL"));
                                }).subscribeOn(Schedulers.io()) // ????????????????????????,??????????????????????????????, ????????????????????????????????????, ?????????
                                // ???????????????????????????, ??????????????????emitter???????????????????????????
                                .observeOn(AndroidSchedulers.mainThread())
                                // ?????????????????????????????????????????????????????????.
                                .subscribe((Result result) -> {
                                    int previewHeight = previewView.getHeight();
                                    int previewWidth = previewView.getWidth();
                                    boxLabelCanvas.setImageBitmap(result.bitmap);
                                    //                            boxLabelCanvas.setImageBitmap(imageBitmapSegmentation);
                                    frameSizeTextView.setText(previewHeight + "x" + previewWidth);
                                    inferenceTimeTextView.setText(Long.toString(result.costTime) + "ms");
                                });
                    }
                }
                else{
                    Log.d("SegmentResult", "NOT process");
                }
            }

            @Override
            public void onError(@NotNull String error) {
                Log.d("SegmentResult", "OnError");
            }
        };


                    int imageHeight = image.getHeight();
                    int imagewWidth = image.getWidth();
//                    int imagewWidth = segmentationResult.getWidth();
//                    int imageHeight = segmentationResult.getHeight();

                    byte[][] yuvBytes = new byte[3][];
                    ImageProxy.PlaneProxy[] planes = image.getPlanes();
                    imageProcess.fillBytes(planes, yuvBytes);
                    int yRowStride = planes[0].getRowStride();
                    final int uvRowStride = planes[1].getRowStride();
                    final int uvPixelStride = planes[1].getPixelStride();

                    int[] rgbBytes = new int[imageHeight * imagewWidth];
                    imageProcess.YUV420ToARGB8888(
                            yuvBytes[0],
                            yuvBytes[1],
                            yuvBytes[2],
                            imagewWidth,
                            imageHeight,
                            yRowStride,
                            uvRowStride,
                            uvPixelStride,
                            rgbBytes);

                    // ??????bitmap
                    Bitmap imageBitmap0 = Bitmap.createBitmap(imagewWidth, imageHeight, Bitmap.Config.ARGB_8888);
                    imageBitmap0.setPixels(rgbBytes, 0, imagewWidth, 0, 0, imagewWidth, imageHeight);
                    FullImageAnalyse.lastBitmapPhoto = imageBitmap0;

                    ImageSegmentationHelper ISH = new ImageSegmentationHelper(2, 0, context, listener);
                    ISH.segment(imageBitmap0, 0);



    }
}
