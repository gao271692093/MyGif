package com.glg.mygif.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.ekwing.http.common.HttpProxy;
import com.glg.mygif.activity.MainActivity;
import com.glg.mygif.fragment.HotFragment;
import com.glg.mygif.fragment.WorldFragment;

/**
 * Description:
 *
 * @package: com.glg.mygif.lifecycle
 * @className: MyLifecycleObserver
 * @author: gao
 * @date: 2020/8/31 14:41
 */
public class MyLifecycleObserver implements LifecycleObserver {

    private String TAG;

    private Object object;

    public MyLifecycleObserver(Object object, String TAG) {
        this.object = object;
        this.TAG = TAG;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(){
        Log.i(TAG,"onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){
        Log.i(TAG,"onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
//        if(TAG.equals("HotFragment")) {
//            HttpProxy.getInstance().get("http://bing.getlove.cn/latelyBingImageStory",null, null, false, ((HotFragment)object).getCallBack());
//        }
//        if(TAG.equals("WorldFragment")) {
//            HttpProxy.getInstance().post(MainActivity.BASE_URL + "teacher/" + "index/recommend", null, WorldFragment.maps, false, ((WorldFragment)object).getCallBack());
//        }
        Log.i(TAG,"onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(){
        Log.i(TAG,"onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        Log.i(TAG,"onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        Log.i(TAG,"onDestroy");
    }
}
