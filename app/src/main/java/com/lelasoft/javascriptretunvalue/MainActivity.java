package com.lelasoft.javascriptretunvalue;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            webView.addJavascriptInterface(new JavaScriptInterface(), "javascriptinterface");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                saveProcess(Math.random());
            }
        });
        webView.loadUrl("file:///android_asset/test.html");
    }

    private void saveProcess(double value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:process_save('" + value + "');",new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    // value is the result returned by the Javascript as JSON
                    // Receive newpid here
                    Log.d("JS",value);
                }
            });
        } else {
            webView.loadUrl("javascript:javascriptinterface.callback(process_save('" + value + "');");
        }
    }



    private class JavaScriptInterface {
        @JavascriptInterface
        public void callback(String value) {
            Log.d("JS",value);
        }
    }
}
