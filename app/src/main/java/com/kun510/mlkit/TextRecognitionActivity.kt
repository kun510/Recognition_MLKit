package com.kun510.mlkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException

class TextRecognitionActivity : AppCompatActivity() {

    companion object {
        private const val PICK_REQUEST_CODE = 0
    }

    private lateinit var btnPhoto: ImageButton
    private lateinit var ivPhoto: ImageView
    private lateinit var tvImageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        btnPhoto = findViewById(R.id.btnPhoto)
        ivPhoto = findViewById(R.id.ivPhoto)
        tvImageText = findViewById(R.id.tvImageText)

        btnPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_REQUEST_CODE) {
            val selectedImage = data?.data
            selectedImage?.let {
                ivPhoto.setImageURI(it)
                scanText(it)
            }
        }
    }

    private fun scanText(uri: Uri) {
        try {
            val image = FirebaseVisionImage.fromFilePath(this, uri)
            val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

            textRecognizer.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    tvImageText.text = firebaseVisionText.text
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
