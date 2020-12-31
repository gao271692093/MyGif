package com.glg.mygif.utils;

import com.glg.mygif.MyApplication;

/**
 * Description:
 *
 * @package: com.glg.mygif.utils
 * @className: DensityUtil
 * @author: gao
 * @date: 2020/9/14 10:30
 */
public class DensityUtil {

    public static int dp2px(Float dp) {
        Float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static int px2dp(Float px) {
        Float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }
}
