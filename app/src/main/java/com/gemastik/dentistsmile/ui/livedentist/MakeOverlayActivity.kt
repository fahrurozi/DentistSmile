package com.gemastik.dentistsmile.ui.livedentist

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.gemastik.dentistsmile.components.tflite.ImageSegmentationHelper
import com.gemastik.dentistsmile.components.yolo.analysis.YoloAnalyse
import com.gemastik.dentistsmile.components.yolo.detector.Yolov5TFLiteDetector
import com.gemastik.dentistsmile.databinding.ActivityMakeOverlayBinding
import com.gemastik.dentistsmile.utils.mergeBitmap
import dmax.dialog.SpotsDialog
import org.tensorflow.lite.task.vision.segmenter.Segmentation
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MakeOverlayActivity : AppCompatActivity(), ImageSegmentationHelper.SegmentationListener {

    private val binding: ActivityMakeOverlayBinding by lazy {
        ActivityMakeOverlayBinding.inflate(layoutInflater)
    }
    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(this, "Processing Image...")
    }

    private lateinit var imageSegmentationHelper: ImageSegmentationHelper
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var cameraExecutor: ExecutorService

    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

    private var bitmapOverlay: Bitmap? = null

    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCameraObserver()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
        setupButtonCamera()
    }

    private fun setupButtonCamera() {
        binding.btnTakePicture.setOnClickListener {
            bitmapOverlay?.let {
                takePicture()
            } ?: kotlin.run {
                Toast.makeText(
                    this,
                    "Please wait for the segmentation to finish",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(baseContext, "Permission request granted", Toast.LENGTH_LONG).show()
                startCameraObserver()
            } else {
                Toast.makeText(baseContext, "Permission request denied", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private fun startCameraObserver() {
        initYoloModel()
        imageSegmentationHelper = ImageSegmentationHelper(
            context = this,
            imageSegmentationListener = this
        )


        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        binding.viewFinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    // Declare and bind preview, capture and analysis use cases
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            // The image rotation and RGB image buffer are initialized only once
                            // the analyzer has started running
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }

                        segmentImage(image)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun segmentImage(image: ImageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        val imageRotation = image.imageInfo.rotationDegrees
        // Pass Bitmap and rotation to the image segmentation helper for processing and segmentation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageSegmentationHelper.segment(bitmapBuffer, imageRotation)
        } else {
            Toast.makeText(this, "Require Android Q and higher", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onResults(
        results: List<Segmentation>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        this.runOnUiThread {
            // Pass necessary information to OverlayView for drawing on the canvas
            bitmapOverlay = binding.overlay.setResults(
                results,
                imageHeight,
                imageWidth
            )

            binding.overlay.invalidate()
        }
    }

    private fun takePicture() {
        with(binding) {
            if (viewFinder.bitmap != null) {
                bitmapOverlay?.let {
                    val mergeBitmap = mergeBitmap(it, viewFinder.bitmap!!)
                    spotsDialog.show()
                    YoloAnalyse(
                        yolov5TFLiteDetector,
                        viewFinder.bitmap!!
                    ) { bitmap ->
                        spotsDialog.dismiss()
                        val mergedBitmap = mergeBitmap(bitmap, mergeBitmap)
                        ivDebug.setImageBitmap(mergedBitmap)
                        saveBitmapResult(mergedBitmap)
                    }.analyse()
                }
            }
        }
    }

    private fun saveBitmapResult(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        // Init Directory
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Dentistsmile/"
        )

        // Make Folder
        if (!folder.exists()) {
            folder.mkdirs()
        }

        // Folder AI
        val fileAi = File(folder, "dentistsmile_ai_$timestamp.jpeg")

        //create a file to write bitmap data
        //create a file to write bitmap data
        folder.createNewFile()

        try {
            val os: OutputStream = BufferedOutputStream(FileOutputStream(fileAi))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()

            // Activity Result
            val intent = Intent()
            intent.data = fileAi.toUri()
            Log.d("uriAi", fileAi.toUri().toString())
            Log.d("YoloRresult", Yolov5TFLiteDetector.teethProblemCounts.toString())
            setResult(RESULT_OK, intent)
            finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun initYoloModel() {
        try {
            this.yolov5TFLiteDetector = Yolov5TFLiteDetector()
            this.yolov5TFLiteDetector.let {
                it.modelFile = "yolocustom"
                it.addGPUDelegate()
                it.initialModel(this)
                Log.i("model", "Success loading model" + it.modelFile)
            }
        } catch (e: java.lang.Exception) {
            Log.e("image", "load model error: " + e.message + e.toString())
        }
    }

    companion object {
        private const val TAG = "MakeOverlayActivity"
    }
}