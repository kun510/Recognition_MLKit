package com.kun510.mlkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException

class ImageLabelingActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK = 0
    }

    private lateinit var btnPhoto: ImageButton
    private lateinit var ivPhoto: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvLabels: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)

        btnPhoto = findViewById(R.id.btnPhoto)
        ivPhoto = findViewById(R.id.ivPhoto)
        progressBar = findViewById(R.id.progressBar)
        tvLabels = findViewById(R.id.tvLabels)

        btnPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK) {
            val uri = data?.data
            uri?.let { labelImage(it) }
        }
    }

    private fun labelImage(uri: Uri) {
        try {
            val image = FirebaseVisionImage.fromFilePath(this, uri)
            val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
            progressBar.visibility = View.VISIBLE

            labeler.processImage(image)
                .addOnSuccessListener { labels ->
                    if (labels.isEmpty()) {
                        tvLabels.text = "No labels detected"
                    } else {
                        val sb = StringBuilder("Recognized labels:\n")
                        for ((index, label) in labels.withIndex()) {
                            sb.append("${index + 1}. ${label.text}\n")
                        }
                        tvLabels.text = sb.toString()
                        ivPhoto.setImageURI(uri)
                    }
                    progressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    Toast.makeText(this@ImageLabelingActivity, "Oops, that didn't work!", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
