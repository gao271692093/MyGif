package com.glg.mygif.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekwing.dataparser.json.CommonJsonBuilder;
import com.ekwing.http.common.HttpProxy;
import com.ekwing.http.common.interfaces.CallBack;
import com.glg.mygif.activity.VideoActivity;
import com.glg.mygif.adapter.VideoNewAdapter;
import com.glg.mygif.adapter.ImagesAdapter;
import com.glg.mygif.R;
import com.glg.mygif.entity.VideoEntity;
import com.glg.mygif.lifecycle.MyLifecycleObserver;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import static com.glg.mygif.utils.DensityUtil.dp2px;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: WorldFragment
 * @author: gao
 * @date: 2020/8/4 18:05
 */
public class WorldFragment extends Fragment {

    private int count = 10;

    private int page = 1;

    private String URL = "https://api.apiopen.top/getJoke?page=";

    private Activity activity;

    private VideoEntity videoEntity;

    private List<VideoEntity.ResultBean> resultBeanList = new ArrayList<>();

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private VideoNewAdapter videoAdapter;

    private int notify_start = 0;

    private int lastVisibleItem[];

    private CallBack callBack = new CallBack() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String result) {
            videoEntity = CommonJsonBuilder.toObject(result, VideoEntity.class);
            resultBeanList = videoEntity.getResult();
            videoAdapter.setResultBeanList(resultBeanList);
            if(swipeRefreshLayout.isRefreshing()) {
                videoAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                videoAdapter.notifyItemRangeInserted(notify_start, 10);
                notify_start += 10;
            }
            //Toast.makeText(getActivity(), "请求成功" + recommendBean.getDefaultGrade() + (recommendBean.getList() == null), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCacheSuccess(String result) {

        }

        @Override
        public void onError(int code, Throwable throwable) {
            Toast.makeText(getActivity(), "请求失败" + code, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {

        }
    };

    public WorldFragment() {
    }

    public WorldFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoAdapter = new VideoNewAdapter(this, resultBeanList, getImageWidth());
        getLifecycle().addObserver(new MyLifecycleObserver(this, "WorldFragment"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_layout, container, false);
        recyclerView = view.findViewById(R.id.world_recyclerView);
        recyclerView.setAdapter(videoAdapter);
        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        videoAdapter.setOnItemClickListener(new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VideoEntity.ResultBean resultBean = resultBeanList.get(position);
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", resultBean.getName());
                bundle.putString("url", resultBean.getVideo());
                bundle.putString("header", resultBean.getHeader());
                bundle.putString("like_count", resultBean.getUp());
                bundle.putString("image", resultBean.getThumbnail());
                intent.putExtras(bundle);
                //startActivity(intent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                } else {
//                    startActivity(intent);
//                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, videoAdapter.getBundle());
                } else {
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                if(lastVisibleItem == null) {
                    lastVisibleItem = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastVisibleItem);
                int lastVisiblePosition = findMax(lastVisibleItem);
                int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                int totalItemCount = staggeredGridLayoutManager.getItemCount();
                Log.d("测试", lastVisiblePosition + "============" + totalItemCount);

                if (lastVisiblePosition == totalItemCount - 1) {
                    recyclerView.stopScroll();
                    HttpProxy.getInstance().get(URL + page + "&count=" + (count += 10) + "&type=video",null, null, false, callBack);
                }
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.world_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                HttpProxy.getInstance().post(URL + ++page + "&count=" + count + "&type=video", null, null, false, callBack);
            }
        });
        HttpProxy.getInstance().post(URL + page + "&count=" + count + "&type=video", null, null, false, callBack);
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        HttpProxy.getInstance().post(URL, null, maps, false, callBack);
//    }

    public CallBack getCallBack() {
        return callBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
//    private List<Fruit> fruitList = new ArrayList<>();
//
//    private RecyclerView recyclerView;
//
//    private SwipeRefreshLayout swipeRefreshLayout;
//
//    private FruitAdapter adapter;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.world_layout, container, false);
//        MainActivity.initFruits(fruitList);
//        recyclerView = view.findViewById(R.id.world_recyclerView);
//        swipeRefreshLayout = view.findViewById(R.id.world_refresh);
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new FruitAdapter(fruitList);
//        recyclerView.setAdapter(adapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                MainActivity.initFruits(fruitList);
//                                adapter.notifyDataSetChanged();
//                                swipeRefreshLayout.setRefreshing(false);
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        return view;
//    }

    public void limitScroll() {
//        int firstCompletelyVisibleItemPosition;
//        int lastCompletelyVisibleItemPosition;
//        int totalItemCount;
//        LinearLayoutManager linearLayoutManager;
//        View appBarChildAt;
//        AppBarLayout.LayoutParams appBarParams;
//        firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//        lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//        totalItemCount = linearLayoutManager.getItemCount();
//
//        Log.d("测试", firstCompletelyVisibleItemPosition + "===============" + lastCompletelyVisibleItemPosition + "=========" + totalItemCount);
//
//        if(firstCompletelyVisibleItemPosition == 0 && lastCompletelyVisibleItemPosition == totalItemCount - 1) {
//            Log.d("测试限制AppBar滑动", firstCompletelyVisibleItemPosition + "===============" + lastCompletelyVisibleItemPosition + "=========" + totalItemCount);
//            appBarChildAt = appbar.getChildAt(0);
//            appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
//            appBarParams.setScrollFlags(0);
//            appBarChildAt.setLayoutParams(appBarParams);
//        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    private int getImageWidth() {
        WindowManager windowManager = (WindowManager)(activity.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics matrix = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(matrix);
        int columnWidth = matrix.widthPixels / 2;
        return columnWidth - dp2px(10f);
    }
}
