package com.glg.mygif.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.ChangeBounds;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.glg.mygif.R;
import com.glg.mygif.utils.StatusBarUtil;

public class VideoActivity extends BaseActivity {

    private VideoView videoView;
    private ImageView videoPause;
    private CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            videoPause.setVisibility(View.GONE);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        StatusBarUtil.setStatusBarColor(this, R.color.black);
        getWindow().setNavigationBarColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                countDownTimer.start();
                videoPause.setVisibility(View.VISIBLE);
            }
        });
        videoView.start();
        videoPause = findViewById(R.id.video_close);
        videoPause.setVisibility(View.VISIBLE);
        videoPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()) {
                    videoView.pause();
                    videoPause.setImageResource(R.drawable.play);
                } else {
                    videoPause.setImageResource(R.drawable.pause);
                    videoView.start();
                }
            }
        });
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