package com.example.lab2

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentPhoto: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            Intent(this, NewActivity::class.java).also { i ->
                i.putExtra(MESSAGE, editText.text)
                startActivityForResult(i, ACTIVITY_REQUEST)
            }
        }

        button2.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { i ->
                i.resolveActivity(packageManager)?.also {
                    File.createTempFile(
                        "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                        ".jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )
                        .apply { currentPhoto = absolutePath }
                        ?.also {
                            val uri = FileProvider.getUriForFile(
                                this,
                                "com.example.android.fileprovider",
                                it
                            )
                            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                            startActivityForResult(i, CAMERA_REQUEST)
                        }
                }
            }
        }

    }

    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setTitle("Czy na pewno?")
            .setPositiveButton("TAK") { _, _ -> super.onBackPressed() }
            .setNegativeButton("NIEEE!") { _, _ -> ; }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTIVITY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Toast.makeText(this, data?.extras?.get(MESSAGE) as CharSequence?, Toast.LENGTH_SHORT).show()
            }

            CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    BitmapFactory.Options()
                        .apply {
                            inJustDecodeBounds = true
                            BitmapFactory.decodeFile(currentPhoto, this)
                            val photoW: Int = outWidth
                            val photoH: Int = outHeight

                            val scaleFactor: Int = Math.min(photoW / imageView.width, photoH / imageView.height)

                            inJustDecodeBounds = false
                            inSampleSize = scaleFactor
                        }
                        .also {
                            BitmapFactory.decodeFile(currentPhoto, it)
                                ?.apply {
                                    imageView.setImageBitmap(this)
                                }
                        }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val ACTIVITY_REQUEST = 1
        const val CAMERA_REQUEST = 2
        const val MESSAGE = "MESSAGE"
    }
}
