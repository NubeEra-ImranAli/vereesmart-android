# VereeSmart Android Controller

Open `android-app` in Android Studio and build/run it on an Android phone (minimum Android 6.0).

- Connect the phone to the ESP32 SoftAP using **Android Settings** first.
- The app has no Wi-Fi picker, no IP field, and no connection form.
- Every HTTP request is fixed to `http://192.168.4.1`.
- It polls `/api/status` every two seconds and treats that response as authoritative.

The only Android permission is `INTERNET`. Clear-text HTTP is allowed only for `192.168.4.1` in `network_security_config.xml`.
