package de.traendy.cameraapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap? {
    val imageStream = context.contentResolver.openInputStream(selectedImage)
    val img =  BitmapFactory.decodeStream(imageStream, null, BitmapFactory.Options())
    imageStream?.close()
    return img
}