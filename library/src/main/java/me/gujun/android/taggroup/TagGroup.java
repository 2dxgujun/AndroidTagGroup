package me.gujun.android.taggroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * This class is used to create a tag group.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-3 14:16:32
 */
public class TagGroup extends ViewGroup {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;


    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0;
        int childrenRowWidth = 0;
        int childrenRowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            if (child.getVisibility() != GONE) {
                childrenRowWidth += childWidth;
                if (childrenRowWidth > widthSize) { // Next line
                    childrenRowWidth = childWidth;
                    height += childrenRowMaxHeight;
                    childrenRowMaxHeight = childHeight;
                    row++;
                } else {
                    childrenRowMaxHeight = Math.max(childrenRowMaxHeight, childHeight);
                }
            }
        }
        height += childrenRowMaxHeight;

        if (row == 0) {
            width = childrenRowWidth;
        } else {// Exceed ont line
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        final int parentLeft = l;
        final int parentRight = r;
        final int parentTop = t;
        final int parentBottom = b;

        int childLeft = parentLeft;
        int childTop = parentTop;

        int currRowMaxChildHeight = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                currRowMaxChildHeight = Math.max(currRowMaxChildHeight, height);

                if (childLeft + width > parentRight - parentLeft) { // Next line
                    childLeft = parentLeft;
                    childTop += currRowMaxChildHeight;
                    currRowMaxChildHeight = 0;
                }

                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                childLeft += width;
            }
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagGroup.LayoutParams(getContext(), attrs);
    }

    /**
     * Per-child layout information for layouts that support margins.
     */
    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}