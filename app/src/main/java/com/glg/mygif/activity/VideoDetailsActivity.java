package com.glg.mygif.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.glg.mygif.R;
import com.glg.mygif.databinding.ActivityVideoDetailsBinding;
import com.glg.mygif.view.ZoomImageView;

import java.util.concurrent.ExecutionException;

public class VideoDetailsActivity extends BaseActivity {

    private ZoomImageView zoomImageView;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityVideoDetailsBinding activityVideoDetailsBinding = DataBindingUtil.setContentView(VideoDetailsActivity.this, R.layout.activity_video_details);
        //setContentView(R.layout.activity_video_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final String coverUrl = bundle.getString("coverUrl");
        final String name = bundle.getString("name");
        final int position = bundle.getInt("position");
        //zoomImageView = findViewById(R.id.zoomImageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = Glide.with(VideoDetailsActivity.this).asBitmap().load(coverUrl).centerCrop().into(1920, 1080).get();
//                    activityVideoDetailsBinding.zoomImageView.setImageBitmap(bitmap);
//                    activityVideoDetailsBinding.imageContent.setText(name);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activityVideoDetailsBinding.zoomImageView.setImageBitmap(bitmap);
                            activityVideoDetailsBinding.imageContent.setText(name);
                        }
                    });
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
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(300);
        //排除状态栏
        changeBounds.excludeTarget(android.R.id.statusBarBackground, true);
        //是否同时执行
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        //进入
        getWindow().setEnterTransition(changeBounds);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}