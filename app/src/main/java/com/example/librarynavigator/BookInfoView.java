package com.example.librarynavigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BookInfoView extends AppCompatActivity {
    private String url = "http://mlib.ajou.ac.kr/search/Search.Detail.ax?cid=";
    private String cid;
    private WebView bookInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info_view);

        Intent in = getIntent();
        cid = in.getStringExtra("cid");
        url = url+""+cid;
        bookInfoView = (WebView) findViewById(R.id.book_info_view);
        bookInfoView.setWebViewClient(new WebViewClient());
        bookInfoView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        bookInfoView.loadUrl(url);
    }
}
