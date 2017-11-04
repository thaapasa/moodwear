# MoodWear
Android Wear example complication app. Shows a mood-indicating icon 
in a watch face complication. Click icon to change mood.

## Development

This is a standard Android Studio / IntelliJ IDEA -generated project 
using gradle wrapper. Build using gradle, develop using IDEA / Android Studio.

## Debugging in watch

See instructions at 
[https://developer.android.com/training/wearables/apps/debugging.html](https://developer.android.com/training/wearables/apps/debugging.html).

Debug forwarding commands:

    adb forward tcp:4444 localabstract:/adb-hub
    adb connect 127.0.0.1:4444

## Icons

Images can be vectorized at [https://www.vectorizer.io/](https://www.vectorizer.io/) 
and SVG images can be converted to Android image assets at 
[http://inloop.github.io/svg2android/](http://inloop.github.io/svg2android/).

Android launcher icon versions can be generated at 
[https://romannurik.github.io/AndroidAssetStudio/index.html](https://romannurik.github.io/AndroidAssetStudio/index.html).
