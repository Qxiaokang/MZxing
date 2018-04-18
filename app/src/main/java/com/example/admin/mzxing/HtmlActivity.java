package com.example.admin.mzxing;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mzxing.bean.Const;
import com.example.admin.mzxing.utils.ComUtil;
import com.example.admin.mzxing.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HtmlActivity extends AppCompatActivity {

    private String result;
    private ProgressBar pb;
    private TextView tvTitle;
    private String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        url=this.getFilesDir().getAbsolutePath()+File.separator+ Const.QRDIR+File.separator;
        initData();
        initView();
    }

    private void initView() {
        WebView webView=findViewById(R.id.web_result);
        pb=findViewById(R.id.prg);
        tvTitle=findViewById(R.id.tv_result);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(viewClient);
        webView.setWebChromeClient(chromeClient);
        webView.loadDataWithBaseURL("file:///android_asset",result,"text/html","utf-8",null);
    }
    WebChromeClient chromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            LogUtils.i("new progress:"+newProgress);
            pb.setProgress(newProgress);
            if(newProgress==100){
                pb.setVisibility(View.GONE);
            }
        }
    };
    WebViewClient viewClient=new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.i("start------");
            pb.setProgress(0);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            LogUtils.i("end--------"+"title:"+title);
            tvTitle.setText(title==null?"":title);
        }
        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            return super.onRenderProcessGone(view, detail);
        }
    };
    private void initData() {
        Bundle bundle=getIntent().getExtras();
        result = bundle.getString("value");
        LogUtils.i("value:"+result);
    }
}
