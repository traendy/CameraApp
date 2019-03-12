package de.traendy.cameraapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

private const val TAG = "MainActivity"

private const val REQUEST_IMAGE_CAPTURE: Int = 10
private const val FILENAME: String = "photo"
private const val FILE_PROVIDER_AUTHORITY = "de.traendy.cameraapp.android.fileprovider"

class MainActivity : AppCompatActivity() {

    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pictureButton.setOnClickListener { takeAPicture() }
    }

    private fun takeAPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(FILENAME)
                } catch (ex: IOException) {
                    Log.e(TAG, ex.message)
                    null
                }
                photoFile?.also {
                    val photoUri: Uri =
                        FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(filename: String): File? {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            filename, /* name */
            ".png", /* extention */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageBitmap: Bitmap?
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = handleSamplingAndRotationBitmap(
                this,
                Uri.fromFile(File(currentPhotoPath)))
            imageBitmap?.let {
                imageView.setImageBitmap(it)
            }
        }
    }
}
