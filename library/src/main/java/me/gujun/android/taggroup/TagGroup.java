package me.gujun.android.taggroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * This class is used to create a tag group.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-3 14:16:32
 */
public class TagGroup extends ViewGroup {
    private final int default_active_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_normal_color = Color.rgb(0xAA, 0xAA, 0xAA);
    private final float default_border_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;

    private int mActiveColor;
    private int mNormalColor;
    private float mBorderWidth;
    private float mTextSize;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;


    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tagGroupStyle);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_border_width = dp2px(1.0f);
        default_text_size = 13.0f;
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TagGroup, defStyleAttr, 0);
        try {
            mActiveColor = a.getColor(R.styleable.TagGroup_activeColor, default_active_color);
            mNormalColor = a.getColor(R.styleable.TagGroup_normalColor, default_normal_color);
            mBorderWidth = a.getDimension(R.styleable.TagGroup_borderWidth, default_border_width);
            mTextSize = a.getDimension(R.styleable.TagGroup_textSize, default_text_size);
            mHorizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_horizontalSpacing,
                    default_horizontal_spacing);
            mVerticalSpacing = (int) a.getDimension(R.styleable.TagGroup_verticalSpacing,
                    default_vertical_spacing);
        } finally {
            a.recycle();
        }
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
        int rowWidth = 0;
        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth + mHorizontalSpacing;
                if (rowWidth > widthSize) { // Next line
                    rowWidth = childWidth + mHorizontalSpacing;
                    height += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = childHeight;
                    row++;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
            }
        }
        height += rowMaxHeight;
        height += getPaddingTop() + getPaddingBottom();

        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {// Exceed ont line
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;
        boolean firstTagInGroup = true;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                rowMaxHeight = Math.max(rowMaxHeight, height);

                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = 0;
                    firstTagInGroup = true;
                }
                if (firstTagInGroup) {
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                    firstTagInGroup = !firstTagInGroup;
                } else {
                    child.layout(childLeft + mHorizontalSpacing, childTop, childLeft + width + mHorizontalSpacing, childTop + height);
                }

                childLeft += width;
            }
        }
    }

    public void addTag(String text) {
        TagView tagView = new TagView(getContext());
        tagView.setText(text);
        tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                TagGroup.LayoutParams.WRAP_CONTENT));
        addView(tagView);
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
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

    /**
     *
     */
    public class TagView extends TextView {
        public static final int NORMAL = 1;
        public static final int ACTIVE = 2;
        public static final int CHECKED = 3;
        public static final int INPUT = 4;

        private int mState = ACTIVE;

        private Paint mBorderPaint;

        private RectF mLeftCornerRectF;
        private RectF mRightCornerRectF;

        private RectF mHorizontalBlankFillRectF;
        private RectF mVerticalBlankFillRectF;

        private Path mBorderPath;

        private PathEffect mPathEffect;

        public TagView(Context context) {
            this(context, null);
        }

        public TagView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mLeftCornerRectF = new RectF();
            mRightCornerRectF = new RectF();
            mHorizontalBlankFillRectF = new RectF();
            mVerticalBlankFillRectF = new RectF();

            mBorderPath = new Path();
            mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


            int horizontalPadding = (int) dp2px(15.0f);
            int verticalPadding = (int) dp2px(5.0f);
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
            setGravity(Gravity.CENTER);
            setTextSize(mTextSize);
            invalidatePaint();
        }

        private void invalidatePaint() {
            if (mState == NORMAL) {
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mBorderPaint.setColor(mNormalColor);
                mBorderPaint.setPathEffect(null);
                setTextColor(mNormalColor);
                setEnabled(false);
            } else if (mState == ACTIVE) {
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mBorderPaint.setColor(mActiveColor);
                mBorderPaint.setPathEffect(null);
                setTextColor(mActiveColor);
                setEnabled(false);
            } else if (mState == CHECKED) {
                mBorderPaint.setStyle(Paint.Style.FILL);
                mBorderPaint.setColor(mActiveColor);
                mBorderPaint.setPathEffect(null);
                setTextColor(Color.WHITE);
                setEnabled(false);
            } else if (mState == INPUT) {
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mBorderPaint.setColor(mNormalColor);
                mBorderPaint.setPathEffect(mPathEffect);
                setTextColor(mNormalColor);
                setEnabled(true);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mState == CHECKED) {
                canvas.drawArc(mLeftCornerRectF, -180, 90, true, mBorderPaint);
                canvas.drawArc(mLeftCornerRectF, -270, 90, true, mBorderPaint);
                canvas.drawArc(mRightCornerRectF, -90, 90, true, mBorderPaint);
                canvas.drawArc(mRightCornerRectF, 0, 90, true, mBorderPaint);
                canvas.drawRect(mHorizontalBlankFillRectF, mBorderPaint);
                canvas.drawRect(mVerticalBlankFillRectF, mBorderPaint);
            } else {
                canvas.drawPath(mBorderPath, mBorderPaint);
            }
            super.onDraw(canvas);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            float left = mBorderWidth;
            float top = mBorderWidth;
            float right = left + w - mBorderWidth * 2;
            float bottom = top + h - mBorderWidth * 2;

            float d = bottom - top;

            mLeftCornerRectF.set(left, top, left + d, top + d);
            mRightCornerRectF.set(right - d, top, right, top + d);

            mBorderPath.reset();
            mBorderPath.addArc(mLeftCornerRectF, -180, 90);
            mBorderPath.addArc(mLeftCornerRectF, -270, 90);
            mBorderPath.addArc(mRightCornerRectF, -90, 90);
            mBorderPath.addArc(mRightCornerRectF, 0, 90);

            int l = (int) (d / 2.0f);
            mBorderPath.moveTo(left + l, top);
            mBorderPath.lineTo(right - l, top);

            mBorderPath.moveTo(left + l, bottom);
            mBorderPath.lineTo(right - l, bottom);

            mBorderPath.moveTo(left, top + l);
            mBorderPath.lineTo(left, bottom - l);

            mBorderPath.moveTo(right, top + l);
            mBorderPath.lineTo(right, bottom - l);

            mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
            mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);
        }

        public void setState(int state) {
            mState = state;
            invalidatePaint();
        }
    }
}