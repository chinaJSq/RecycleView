package com.com.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/23.
 */

public class SizeHelper {
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dp * scale);
    }

}
