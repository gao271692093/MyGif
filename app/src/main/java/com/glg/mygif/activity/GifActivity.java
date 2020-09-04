package com.glg.mygif.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.glg.mygif.utils.DownloadUtils;
import com.glg.mygif.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends BaseActivity {

    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("gifUrl");
        gifImageView = findViewById(R.id.gifImageView);
        //1. 下载gif图片(文件名自定义可以通过Hash值作为key)
        DownloadUtils downloadUtils = new DownloadUtils();
        downloadUtils.download(url, getDiskCacheDir(this)+"/0.gif");
        //2. 下载完毕后通过“GifDrawable”进行显示
        downloadUtils.setOnDownloadListener(new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadUpdate(int percent) {
            }
            @Override
            public void onDownloadError(Exception e) {
            }
            @Override
            public void onDownloadConnect(int filesize) {
            }
            //下载完毕后进行显示
            @Override
            public void onDownloadComplete(Object result) {
                try {
                    GifDrawable gifDrawable = new GifDrawable(getDiskCacheDir(GifActivity.this)+"/0.gif");
                    gifImageView.setImageDrawable(gifDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取缓存的路径
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 路径：/storage/emulated/0/Android/data/<application package>/cache
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            // 路径：/data/data/<application package>/cache
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}