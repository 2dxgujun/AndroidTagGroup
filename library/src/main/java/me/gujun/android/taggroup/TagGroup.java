package me.gujun.android.taggroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;


/**
 * This class is used to create a tag group.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-3 14:16:32
 */
public class TagGroup extends ViewGroup {

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


}