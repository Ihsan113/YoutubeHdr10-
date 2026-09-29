# DanzKu YouTube HDR-like (prototype)

Android prototype: a non-interactive translucent overlay is shown only while `com.google.android.youtube` is the most recently used app. Requires Usage Access and Display over other apps permission.

Important limitations:
- This is a tint overlay only. Android's normal overlay API cannot read or shader-process YouTube's protected/underlying video pixels.
- It does not create HDR10+, BT.2020/PQ output, or dynamic HDR metadata.
- YouTube foreground detection uses UsageStats and may be delayed or restricted by ROM battery management.
- Debug APK is built by GitHub Actions (Actions > Build APK > artifact).

Build locally with Android SDK + Gradle 8.7: `gradle assembleDebug`.
