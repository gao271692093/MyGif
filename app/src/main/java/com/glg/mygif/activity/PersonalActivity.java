package com.glg.mygif.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.glg.mygif.R;
import com.glg.mygif.entity.EventPersonal;
import com.glg.mygif.utils.StatusBarUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.glg.mygif.activity.MainActivity.REQUEST_CODE;
import static com.glg.mygif.activity.MainActivity.RESPONSE_CODE;

public class PersonalActivity extends BaseActivity {

    private String username;
    private String introduction;
    private Bitmap bitmap;
    private ImageView background;
    private ImageView icon;
    private TextView user_name;
    private TextView user_introduction;
    private ImageView userBgImage;
    private FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        //StatusBarUtil.setStatusBarColor(this, R.color.launcher);

        EventBus.getDefault().register(this);
        EventPersonal eventPersonal = new EventPersonal();
        eventPersonal.setIntent(getIntent());
        EventBus.getDefault().post(eventPersonal);
        background = findViewById(R.id.personal_image_view);
        userBgImage = findViewById(R.id.userBgImage);
        icon = findViewById(R.id.userAvatar);
        user_name = findViewById(R.id.userNickname);
        user_introduction = findViewById(R.id.userDescription);
        floatingActionButton = findViewById(R.id.fab);
        initData();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(PersonalActivity.this, EditPersonalIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("introduction", user_introduction.getText().toString());
                bundle.putParcelable("bitmap", bitmap);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESPONSE_CODE) {
            Bundle bundle = data.getExtras();
            username = bundle.getString("name");
            introduction = bundle.getString("introduction");
            bitmap = bundle.getParcelable("photo");
        }
        initData();
    }

    private void initData() {
        if(username != null && !username.equals("")) {
            user_name.setText(username);
        }
        if(introduction != null && !introduction.equals("")) {
            if(introduction.equals("编辑个人简介")) {
                user_introduction.setText("这个人很懒，什么都没有留下");
            } else {
                user_introduction.setText("简介：" + introduction);
            }
        }
        if(bitmap != null) {
            background.setImageBitmap(bitmap);
            RequestBuilder<Drawable> apply = Glide.with(PersonalActivity.this).load(bitmap);
            apply.into(background);
            apply.into(userBgImage);
            apply.apply(RequestOptions.bitmapTransform(new CircleCrop())).into(icon);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventPersonal event) {
        Intent intent = event.getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        introduction = bundle.getString("introduction");
        bitmap = bundle.getParcelable("bitmap");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}