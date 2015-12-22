package me.gujun.android.taggroup;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Alireza Eskandarpour Shoferi on 12/22/2015.
 */
public class AndroidUtils {
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }
}
