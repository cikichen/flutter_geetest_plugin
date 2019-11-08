# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\AndroidSdk\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# 极验SDK已做混淆处理，集成时请带上混淆规则，勿再次混淆SDK
-dontwarn com.geetest.sdk.**
-keep class com.geetest.sdk.**{*;}

# 腾讯 x5 SDK已做混淆处理，集成时请带上混淆规则，勿再次混淆SDK
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}