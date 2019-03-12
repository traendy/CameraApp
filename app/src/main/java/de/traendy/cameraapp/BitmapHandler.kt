package de.traendy.cameraapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap? {
    val imageStream = context.contentResolver.openInputStream(selectedImage)
    val image = BitmapFactory.decodeStream(imageStream, null, BitmapFactory.Options())
    imageStream?.close()
    return if(image != null){
        rotateImageIfRequired(image, selectedImage)
    } else {
        image
    }
}

private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
    selectedImage.path?.let {
        val orientation = ExifInterface(it).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(img, true)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(img, false)
            else -> img
        }
    }
    return img
}

private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree)
    return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
}

private fun flip(bitmap: Bitmap, horizontal: Boolean): Bitmap {
    val matrix = Matrix()
    matrix.preScale((if (horizontal) -1f else 1f), (if (!horizontal) -1f else 1f))
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
