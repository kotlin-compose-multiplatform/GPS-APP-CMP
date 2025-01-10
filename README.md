# GPS Tracking System (Alternative to Wialon Local)

A powerful and flexible GPS tracking application built with Kotlin Compose Multiplatform, providing seamless support for both Android and iOS platforms. This app is an alternative to Wialon Local, designed for real-time tracking and monitoring of units.

---

## Features
- **Real-time GPS Tracking**
- **Unit Status Monitoring**
- **Cross-Platform Support**: Android and iOS
- **Lightweight and Fast**
- **App support in 3 languages [Russian, English, Turkmen], default is Russian**

---

## Used Libraries
- **Google Maps expect/actual self configured for each target**
- **Ktor client for api call**
- **Koin for dependency injection**
- **Voyager for navigation**
- **Multiplatform-settings library for save data in local**
- **Lyricist library for localization**
- **Coil for image loading**

---

## Screenshots
![Login Screen](https://play-lh.googleusercontent.com/cDM5XF2H4NkQX4SdzCs3WInyCHrmFaYXnLSpp6UZFV3H-6Y2UHLE2p_yDsavDDrOQto=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/fw35PgADrzkR3qa9n3-X4-C-mKcH7s9FEkC-TtWZJnHcnogl7zfj7-166l1Km8HrQso=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/06Bfm-xZODDspqoBJ3bn7Ret2Id-PtZn0S3U92CPvKtfh2Y1aw909hhjxlMo6KVynrU=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/sPWH1BCIgR--WhU1NXS2zYVAzicO9mUqNXVRj-YjTcCSUUUYBTOhYUVW1y6o4xLcBuga=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/4kN5lAYz6swqbzeQ0Zg_zIyeUJ44IAxWO4A-NLZa2D3j_7H0psU5VW2pk0DvDmVusg=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/auQd-bbtv2e5WkhYYAmaSL34ff-3RBMVPMRtIDQcUnsuAGI6Lb02XnBy8JPxrCFB47c=w526-h296)
![Dashboard](https://play-lh.googleusercontent.com/KBaa4rhS1D10uhUcDDhOUhV49VgFoksekPMt2Ll33_k36Hh9CcwxHgvzvUuTnhabYQ4=w526-h296)

---

## Demo Video
Watch a full demonstration of the app on Google Drive:

![GPS Tracking System Demo](https://play-lh.googleusercontent.com/j5Gtpkih2WI7-8GvEXTOkleLaSWzShvjcgzXZJM_1shIV8EgLoJox4AOD8bIVsHO6KgW=w240-h480)

Click the image or [here](https://drive.google.com/file/d/1m2QY3V9-CdBa9eMDgXSvInk-jFHGPlhz/view?usp=sharing) to watch the demo.

---


## Play Store
[Download the app from Google Play Store](https://play.google.com/store/apps/details?id=com.gs.wialonlocal.android&hl=en)

---

## Getting Started

### Prerequisites
- **Android Studio** (latest version with Kotlin Multiplatform plugin)
- **Xcode** (for iOS development)
- **JDK 17 or later**

---

## Installation

### Step 1: Clone the Project
```bash
git clone https://github.com/yourusername/gps-tracking-system.git
cd gps-tracking-system
```


### Step 2: Configure Google Maps API Key [Optional, default key is exist in the code]

#### Android:

1. Open `androidApp/src/main/AndroidManifest.xml`.
2. Locate the placeholder `YOUR_GOOGLE_MAPS_API_KEY` and replace it with your Google Maps API key:

   xml

   Copy code

   `<meta-data     android:name="com.google.android.geo.API_KEY"     android:value="YOUR_GOOGLE_MAPS_API_KEY" />`


#### iOS:

1. Open the `ContentView.swift` file.
2. Update the Google Maps API key in the initialization code:

   swift

   Copy code

   `GMSServices.provideAPIKey("YOUR_GOOGLE_MAPS_API_KEY")`


---

### Step 3: Update Android Release Keystore [Optional, default key is exist in the code]

1. Replace the existing `androidApp/jetbrains.jks` file in the project with your own keystore file.
2. Update the `androidApp/build.gradle.kts` file with your keystore credentials:
   `signingConfigs {
     create("release") {
       keyAlias = "jetbrains"
       keyPassword = "changeme"
       storeFile = file("jetbrains.jks")
       storePassword = "changeme"
     }
   }`
---

### Step 4: Build and Run the Project

#### Android:

1. Open the project in Android Studio.
2. Select the desired emulator or connected device.
3. Click **Run** ▶️.

#### iOS:

1. Open the `iosApp` project in Xcode.
2. Select a simulator (e.g., iPhone 14).
3. Click **Run** ▶️.

---

## Usage

### Login Credentials:

- **Username**: `demo`
- **Password**: `demo2022`

1. Launch the app on your device or emulator.
2. Enter the demo credentials on the login screen.
3. Explore the app features.