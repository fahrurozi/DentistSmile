package com.gemastik.dentistsmile.components.yolo.analysis

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers

class YoloAnalyse(
    val yolov5TFLiteDetector: Yolov5TFLiteDetector,
    val bitmapInput: Bitmap,
    val onResult: (bitmap: Bitmap) -> Unit
) {

    class Result(var bitmap: Bitmap)

    fun analyse() {

        Observable.create { emitter: ObservableEmitter<Result> ->
            val emptyCropSizeBitmap =
                Bitmap.createBitmap(bitmapInput.width, bitmapInput.height, Bitmap.Config.ARGB_8888)
            val cropCanvas = Canvas(emptyCropSizeBitmap)

            val boxPaint = Paint()
            boxPaint.strokeWidth = 5f
            boxPaint.style = Paint.Style.STROKE
            boxPaint.color = Color.GREEN

            val textPain = Paint()
            textPain.textSize = 50f
            textPain.color = Color.GREEN
            textPain.style = Paint.Style.FILL

            val bitmapARGB = bitmapInput.copy(Bitmap.Config.ARGB_8888, true)

            val recognitions = yolov5TFLiteDetector.detect(bitmapARGB)
            for (res in recognitions) {
                val location = res.location
                val label = res.labelName
                val confidence = res.confidence

                Log.e("BOPIT", location.toString())
                cropCanvas.drawRect(location, boxPaint)
                cropCanvas.drawText(
                    label + ":" + String.format("%.2f", confidence),
                    location.left,
                    location.top,
                    textPain
                )
            }
            emitter.onNext(Result(emptyCropSizeBitmap))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: Result ->
                onResult.invoke(result.bitmap)
            }
    }
}