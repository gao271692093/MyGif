package com.glg.mygif.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glg.mygif.R;

import java.io.File;

public class EditPersonalIntroductionActivity extends BaseActivity implements View.OnClickListener {

    private String Tag = "EditPersonalIntroductionActivity";
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private static final int IMAGE_REQUEST_CODE = 0x10;
    private static final int CAMERA_REQUEST_CODE = 0x11;
    private static final int RESULT_REQUEST_CODE = 0x12;
    private ImageView icon_image;
    private ImageView imageView;
    private FrameLayout photoLayout;
    private TextView name;
    private EditText edit_name;
    private EditText edit_introduction;
    private TextView text_name;
    private TextView text_introduction;
    private TextView save;
    private TextView introduction;
    private Bitmap bitmap;
    public boolean flag = false;
    private String username;
    private String introductions;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_introduction);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        icon_image = findViewById(R.id.icon_image);
        imageView = findViewById(R.id.photo);
        imageView.setClickable(true);
        imageView.setOnClickListener(this);
        photoLayout = findViewById(R.id.photo_layout);
        photoLayout.setOnClickListener(this);
        name = findViewById(R.id.name);
        text_name = findViewById(R.id.text_name);
        text_introduction = findViewById(R.id.text_introduction);
        edit_name = findViewById(R.id.edit_name);
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_name.setSelection(s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= 12) {
                    text_name.setText(s.length() + "/12");
                } else {
                    edit_name.setText(s.subSequence(0, 12));
                    edit_name.setSelection(12);
                    text_name.setText("12/12");
                }
                if(s.equals("") || s == null) {
                    name.setText("");
                } else {
                    name.setText(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        introduction = findViewById(R.id.introduction);
        edit_introduction = findViewById(R.id.edit_introduction);
        edit_introduction.requestFocus();
        edit_introduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_introduction.setSelection(s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= 60) {
                    text_introduction.setText(s.length() + "/60");
                } else {
                    edit_introduction.setText(s.subSequence(0, 60));
                    edit_introduction.setSelection(60);
                    text_introduction.setText("60/60");
                }
                if(s.equals("") || s == null) {
                    introduction.setText("");
                } else {
                    introduction.setText("简介：" + s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("name", edit_name.getText().toString());
                bundle.putString("introduction", edit_introduction.getText().toString());
                bundle.putParcelable("photo", bitmap);
                intent.putExtras(bundle);
                setResult(MainActivity.RESPONSE_CODE, intent);
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        introductions = bundle.getString("introduction");
        bitmap = bundle.getParcelable("bitmap");
        name.setText(username);
        if(username.equals("未填写")) {
            edit_name.setHint("请填写您的昵称");
        } else {
            edit_name.setText(username);
        }
        if(bitmap != null) {
            icon_image.setImageBitmap(bitmap);
        }
        if(introductions.equals("编辑个人简介") || introductions.equals("这个人很懒，什么都没有留下")) {
            edit_introduction.setText("");
            introduction.setText("");
            introductions = "";
        } else {
            edit_introduction.setText(introductions.substring(3));
            edit_introduction.setSelection(introductions.substring(3).length());
        }
    }

    @Override
    public void onClick(View v) {
        final String items[] = {"拍照", "你的相册"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("选择个人封面照片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("拍照")) {
                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //方案一
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    builder.detectFileUriExposure();

                    //方案二，待修正
//                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(EditPersonalIntroductionActivity.this, new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    }
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    }
                    startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                }
                if(items[which].equals("你的相册")) {
                    Intent intentFromGallery = new Intent();
                    intentFromGallery.setType("image/*"); // 设置文件类型
                    intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            }
                        }
                        File tempFile = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(EditPersonalIntroductionActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
                default:
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(username.equals(edit_name.getText().toString()) && introductions.equals(edit_introduction.getText().toString()) && flag == false) {
                    finish();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(EditPersonalIntroductionActivity.this).create();
                    alertDialog.setMessage("您已修改了个人信息，确定放弃保存吗？");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "留下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.show();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            bitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(bitmap);
            icon_image.setImageDrawable(drawable);
            flag = true;
        }
    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "MyGif.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public boolean isFlag() {
        return flag;
    }
}