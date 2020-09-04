package com.glg.mygif.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekwing.dataparser.json.CommonJsonBuilder;
import com.ekwing.http.common.HttpProxy;
import com.ekwing.http.common.interfaces.CallBack;
import com.glg.mygif.activity.ImageDetailsActivity;
import com.glg.mygif.entity.ImageItem;
import com.glg.mygif.entity.Images;
import com.glg.mygif.adapter.ImagesAdapter;
import com.glg.mygif.R;
import com.glg.mygif.lifecycle.MyLifecycleObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: HotFragment
 * @author: gao
 * @date: 2020/8/4 18:27
 */
public class HotFragment extends Fragment {

    private String URL = "http://bing.getlove.cn/latelyBingImageStory";

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static Map<String, String> maps = new HashMap<>();

    private Images gifData;

    private List<ImageItem> list = new ArrayList<ImageItem>();

    private List<ImageItem> adapter_list = new ArrayList<>();

    private ImagesAdapter imagesAdapter = new ImagesAdapter(this, list);

    private int notify_start = 0;
//
//    private int notify_count = 10;

    private CallBack callBack = new CallBack() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String result) {
            list.clear();
            list = CommonJsonBuilder.toObjectArray(result, ImageItem.class);
//            String [] strings = result.split("\\},");
//            strings[0] = strings[0].substring(1,strings[0].length()).concat("}");
//            strings[strings.length - 1] = strings[strings.length - 1].substring(0,strings[strings.length - 1].length() - 1);
//            for(int i = 0; i < strings.length; i += 1) {
//                if(i > 0 && i < strings.length - 1) {
//                    strings[i] = strings[i].concat("}");
//                }
//                list.add(CommonJsonBuilder.toObject(strings[i], ImageItem.class));
//            }
            adapter_list.addAll(list);
            imagesAdapter.setList(adapter_list);
            if(swipeRefreshLayout.isRefreshing()) {
                imagesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                imagesAdapter.notifyItemRangeInserted(notify_start, list.size());
            }
            notify_start = notify_start  + list.size();
//            notify_start = 0;
//            if(list.size() <= notify_count) {
//                imagesAdapter.notifyDataSetChanged();
//                notify_start = list.size();
//            } else {
//                imagesAdapter.notifyItemRangeChanged(notify_start, notify_count);
//                notify_start = notify_start + notify_count;
//            }
            //recyclerView.setAdapter(imagesAdapter);
            //Toast.makeText(getActivity(), "请求成功" + recyclerView.getChildCount(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCacheSuccess(String result) {

        }

        @Override
        public void onError(int code, Throwable throwable) {
            Toast.makeText(getActivity(), "请求失败" + code + throwable.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getLifecycle().addObserver(new MyLifecycleObserver(this,"HotFragment"));
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_layout, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        imagesAdapter.setOnItemClickListener(new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageItem imageItem = list.get(position);
                Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("copyrightLink", imageItem.getCopyrightLink());
                bundle.putString("url", "https://cn.bing.com/" + imageItem.getUrl());
                intent.putExtras(bundle);
                //startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView = view.findViewById(R.id.hot_recyclerView);
        recyclerView.setAdapter(imagesAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    recyclerView.stopScroll();
                    HttpProxy.getInstance().get(URL,null, null, false, callBack);
                }
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if(recyclerView.isComputingLayout()) {
//                        recyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(notify_start < list.size()) {
//                                    if(list.size() <= (notify_count + notify_start)) {
//                                        imagesAdapter.notifyItemRangeChanged(notify_start, list.size() - notify_start);
//                                        notify_start = list.size();
//                                    } else {
//                                        imagesAdapter.notifyItemRangeChanged(notify_start, notify_count);
//                                        notify_start = notify_start + notify_count;
//                                    }
//                                }
//                            }
//                        });
//                    } else {
//                        if(notify_start < list.size()) {
//                            if(list.size() <= (notify_count + notify_start)) {
//                                imagesAdapter.notifyItemRangeChanged(notify_start, list.size() - notify_start);
//                                notify_start = list.size();
//                            } else {
//                                imagesAdapter.notifyItemRangeChanged(notify_start, notify_count);
//                                notify_start = notify_start + notify_count;
//                            }
//                        }
//                    }
//                }
//            }
//        });

//        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(recyclerView.isComputingLayout()) {
//                    recyclerView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(notify_start < list.size()) {
//                                if(list.size() <= (notify_count + notify_start)) {
//                                    imagesAdapter.notifyItemRangeChanged(notify_start, list.size() - notify_start);
//                                    notify_start = list.size();
//                                } else {
//                                    imagesAdapter.notifyItemRangeChanged(notify_start, notify_count);
//                                    notify_start = notify_start + notify_count;
//                                }
//                            }
//                        }
//                    });
//                } else {
//                    if(notify_start < list.size()) {
//                        if(list.size() <= (notify_count + notify_start)) {
//                            imagesAdapter.notifyItemRangeChanged(notify_start, list.size() - notify_start);
//                            notify_start = list.size();
//                        } else {
//                            imagesAdapter.notifyItemRangeChanged(notify_start, notify_count);
//                            notify_start = notify_start + notify_count;
//                        }
//                    }
//                }
//            }
//        });
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//滑动过程中item不移动
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);//设置固定大小
        //recyclerView.setNestedScrollingEnabled(false);//设置不支持滑动嵌套
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        ((SimpleItemAnimator)itemAnimator).setSupportsChangeAnimations(false);//recyclerView.setItemAnimator(null);

        swipeRefreshLayout = view.findViewById(R.id.hot_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //recyclerView.scrollToPosition(0);
                notify_start = 0;
                adapter_list.clear();
                HttpProxy.getInstance().get(URL,null, null, false, callBack);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        HttpProxy.getInstance().get(URL,null, null, false, callBack);
//    }

    public CallBack getCallBack() {
        return callBack;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setDuration(300);
        //一起动画
        transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        transitionSet.addTransition(slideTransition);
        Fade fadeTransition = new Fade();
        transitionSet.addTransition(fadeTransition);
        //排除状态栏
        transitionSet.excludeTarget(android.R.id.statusBarBackground, true);
        //是否同时执行
        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
        getActivity().getWindow().setAllowReturnTransitionOverlap(false);
        //退出这个界面
        getActivity().getWindow().setExitTransition(transitionSet);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().setExitTransition(null);
    }
}
