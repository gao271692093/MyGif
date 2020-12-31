package com.glg.mygif.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.glg.mygif.R;
import com.glg.mygif.utils.StatusBarUtil;
import com.glg.mygif.view.ZoomImageView;
import com.gyf.immersionbar.ImmersionBar;

import java.util.concurrent.ExecutionException;

public class ImageDetailsActivity extends BaseActivity {

    private ZoomImageView zoomImageView;

    private WebView webView;

    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //ImmersionBar.with(this).fullScreen(true).fitsSystemWindows(true).init();

        //StatusBarUtil.setStatusBarColor(this, R.color.launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String copyrightLink = bundle.getString("copyrightLink");
        final String url = bundle.getString("url");
        zoomImageView = findViewById(R.id.zoomImageView);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            //防止有 URL Scheme 跳转协议类型的url 导致webView加载网页失败
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url == null) {
                    return false;
                }
                if(url.startsWith("http:")||url.startsWith("https:")){
                    view.loadUrl(url);
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getApplicationContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }

            // 在默认情况下，通过loadUrl(String url)方法，可以顺利load。
            // 但是，当load有ssl层的https页面时，如果这个网站的安全证书在Android无法得到认证，WebView就会变成一个空白页，
            // 而并不会像PC浏览器中那样跳出一个风险提示框。因此，我们必须针对这种情况进行处理。(这个证书限于2.1版本以上的Android 系统才可以)
            // 默认的处理方式，WebView变成空白页
            // handler.cancel();
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        if(copyrightLink != null) {
            webView.loadUrl("http://cn.bing.com/" + copyrightLink);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = Glide.with(ImageDetailsActivity.this).asBitmap().load(url).into(1920, 1080).get();
                    zoomImageView.setImageBitmap(bitmap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        //爆炸效果进入进出
        Explode explodeTransition = new Explode();
        explodeTransition.setDuration(300);
        //排除状态栏
        explodeTransition.excludeTarget(android.R.id.statusBarBackground, true);
        //是否同时执行
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        //进入
        getWindow().setEnterTransition(explodeTransition);
    }
}