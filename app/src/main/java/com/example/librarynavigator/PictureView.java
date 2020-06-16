package com.example.librarynavigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class PictureView extends Activity {
    private String html = "<html> <head><meta charset=\"UTF-8\"</head> "
            +" <body>  <img src=\"http://git.ajou.ac.kr/mingi/libraryresources/raw/master/%s\"" +
            " style=\"max-width: 100%; height: auto;\"> "
                    +"</body> </html>";
    private WebView mWebView;
    private int imgurlIdx = 0;
    private ArrayList<String> imgurls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_view);

        Intent in = getIntent();
        imgurls = in.getStringArrayListExtra("visit_path");
        imgurlIdx = in.getIntExtra("imgurlIdx", 0);

        mWebView = (WebView)findViewById(R.id.mWebView);
        mWebView.setWebViewClient(new WebViewClient());
        String data = html.replace("%s", imgurls.get(imgurlIdx));
        Log.d("mingidev", "imgurls:"+imgurls.get(imgurlIdx));
        mWebView.loadData(data, "text/html", "UTF-8");
    }

    public void onClickNext(View v) {
        if (imgurlIdx + 1 < imgurls.size()) {
            imgurlIdx += 1;

            String data = html.replace("%s", imgurls.get(imgurlIdx));
            Log.d("mingidev", "imgurls:"+imgurls.get(imgurlIdx));
            mWebView.loadData(data, "text/html", "UTF-8");
            Intent in = getIntent();
            in.putExtra("imgurlIdx", imgurlIdx);
            in.putStringArrayListExtra("visit_path", imgurls);
            finish();
            startActivity(in);
        }
    }

    public void onClickPrev(View v) {
        if (imgurlIdx - 1 >= 0) {
            imgurlIdx -= 1;

            String data = html.replace("%s", imgurls.get(imgurlIdx));
            Log.d("mingidev", "imgurls:"+imgurls.get(imgurlIdx));
            mWebView.loadData(data, "text/html", "UTF-8");
            Intent in = getIntent();
            in.putExtra("imgurlIdx", imgurlIdx);
            in.putStringArrayListExtra("visit_path", imgurls);
            finish();
            startActivity(in);
        }

    }
}
