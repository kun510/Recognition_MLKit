# 🤖 Recognition_MLKit – Android Kotlin App with Google ML Kit

**Recognition_MLKit** is a modern Android application built with **Kotlin** that integrates **Google's ML Kit** to perform various on-device machine learning tasks. The app provides real-time image recognition functionalities using the device's camera or gallery.

---

## 🚀 Features

- 📷 **Text Recognition (OCR)** – Detect and extract text from camera or image input.
- 😊 **Face Detection** – Identify human faces, facial landmarks, and emotions.
- 📦 **Barcode Scanning** – Read QR codes, EAN, UPC, and other barcode formats.
- 🧠 **Image Labeling** – Automatically label objects or scenes in an image.
- 🖼️ **Object Detection** – Detect and track multiple objects in real time (optional).
- 📁 **Image Picker** – Select image from gallery for offline analysis.

---

## 🧱 Project Structure

Recognition_MLKit/
├── app/
│ ├── src/
│ │ └── main/
│ │ ├── java/com/example/recognition_mlkit/
│ │ │ ├── MainActivity.kt
│ │ │ ├── TextRecognitionActivity.kt
│ │ │ ├── FaceDetectionActivity.kt
│ │ │ ├── BarcodeScannerActivity.kt
│ │ │ └── utils/
│ │ └── res/
│ │ ├── layout/
│ │ ├── drawable/
│ │ └── values/
├── build.gradle
├── AndroidManifest.xml

---

## ⚙️ Technologies & Dependencies

- **Kotlin** – Modern programming language for Android
- **CameraX** – Jetpack library for camera handling
- **Google ML Kit**:
  - `com.google.mlkit:text-recognition`
  - `com.google.mlkit:face-detection`
  - `com.google.mlkit:barcode-scanning`
  - `com.google.mlkit:image-labeling`
- **ViewModel + LiveData** – For clean state management
- **Material Components** – For UI styling

---

## 🔐 Permissions

Make sure to request these runtime permissions:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
For Android 13+ (API 33), add:

<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />


✅ Setup Instructions
Clone the repository:
git clone https://github.com/your-username/Recognition_MLKit.git
Open the project in Android Studio.

Sync Gradle and install dependencies.

Run the app on a device or emulator with camera support.

📦 Future Improvements
📹 Real-time video processing with CameraX

☁️ Cloud-based ML support (optional)

🌙 Dark Mode

🧪 Unit & Instrumented Tests

🧑‍💻 Author
Tran Cuong
