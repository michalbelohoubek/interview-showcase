# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information forxys
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class cz.michalbelohoubek.** {*;}
-keep public class com.google.firebase.** {*;}
-keep class com.google.android.gms.internal.** {*;}
-keepclassmembers class cz.michalbelohoubek.interviewshowcase.network.model.** {
    <init>(...);
    <fields>;
}

-keepclassmembers class cz.michalbelohoubek.interviewshowcase.core.model.** {
    <init>(...);
    <fields>;
}

-keepclassmembers class cz.michalbelohoubek.interviewshowcase.model.** {
    <init>(...);
    <fields>;
}

-keep class cz.michalbelohoubek.interviewshowcase.network.model.** { *; }
-keep class cz.michalbelohoubek.interviewshowcase.core.model.** { *; }
-keep class cz.michalbelohoubek.interviewshowcase.model.** { *; }

-keepclasseswithmembers class com.google.firebase.FirebaseException
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

-printconfiguration ~/tmp/full-r8-config.txt

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}

-keepclasseswithmembers,includedescriptorclasses class * {
   @dagger.internal.KeepFieldType <fields>;
}