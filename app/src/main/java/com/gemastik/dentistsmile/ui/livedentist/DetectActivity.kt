package com.gemastik.dentistsmile.ui.livedentist

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gemastik.dentistsmile.databinding.ActivityDetectBinding

class DetectActivity : AppCompatActivity() {
    private val binding: ActivityDetectBinding by lazy {
        ActivityDetectBinding.inflate(layoutInflater)
    }

    private var bitmapOverlay: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bitmapOverlay = intent.getParcelableExtra("bitmapOverlay")

        binding.ivResult.setImageBitmap(bitmapOverlay)
    }
}