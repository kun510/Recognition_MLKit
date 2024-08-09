package com.kun510.mlkit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Pair
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import java.io.IOException
import java.util.Random
import kotlin.collections.ArrayList
import kotlin.collections.List

class FaceRecognitionActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK = 0
    }

    private lateinit var btnPhoto: ImageButton
    private lateinit var ivPhoto: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvContours: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_recognition)

        btnPhoto = findViewById(R.id.btnPhoto)
        ivPhoto = findViewById(R.id.ivPhoto)
        progressBar = findViewById(R.id.progressBar)
        rvContours = findViewById(R.id.rvContours)

        btnPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK) {
            val uri = data?.data
            uri?.let { scanContours(it) }
        }
    }

    private fun scanContours(uri: Uri) {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build()

        try {
            progressBar.visibility = View.VISIBLE
            val image = FirebaseVisionImage.fromFilePath(this, uri)
            val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
            detector.detectInImage(image)
                .addOnSuccessListener { firebaseVisionFaces ->
                    val bmp = drawContoursOnImage(uri, firebaseVisionFaces)
                    if (bmp != null) {
                        ivPhoto.setImageBitmap(bmp)
                        Toast.makeText(this@FaceRecognitionActivity, "SUCCESS", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@FaceRecognitionActivity, "Faces couldn't be scanned", Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@FaceRecognitionActivity, e.toString(), Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun drawRectsOnImage(image: Uri, faces: List<FirebaseVisionFace>): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(contentResolver, image)
            var bmp = ImageDecoder.decodeBitmap(source)
            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true)

            val canvas = Canvas(bmp)

            val paint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 10f
                color = Color.RED
                isAntiAlias = true
            }

            for (face in faces) {
                canvas.drawRect(face.boundingBox, paint)
            }

            bmp
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun drawContoursOnImage(image: Uri, faces: List<FirebaseVisionFace>): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(contentResolver, image)
            var bmp = ImageDecoder.decodeBitmap(source)
            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true)

            val canvas = Canvas(bmp)

            val paint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 3f
                isAntiAlias = true
            }

            val colorList: ArrayList<Pair<Int, String>> = setupColorList()
            rvContours.adapter = RecyclerViewAdapter(this, colorList)
            rvContours.layoutManager = LinearLayoutManager(this)

            for (face in faces) {
                drawContoursOnCanvas(getAllContourLists(face), colorList, canvas, paint)
            }

            bmp
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getAllContourLists(face: FirebaseVisionFace): List<List<FirebaseVisionPoint>> {
        val result = ArrayList<List<FirebaseVisionPoint>>()
        for (i in 2..14) {
            result.add(face.getContour(i).points)
        }
        return result
    }

    private fun drawContoursOnCanvas(
        allContours: List<List<FirebaseVisionPoint>>,
        colorList: List<Pair<Int, String>>,
        canvas: Canvas,
        paint: Paint
    ) {
        for (i in allContours.indices) {
            val points = allContours[i]
            val curColor = colorList[i].first
            paint.color = curColor

            for (j in 0 until points.size - 1) {
                val point1 = points[j]
                val point2 = points[j + 1]
                canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint)
            }
            if (points.isNotEmpty()) {
                val firstPoint = points[0]
                val lastPoint = points[points.size - 1]
                canvas.drawLine(lastPoint.x, lastPoint.y, firstPoint.x, firstPoint.y, paint)
            }
        }
    }

    private fun setupColorList(): ArrayList<Pair<Int, String>> {
        val r = Random()
        val colorList = ArrayList<Pair<Int, String>>()
        val allContourTypes = arrayListOf(
            "Face",
            "Left Eyebrow Top", "Left Eyebrow Bottom", "Right Eyebrow Top",
            "Right Eyebrow Bottom", "Left Eye", "Right Eye", "Upper Lip Top",
            "Upper Lip Bottom", "Lower Lip Top", "Lower Lip Bottom",
            "Nose Bridge", "Nose Bottom"
        )
        for (i in 2..14) {
            val color = Color.argb(255, r.nextInt(255), r.nextInt(255), r.nextInt(255))
            colorList.add(Pair(color, allContourTypes[i - 2]))
        }
        return colorList
    }
}
