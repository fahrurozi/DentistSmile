package com.gemastik.dentistsmile.utils

fun mergeBitmap(
    foreground: android.graphics.Bitmap,
    background: android.graphics.Bitmap
): android.graphics.Bitmap {
    val result = android.graphics.Bitmap.createBitmap(
        background.width,
        background.height,
        background.config
    )
    val canvas = android.graphics.Canvas(result)
    canvas.drawBitmap(background, 0f, 0f, null)
    canvas.drawBitmap(foreground, 0f, 0f, null)
    return result
}