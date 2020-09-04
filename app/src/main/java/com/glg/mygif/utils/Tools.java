package com.glg.mygif.utils;

import android.os.Environment;

/**
 * Created by gao on 2020/7/22 10:50.
 * Function:
 */
public class Tools {
    public static boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
}
