package com.glg.mygif.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glg.mygif.adapter.FragmentViewPagerAdapter;
import com.glg.mygif.entity.Fruit;
import com.glg.mygif.adapter.FruitAdapter;
import com.glg.mygif.R;
import com.glg.mygif.fragment.ConsiderFragment;
import com.glg.mygif.fragment.HotFragment;
import com.glg.mygif.fragment.WorldFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//实现LoaderManager.LoaderCallbacks<Void>接口并调用reportFullyDrawn()方法是为了将启动Activity的时间在日志中打印出来，目测无效
public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Void> {

    public static int RESPONSE_CODE = 0x11;
    public static int RESPONSE_PUBLISH = 0x21;
    public static int REQUEST_CODE = 0x12;
    private DrawerLayout mDrawerLayout;
    public static Fruit[] fruits = {new Fruit("Apple", R.drawable.apple),new Fruit("Pear", R.drawable.pear),new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Mango", R.drawable.mango),new Fruit("Orange", R.drawable.orange),new Fruit("Grape", R.drawable.grape),
            new Fruit("Watermelon", R.drawable.watermelon),new Fruit("Cherry", R.drawable.cherry),new Fruit("Pineapple", R.drawable.pineapple),};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout edit_layout;
    private ImageView icon_image;
    private TextView username;
    private TextView edit_introduction;
    private NavigationView navigationView;
    private Bitmap bitmap;

    private long exitTime = 0;

    //private LinearLayout search_layout;

    //private EditText search_text;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示状态栏

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //search_layout = findViewById(R.id.search_layout);
        //search_text = findViewById(R.id.search_text);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.main);
        }
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, PublishActivity.class), REQUEST_CODE);
            }
        });

//        initFruits(fruitList);
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new FruitAdapter(fruitList);
//        recyclerView.setAdapter(adapter);
//
//        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshFruits(swipeRefreshLayout, fruitList, adapter);
//            }
//        });

        viewPager = findViewById(R.id.viewPager);
        initFragments();
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentViewPagerAdapter);
        viewPager.setCurrentItem(0);

        tabLayout = findViewById(R.id.tab);
        tabLayout.getTabAt(0).select();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_publish:
                        startActivity(new Intent(MainActivity.this, PublishActivity.class));
                        break;
                    case R.id.nav_personal:
                        Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username.getText().toString());
                        bundle.putString("introduction", edit_introduction.getText().toString());
                        bundle.putParcelable("bitmap", bitmap);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.nav_recommend:
                    case R.id.nav_box:
                    case R.id.nav_settings:
                    default:
                        mDrawerLayout.closeDrawers();
                }
                return false;
            }
        });

        username = navigationView.getHeaderView(0).findViewById(R.id.username);
        edit_introduction = navigationView.getHeaderView(0).findViewById(R.id.edit_introduction);
        icon_image = navigationView.getHeaderView(0).findViewById(R.id.icon_image);

        edit_layout = navigationView.getHeaderView(0).findViewById(R.id.edit_layout);
        edit_layout.setClickable(true);
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditPersonalIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username.getText().toString());
                bundle.putString("introduction", edit_introduction.getText().toString());
                bundle.putParcelable("bitmap", bitmap);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0x12);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESPONSE_CODE) {
            Bundle bundle = data.getExtras();
            if(!bundle.getString("name").equals("")) {
                username.setText(bundle.getString("name"));
            }
            if(!bundle.getString("introduction").equals("")) {
                edit_introduction.setText("简介：" + bundle.getString("introduction"));
            }
            bitmap = bundle.getParcelable("photo");
            if(bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                icon_image.setImageDrawable(drawable);
            }
        }
    }

    private void refreshFruits(final SwipeRefreshLayout swipeRefreshLayout, final List<Fruit> fruitList, final FruitAdapter adapter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits(fruitList);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
//                toolbar.setVisibility(View.GONE);
//                search_layout.setVisibility(View.VISIBLE);
//                search_text.setFocusable(true);
//                search_text.requestFocus();
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(search_text, 0);
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
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

    public static void initFruits(List<Fruit> fruitList) {
        fruitList.clear();
        for(int i = 0; i < 50; i += 1) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    private void initFragments() {
        WorldFragment worldFragment = new WorldFragment(this);
        fragments.add(worldFragment);
        ConsiderFragment considerFragment = new ConsiderFragment();
        fragments.add(considerFragment);
        HotFragment hotFragment = new HotFragment();
        fragments.add(hotFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出应用程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @NonNull
    @Override
    public Loader<Void> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void data) {
        reportFullyDrawn();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Void> loader) {

    }
}
