package com.example.webtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val isWebViewInNestedScrollView = intent.getBooleanExtra(IS_WEBVIEW_IN_NESTED_SCROLLVIEW, true)
        val isLoadContentFromAssets = intent.getBooleanExtra(IS_LOAD_CONTENT_FROM_ASSETS, true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WebView.setWebContentsDebuggingEnabled(true)
        val webView: WebView = findViewById(R.id.web)
        webView.settings.apply {
            javaScriptEnabled = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            domStorageEnabled = true
        }
        if (isLoadContentFromAssets) {
            GlobalScope.launch(Dispatchers.Default) {
                val contentString = assets.open("article.html").bufferedReader().readText()
                launch(Dispatchers.Main) {
                    webView.loadDataWithBaseURL(
                        "https://sports.yahoo.com/micheal-jordan-catches-442-pound-marlin-in-north-carolina-fishing-tournament-001736287.html",
                        contentString,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        } else {
            webView.loadUrl("https://sports.yahoo.com/micheal-jordan-catches-442-pound-marlin-in-north-carolina-fishing-tournament-001736287.html")
        }
    }
    companion object {
        const val IS_WEBVIEW_IN_NESTED_SCROLLVIEW = "webview_in_nested_scrollview"
        const val IS_LOAD_CONTENT_FROM_ASSETS = "load_content_from_assets"
    }
}