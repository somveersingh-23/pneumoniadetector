# 🫁 LungScan AI - Pneumonia Detection App

<div align="center">

**AI-Powered Chest X-Ray Analysis for Pneumonia Detection**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)

</div>

---

## 📋 Overview

**LungScan AI** is a modern Android application that uses deep learning to detect pneumonia from chest X-ray images. Built with Jetpack Compose and powered by a ResNet50 model, it provides fast and accurate pneumonia detection with **98.72% sensitivity**.

---

## ✨ Key Features

- 🎯 **AI-Powered Detection** - ResNet50 model with 98.97% recall rate
- ⚡ **Fast Analysis** - Results in under 2 seconds
- 🎨 **Modern UI** - Material 3 design with Jetpack Compose
- 📊 **Detailed Results** - Confidence scores and classification breakdown
- 🔄 **Real-Time Status** - Live API server health monitoring
- 📱 **Responsive Design** - Works on all Android screen sizes

---

## 📱 Screenshots

| Home Screen | Scan Screen | Result Screen |
|-------------|-------------|---------------|
| Main dashboard | Image upload | Analysis results |



---

## 🚀 Installation

### Download & Install

1. Download the APK from [Releases](https://github.com/yourusername/lungscan-ai/releases)
2. Enable "Install from Unknown Sources" in your phone settings
3. Open the APK file and tap Install
4. Launch the app

### Requirements

- Android 7.0 (API 24) or higher
- Internet connection (for API calls)
- ~50 MB free storage

---

## 🎮 How to Use

### Quick Start Guide

1. **Launch** the app and check server status shows "Online"
2. **Tap** "Start X-Ray Analysis" button
3. **Select** an image from gallery or capture with camera
4. **Upload** the X-ray image for analysis
5. **View** results with confidence score
6. **Follow** recommended next steps

### Tips for Best Results

✅ Use clear, high-quality chest X-ray images  
✅ Ensure good lighting for camera captures  
✅ Wait for server status to show "Online"  
✅ Review confidence scores carefully  

❌ Don't use blurry or low-resolution images  
❌ Don't upload non-chest X-ray images  
❌ Don't rely solely on app results for diagnosis  

---

## 🏗️ Technical Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin 100% |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM Pattern |
| **Networking** | Retrofit + OkHttp |
| **Image Loading** | Coil |
| **Async** | Kotlin Coroutines |
| **Backend** | FastAPI (Python) |
| **ML Model** | ResNet50 |
| **Hosting** | Render.com |

---

## 🧠 Model Information

### ResNet50 Deep Learning Model

- **Architecture**: 50-layer deep residual network
- **Input**: 224×224 RGB chest X-ray images
- **Output**: Binary classification (Normal/Pneumonia)

### Performance Metrics

| Metric | Score |
|--------|-------|
| Sensitivity (Recall) | 98.97% |
| Specificity | ~95% |
| Accuracy | ~97% |
| Inference Time | < 2 seconds |

### Training Dataset

- **Source**: [Chest X-Ray Images (Pneumonia) - Kaggle](https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia)
- **Training Images**: ~5,000
- **Classes**: Normal, Bacterial Pneumonia, Viral Pneumonia

---

## 🛠️ Development

### Build from Source

```bash
# Clone repository
git clone https://github.com/yourusername/lungscan-ai.git
cd lungscan-ai

# Build APK
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### Project Structure

```
app/src/main/java/com/kaidwal/pneumoniadetector/
├── data/
│   ├── api/              # API services & Retrofit config
│   ├── model/            # Data models
│   └── repository/       # Data layer
├── ui/
│   ├── components/       # Reusable UI components
│   ├── navigation/       # Navigation graph
│   ├── screens/          # App screens
│   └── theme/            # Material theme & colors
├── viewmodel/            # Business logic
└── MainActivity.kt       # App entry point
```

### Configuration

**API Base URL**: `app/src/main/java/.../data/api/RetrofitClient.kt`

```kotlin
private const val BASE_URL = "https://pneumonia-xray-classifier.onrender.com/"
```

**App Name**: `app/src/main/res/values/strings.xml`

```xml
<string name="app_name">LungScan AI</string>
```

---

## 🔐 API Documentation

### Endpoints

**Base URL**: `https://pneumonia-xray-classifier.onrender.com`

#### Health Check
```http
GET /health
```
Response:
```json
{
  "status": "healthy",
  "model_loaded": true
}
```

#### Predict
```http
POST /predict
Content-Type: multipart/form-data
```
Request:
```
file: <chest_xray.jpg>
```
Response:
```json
{
  "prediction": "PNEUMONIA",
  "confidence": 0.9534,
  "probabilities": {
    "NORMAL": 0.0466,
    "PNEUMONIA": 0.9534
  }
}
```

---

## 🎨 Design System

### Colors

```kotlin
Primary Blue    = #2196F3
Accent Teal     = #00BCD4
Success Green   = #4CAF50
Warning Yellow  = #FFC107
Error Red       = #F44336
```

### Typography

- Headlines: Roboto Bold
- Body: Roboto Regular
- Captions: Roboto Light

---

## 📊 Performance

### Optimizations Implemented

✅ ProGuard/R8 code shrinking  
✅ Resource shrinking  
✅ Image compression (JPEG 85%)  
✅ 120-second timeout for cold starts  
✅ Automatic retry on failure  
✅ Image caching with Coil  

### APK Size

- Unoptimized: ~20 MB
- Optimized: ~8-12 MB

---

## 🚧 Known Limitations

⚠️ **Cold Start**: First API call may take 50-60 seconds (Render free tier)  
⚠️ **Internet Required**: Backend connection needed for analysis  
⚠️ **Max Image Size**: 10 MB per image  
⚠️ **X-Ray Only**: Works only with chest X-ray images  

---

## 🗺️ Roadmap

### Version 1.1 (Coming Soon)
- [ ] Offline TensorFlow Lite model
- [ ] Scan history storage
- [ ] PDF report export
- [ ] Dark mode improvements

### Version 2.0 (Future)
- [ ] Multi-disease detection (COVID-19, TB)
- [ ] DICOM file support
- [ ] Real-time camera analysis
- [ ] Cloud sync

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/NewFeature`)
3. Commit changes (`git commit -m 'Add NewFeature'`)
4. Push to branch (`git push origin feature/NewFeature`)
5. Open a Pull Request

---

## ⚠️ Medical Disclaimer

**IMPORTANT**: This application is for **educational and research purposes only**.

- ❌ NOT a substitute for professional medical advice
- ❌ NOT for clinical diagnosis
- ✅ Use only as a preliminary screening tool
- ✅ Always consult qualified healthcare professionals
- ✅ Results must be verified by licensed radiologists

**The developers assume no liability for medical decisions made based on this app.**

---

## 📄 License

MIT License - Copyright (c) 2026 Somveer Singh

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software.

See [LICENSE](LICENSE) file for full details.

---

## 👨‍💻 Author

**Somveer Singh**  
🎓 BTech CSE, RV Institute of Technology, Bijnor  
📧 somveersingh306599@gmail.com 
🔗 [GitHub](https://github.com/somveersingh-23) 

---

## 🙏 Acknowledgments

- Dataset: [Kaggle Chest X-Ray Dataset](https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia)
- Model: ResNet50 (He et al., 2015)
- Hosting: [Render.com](https://render.com)
- Icons: [Material Icons](https://fonts.google.com/icons)

---

## 📞 Support
- 📧 Email: somveersingh306599@gmail.com

---

<div align="center">

**⭐ If you find this project helpful, please star it!**

Made with ❤️ by Somveer Singh

**Version 1.0.0** • Last Updated: January 29, 2026

</div>
