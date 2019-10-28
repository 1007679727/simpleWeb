package com.example.simplebrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileWriter;


public class SelfWebView extends WebView {
    private static final String TAG = "SelfWebView";
    private Handler mHandler;

    static final class JavaScriptLocal {
        JavaScriptLocal() {
        }

        public void saveSource(String source) {
            File file = new File("/sdcard/html.txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter output = new FileWriter(file);
                output.write(source);
                output.flush();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void log(String msg) {
            Log.i(SelfWebView.TAG, "come on!!! i am here, you see --" + msg);
        }
    }

    private class SelfWebChromeClient extends WebChromeClient {
        private SelfWebChromeClient() {
        }

        /* synthetic */ SelfWebChromeClient(SelfWebView selfWebView, SelfWebChromeClient selfWebChromeClient) {
            this();
        }
    }

    private class SelfWebViewClient extends WebViewClient {
        private SelfWebViewClient() {
        }

        /* synthetic */ SelfWebViewClient(SelfWebView selfWebView, SelfWebViewClient selfWebViewClient) {
            this();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            SelfWebView.this.mHandler.obtainMessage(3).sendToTarget();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i(SelfWebView.TAG, "started url = " + url);
            SelfWebView.this.mHandler.obtainMessage(4).sendToTarget();
            SelfWebView.this.mHandler.obtainMessage(1, url).sendToTarget();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public SelfWebView(Context context) {
        super(context);
        init();
    }

    public SelfWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelfWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(true);
        setWebViewClient(new SelfWebViewClient(this, null));
        setWebChromeClient(new SelfWebChromeClient(this, null));
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}
