# Mantém a WebView personalizada, referenciada a partir do layout XML.
-keep class com.henrique.gemininative.NoWebChromeWebView { *; }

# Preserva membros públicos de subclasses de WebView manipuladas pelo sistema.
-keepclassmembers class * extends android.webkit.WebView {
    public *;
}
