package com.henrique.gemininative

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        configureWebView()
        configureCookies()
        configureBackNavigation()

        webView.loadUrl(START_URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = false
            allowContentAccess = false

            // Zoom totalmente desativado (pinça e toque duplo).
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            useWideViewPort = false
            loadWithOverviewMode = false

            // Sem pop-ups e sem janelas múltiplas.
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false

            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW

            // User-Agent do Chrome para reduzir bloqueios de login do Google
            // em WebViews embutidas.
            userAgentString = CHROME_USER_AGENT
        }

        webView.apply {
            // Elimina tremedeiras, brilho de overscroll e pull-to-refresh nativo.
            overScrollMode = View.OVER_SCROLL_NEVER

            // Elimina as barras de rolagem.
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false

            // Consome o pressionamento longo: nada de menu de contexto.
            setOnLongClickListener { true }

            // Fundo escuro para evitar clarão branco durante o carregamento.
            setBackgroundColor(Color.parseColor(BACKGROUND_COLOR))
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                // Bloqueia qualquer navegação fora dos domínios do Google,
                // além de esquemas como intent:, mailto: e tel:.
                return !isAllowedUrl(request)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                injectBlockingScript(view)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                injectBlockingScript(view)
            }

            override fun onRenderProcessGone(
                view: WebView,
                detail: RenderProcessGoneDetail
            ): Boolean {
                recreate()
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                // Reinjeta o script em vários estágios do carregamento da SPA.
                if (newProgress in 25..95) {
                    injectBlockingScript(view)
                }
            }
        }
    }

    private fun configureCookies() {
        val cookies = CookieManager.getInstance()
        cookies.setAcceptCookie(true)
        cookies.setAcceptThirdPartyCookies(webView, true)
    }

    private fun configureBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun injectBlockingScript(view: WebView) {
        view.evaluateJavascript(BLOCKING_SCRIPT, null)
    }

    private fun isAllowedUrl(request: WebResourceRequest): Boolean {
        val url = request.url
        val scheme = url.scheme?.lowercase()
        if (scheme != "https" && scheme != "http") {
            return false
        }
        val host = url.host?.lowercase() ?: return false
        return ALLOWED_HOSTS.any { host == it || host.endsWith("." + it) }
    }

    override fun onActionModeStarted(mode: ActionMode) {
        // Encerra qualquer fita de seleção que eventualmente seja criada.
        mode.finish()
        super.onActionModeStarted(mode)
    }

    override fun onSupportActionModeStarted(mode: androidx.appcompat.view.ActionMode) {
        mode.finish()
        super.onSupportActionModeStarted(mode)
    }

    override fun onDestroy() {
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }

    /**
     * Script injetado na página para suprimir seleção de texto, alças azuis,
     * menu de contexto, barras de rolagem do conteúdo, zoom por gesto e
     * qualquer pull-to-refresh implementado pela própria página.
     */
    private val BLOCKING_SCRIPT = """
        (function () {
          if (window.__geminiNativeBlockerInstalled) { return; }
          window.__geminiNativeBlockerInstalled = true;

          var css = '' +
            '* { -webkit-user-select: none !important; user-select: none !important; -webkit-touch-callout: none !important; } ' +
            '::-webkit-scrollbar { display: none !important; width: 0 !important; height: 0 !important; } ' +
            'html, body { overscroll-behavior: none !important; -webkit-tap-highlight-color: transparent !important; } ' +
            'body { touch-action: pan-x pan-y !important; }';
          var style = document.createElement('style');
          style.setAttribute('data-native-blocker', 'v1');
          style.textContent = css;
          (document.head || document.documentElement).appendChild(style);

          var startY = null;
          function everythingScrolledToTop(target) {
            var node = target;
            while (node && node !== document.documentElement) {
              if (node.scrollTop > 0) { return false; }
              node = node.parentElement;
            }
            return (window.scrollY || document.documentElement.scrollTop || 0) <= 0;
          }
          document.addEventListener('touchstart', function (e) {
            startY = (e.touches.length === 1) ? e.touches[0].clientY : null;
          }, { passive: true });
          document.addEventListener('touchmove', function (e) {
            if (startY !== null && e.touches.length === 1) {
              var dy = e.touches[0].clientY - startY;
              if (dy > 12 && everythingScrolledToTop(e.target)) {
                e.preventDefault();
              }
            }
          }, { passive: false });
          document.addEventListener('contextmenu', function (e) {
            e.preventDefault();
          }, true);
        })();
    """.trimIndent()

    companion object {
        private const val START_URL = "https://gemini.google.com/app?hl=pt-BR"
        private const val BACKGROUND_COLOR = "#1E1F20"
        private const val CHROME_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36"
        private val ALLOWED_HOSTS = listOf(
            "google.com",
            "googleusercontent.com",
            "gstatic.com",
            "g.co"
        )
    }
}
