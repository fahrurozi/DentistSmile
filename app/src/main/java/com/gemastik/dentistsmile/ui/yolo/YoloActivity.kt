package com.gemastik.dentistsmile.ui.yolo

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.concurrent.futures.await
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.gemastik.dentistsmile.components.yolo.analysis.FullScreenAnalyse
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector
import com.gemastik.dentistsmile.components.yolo.utils.CameraProcess
import com.gemastik.dentistsmile.databinding.ActivityYoloBinding
import com.gemastik.dentistsmile.utils.getAspectRatio
import com.gemastik.dentistsmile.utils.getAspectRatioString
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class YoloActivity : AppCompatActivity() {

    val binding: ActivityYoloBinding by lazy {
        ActivityYoloBinding.inflate(layoutInflater)
    }

    private val cameraCapabilities = mutableListOf<CameraCapability>()
    private var enumerationDeferred: Deferred<Unit>? = null
    private lateinit var videoCapture: VideoCapture<Recorder>
    private var cameraIndex = 0
    private var qualityIndex = DEFAULT_QUALITY_IDX

    private val cameraProcess = CameraProcess()

    //Yolo
    private var yolov5TFLiteDetector: Yolov5TFLiteDetector? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initCamera()
    }

    private fun initCamera() {
        this.lifecycleScope.launch {
            if (enumerationDeferred != null) {
                enumerationDeferred!!.await()
                enumerationDeferred = null
            }
            bindCaptureUsecase()
        }
    }

    private suspend fun bindCaptureUsecase() {
        val cameraProvider = ProcessCameraProvider.getInstance(baseContext).await()

        val cameraSelector = getCameraSelector(cameraIndex)
        val quality = cameraCapabilities[cameraIndex].qualities[qualityIndex]
        val qualitySelector = QualitySelector.from(quality)

        binding.previewView.updateLayoutParams<ConstraintLayout.LayoutParams> {
            val orientation = this@YoloActivity.resources.configuration.orientation
            dimensionRatio = quality.getAspectRatioString(
                quality,
                (orientation == Configuration.ORIENTATION_PORTRAIT)
            )
        }

        val preview = Preview.Builder()
            .setTargetAspectRatio(quality.getAspectRatio(quality))
            .build().apply {
                setSurfaceProvider(binding.previewView.surfaceProvider)
            }

        val recorder = Recorder.Builder()
            .setQualitySelector(qualitySelector)
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        try {
            initYoloModel("yolocustom")

            val fullScreenAnalyse = FullScreenAnalyse(
                this,
                binding.previewView,
                binding.boxLabelCanvas,
                0,
                binding.time,
                binding.frameSize,
                yolov5TFLiteDetector
            )

            val imageAnalysis: ImageAnalysis =
                ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetAspectRatio(quality.getAspectRatio(quality))
                    .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), fullScreenAnalyse)

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                imageAnalysis,
                preview
            )

        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
//        enableUI(true)
    }

    /**
     * Retrieve the asked camera's type(lens facing type). In this sample, only 2 types:
     *   idx is even number:  CameraSelector.LENS_FACING_BACK
     *          odd number:   CameraSelector.LENS_FACING_FRONT
     */
    private fun getCameraSelector(idx: Int): CameraSelector {
        if (cameraCapabilities.size == 0) {
            Log.i(TAG, "Error: This device does not have any camera, bailing out")
            finish()
        }
        return (cameraCapabilities[idx % cameraCapabilities.size].camSelector)
    }

    data class CameraCapability(val camSelector: CameraSelector, val qualities: List<Quality>)

    /**
     * Query and cache this platform's camera capabilities, run only once.
     */
    init {
        enumerationDeferred = lifecycleScope.async {
            whenCreated {
                val provider = ProcessCameraProvider.getInstance(this@YoloActivity).await()

                provider.unbindAll()
                for (camSelector in arrayOf(
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    CameraSelector.DEFAULT_FRONT_CAMERA
                )) {
                    try {
                        // just get the camera.cameraInfo to query capabilities
                        // we are not binding anything here.
                        if (provider.hasCamera(camSelector)) {
                            val camera = provider.bindToLifecycle(this@YoloActivity, camSelector)
                            QualitySelector
                                .getSupportedQualities(camera.cameraInfo)
                                .filter { quality ->
                                    listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD)
                                        .contains(quality)
                                }.also {
                                    cameraCapabilities.add(CameraCapability(camSelector, it))
                                }
                        }
                    } catch (exc: java.lang.Exception) {
                        Log.e(TAG, "Camera Face $camSelector is not supported")
                    }
                }
            }
        }
    }

    private fun initYoloModel(modelName: String) {
        try {
            this.yolov5TFLiteDetector = Yolov5TFLiteDetector()
            this.yolov5TFLiteDetector?.let {
                it.modelFile = modelName
                it.addGPUDelegate()
                it.initialModel(this)
                Log.i("model", "Success loading model" + it.getModelFile())
            }
        } catch (e: java.lang.Exception) {
            Log.e("image", "load model error: " + e.message + e.toString())
        }
    }

    companion object {
        private const val TAG = "YoloActivity"
        const val DEFAULT_QUALITY_IDX = 0
    }
}