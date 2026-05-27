# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keep class com.example.pfinance.data.local.entity.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# Apache POI
-dontwarn org.apache.poi.**
-keep class org.apache.poi.** { *; }

# iText
-dontwarn com.itextpdf.**
-keep class com.itextpdf.** { *; }
