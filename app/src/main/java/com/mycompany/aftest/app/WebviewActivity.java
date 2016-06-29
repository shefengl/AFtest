package com.mycompany.aftest.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mycompany.aftest.R;

/**
 * Created by shefeng on 6/28/2016.
 */
public class WebviewActivity extends Activity {
    private WebView webView;
    private ProgressDialog pDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webView1);
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!pDialog.isShowing()) {
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pDialog.isShowing()) {
                    pDialog.hide();
                }
            }
        });

        webView.loadUrl(url);

//        setContentView(webView);

    }

}
