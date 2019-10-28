package com.example.simplebrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SimpleBrowserActivity extends Activity implements OnClickListener, TextWatcher {
    public static final int HIDE_LOADING = 3;
    public static final int LOAD_URL = 0;
    public static final int SHOW_LOADING = 4;
    public static final int UPDATE_URL = 1;
    private String blankUrl = "";
    private ImageView goForward;
    private String homeUrl = "https://mobile.cmbchina.com/MobileHtml/Login/LoginA.aspx";
    private InputMethodManager imm;
    private FrameLayout loading;
    private Handler mHandler = new Handler() {
        @SuppressLint("WrongConstant")
        public void handleMessage(Message msg) {
            String url;
            switch (msg.what) {
                case LOAD_URL:
                    if (msg.obj != null) {
                        url =  msg.obj.toString();
                        if (!url.startsWith("http")) {
                            url = "http://" + url;
                            SimpleBrowserActivity.this.urlInput.setText(url);
                        }
                        Log.i("ttte", "url = " + url);
                        SimpleBrowserActivity.this.webView.loadUrl(url);
                        return;
                    }
                    return;
                case UPDATE_URL:
                    url = (String) msg.obj;
                    if (url != null) {
                        SimpleBrowserActivity.this.urlInput.setText(url);
                        return;
                    }
                    return;
                case HIDE_LOADING:
                    SimpleBrowserActivity.this.loading.setVisibility(4);
                    return;
                case SHOW_LOADING:
                    SimpleBrowserActivity.this.loading.setVisibility(0);
                    return;
                default:
                    return;
            }
        }
    };
    private ImageView removeUrl;
    private EditText urlInput;
    private SelfWebView webView;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    /* Access modifiers changed, original: protected */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().hide();
        this.webView = (SelfWebView) findViewById(R.id.webview);
        this.webView.setHandler(this.mHandler);
        this.urlInput = (EditText) findViewById(R.id.url_input);
        this.urlInput.addTextChangedListener(this);
        this.urlInput.setOnClickListener(this);
        this.goForward = (ImageView) findViewById(R.id.forward);
        this.goForward.setOnClickListener(this);
        this.removeUrl = (ImageView) findViewById(R.id.remove_url);
        this.removeUrl.setOnClickListener(this);
        this.loading = (FrameLayout) findViewById(R.id.loading);
        this.loading.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        this.imm = (InputMethodManager) getSystemService("input_method");
        this.urlInput.setText(this.blankUrl);
        this.goForward.callOnClick();
        urlInput.requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            Builder builder = new Builder(this);
            builder.setTitle("确定退出？");
            builder.setPositiveButton("是滴", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    SimpleBrowserActivity.this.finish();
                }
            });
            builder.setNegativeButton("不想", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            builder.create().show();
        }
        return true;
    }

    @SuppressLint("JavascriptInterface")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.url_input /*2131034172*/:
                this.webView.addJavascriptInterface(new SelfWebView.JavaScriptLocal(), "local_obj");
                this.webView.reload();
                this.webView.loadUrl("javascript:window.local_obj.log('1q2w3e');alert('222')");
                return;
            case R.id.remove_url /*2131034173*/:
                this.urlInput.setText("");
                this.imm.showSoftInput(this.urlInput, 2);
                return;
            case R.id.forward /*2131034174*/:
                String urlStr = this.urlInput.getText().toString();
                if (!(urlStr == null || urlStr.isEmpty())) {
                    this.mHandler.obtainMessage(0, urlStr).sendToTarget();
                }
                this.imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return;
            default:
                return;
        }
    }

    public void afterTextChanged(Editable edit) {
        if (edit.toString().isEmpty()) {
            this.removeUrl.setVisibility(4);
        } else {
            this.removeUrl.setVisibility(0);
        }
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
    }

}
