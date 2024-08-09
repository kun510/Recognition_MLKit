package com.kun510.mlkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnTextRecognition: Button
    private lateinit var btnFaceRecognition: Button
    private lateinit var btnImageLabeling: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTextRecognition = findViewById(R.id.btnTextRecognition)
        btnFaceRecognition = findViewById(R.id.btnFaceRecognition)
        btnImageLabeling = findViewById(R.id.btnImageLabeling)

        btnTextRecognition.setOnClickListener {
            startActivity(Intent(this, TextRecognitionActivity::class.java))
        }

        btnFaceRecognition.setOnClickListener {
            startActivity(Intent(this, FaceRecognitionActivity::class.java))
        }

        btnImageLabeling.setOnClickListener {
            startActivity(Intent(this, ImageLabelingActivity::class.java))
        }
    }
}
