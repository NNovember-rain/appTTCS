package vn.mrlongg71.ps09103_assignment.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import vn.mrlongg71.ps09103_assignment.R;

public class ShowRssActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rss);
        webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String link =intent.getStringExtra("linkNews");
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}
