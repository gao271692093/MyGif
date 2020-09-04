package com.glg.mygif.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekwing.dataparser.json.CommonJsonBuilder;
import com.ekwing.http.common.HttpProxy;
import com.ekwing.http.common.interfaces.CallBack;
import com.glg.mygif.entity.Data;
import com.glg.mygif.adapter.ImagesAdapter;
import com.glg.mygif.activity.MainActivity;
import com.glg.mygif.R;
import com.glg.mygif.entity.RecommendBean;
import com.glg.mygif.entity.RecommendEntity;
import com.glg.mygif.adapter.VideoAdapter;
import com.glg.mygif.activity.VideoDetailsActivity;
import com.glg.mygif.lifecycle.MyLifecycleObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: WorldFragment
 * @author: gao
 * @date: 2020/8/4 18:05
 */
public class WorldFragment extends Fragment {

    private String URL = MainActivity.BASE_URL + "teacher/" + "index/recommend";

    private RecommendBean recommendBean;

    private Data data;

    private List<RecommendEntity> multipleItems;

    private List<RecommendEntity.ListBean> list = new ArrayList<>();

    private List<RecommendEntity.ListBean> listBeanList = new ArrayList<>();

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static Map<String, String> maps = new HashMap<>();

    private VideoAdapter videoAdapter = new VideoAdapter(this, list);

    private CallBack callBack = new CallBack() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String result) {
            if(!result.contains("error_msg")) {
                data = CommonJsonBuilder.toObject(result, Data.class);
                recommendBean = data.getData();
                multipleItems = recommendBean.getList();
                //list.clear();
                int i = 0, j = 0;
                for(i = 0;i < multipleItems.size(); i += 1) {
                    listBeanList = multipleItems.get(i).getList();
                    for(j = 0; j < listBeanList.size(); j += 1) {
                        list.add(listBeanList.get(j));
                    }
                }
                videoAdapter.setItemList(list);
                videoAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                RecommendEntity.ListBean listBean = list.get(position);
                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("coverUrl", listBean.getCoverurl());
                bundle.putString("name", listBean.getName());
                bundle.putInt("position", position);
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
//        recyclerView = view.findViewById(R.id.world_recyclerView);
//        recyclerView.setAdapter(videoAdapter);

        swipeRefreshLayout = view.findViewById(R.id.world_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpProxy.getInstance().post(URL, null, maps, false, callBack);
            }
        });
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

    static {
        maps.put("uid", "201817110");
        maps.put("token", "OMjAxODE3MTEwIyMwMTAyNzQ1NDI3IyM4NDg1M2U2OWZhMmM2MWVhMjhhMDU2YjMzODdlNTFjZiMjNDVhZGVmOTI0YzJhNGYzYjA4NzBiOGUyYzIwNjMyNzAjIzE2MDE2MjM4MDQjIzIjIzEjI2Vrd190ZWFjaGVyh");
        maps.put("v", "3.0");
        maps.put("driverCode", "2.0.1");
        maps.put("os", "android");
        maps.put("osv", "10");
    }
}
