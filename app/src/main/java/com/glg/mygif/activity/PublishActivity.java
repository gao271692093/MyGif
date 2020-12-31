package com.glg.mygif.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.glg.mygif.BuildConfig;
import com.glg.mygif.R;
import com.glg.mygif.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class PublishActivity extends BaseActivity {

    private Toolbar toolbar;

    private ImageView addPicture;

    private EditText publish_editText;

    private Bitmap bitmap;

    private static final String IMAGE_FILE_NAME = "publish_image.jpg";

    private static final int IMAGE_REQUEST_CODE = 0x10;
    private static final int CAMERA_REQUEST_CODE = 0x11;
    private static final int RESULT_REQUEST_CODE = 0x12;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        toolbar = findViewById(R.id.publish_toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FBFBFB"));
        toolbar.setTitle("发布趣图");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPicture = findViewById(R.id.add_picture);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = {"从相册获取", "拍照"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(items[which].equals("从相册获取")) {
                            Intent intentFromGallery = new Intent();
                            if (Build.VERSION.SDK_INT < 19) {
                                intentFromGallery = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                intentFromGallery.setType("image/*");
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                            } else {

                                intentFromGallery = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            }
                            startActivityForResult(intentFromGallery,
                                    IMAGE_REQUEST_CODE);
                        }
                        if(items[which].equals("拍照")) {
                            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 判断存储卡是否可以用，可用进行存储
                            if (Tools.hasSdcard()) {
                                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(PublishActivity.this,new File(Environment.getExternalStorageDirectory()+"/" + IMAGE_FILE_NAME)));
                            }
                            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                            }
                            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                        }
                    }
                });
                builder.show();
            }
        });
        publish_editText = findViewById(R.id.publish_editText);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.publish:
                if (!publish_editText.getText().toString().equals("") && bitmap != null) {
                    Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bitmap", bitmap);
                    bundle.putString("content", publish_editText.getText().toString());
                    intent.putExtras(bundle);
                    setResult(MainActivity.RESPONSE_PUBLISH);
                    finish();
                } else {
                    if(publish_editText.getText().toString().equals("")) {
                        Toast.makeText(this, "发表内容不能为空呦", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "请添加一张您想发表的图片", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(@Nullable View view, @NonNull Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    //startPhotoZoom(data.getData());
                    Uri uri01 = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri01));
                        addPicture.setImageBitmap(bitmap);
                        this.bitmap = bitmap;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                            if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            }
                        }
                        File tempFile = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                        //startPhotoZoom(getUriForFile(this, tempFile));
                        try {
                            Uri uri02 = getUriForFile(this, tempFile);   //图片文件
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri02));
                            addPicture.setImageBitmap(bitmap);
                            this.bitmap = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("square", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenWidth = displayMetrics.widthPixels;
//        int screenHeight = displayMetrics.heightPixels;

        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);

        //解决图片无法加载的问题
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            addPicture.setImageBitmap(bitmap);
            this.bitmap = bitmap;
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
}