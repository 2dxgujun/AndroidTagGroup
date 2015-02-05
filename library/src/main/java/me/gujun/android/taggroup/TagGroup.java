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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class is used to create a group for a set of tags. The tag group has two modes:
 * <p/>
 * 1. APPEND mode<br/>
 * 2. DISPLAY mode
 * <p/>
 * Default is DISPLAY mode. When in APPEND mode, the group is capable of input and append new tags,
 * it always shows a INPUT state tag view at the last position of group. When you finish input,
 * click the blank region of the tag group to end the INPUT state.Click the NORMAL state tag,
 * will check the tag, next click to the checked tag will delete the tag.
 * <p/>
 * When in DISPLAY mode, the group is only for display NORMAL state tags, and all the tags in group
 * is not focusable.
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

    /**
     * Indicates whether this TagGroup is set up to APPEND mode or DISPLAY mode.
     */
    private boolean isAppendMode;

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
            isAppendMode = a.getBoolean(R.styleable.TagGroup_isAppendMode, false);
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

        setUpTagGroup();
    }

    protected void setUpTagGroup() {
        if (isAppendMode) {
            // Append the initial INPUT state tag.
            appendInputTag();

            // Set the TagGroup click listener to end the INPUT state tag and append new one.
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TagView lastTag = getLastTag();
                    if (lastTag != null && lastTag.getState() == TagView.STATE_INPUT
                            && lastTag.isInputAvailable()) {
                        lastTag.endInput();
                        appendInputTag();
                    }
                }
            });
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

    /**
     * Return the last tag in this group.
     *
     * @return The last tag or null if none.
     */
    protected TagView getLastTag() {
        final int lastTagIndex = getChildCount() - 1;
        TagView tagView = (TagView) getChildAt(lastTagIndex);
        return tagView;
    }

    /**
     * Append a INPUT state tag to this group. It will check the group state first.
     */
    protected void appendInputTag() {
        TagView lastTag = getLastTag();
        if (lastTag != null && lastTag.getState() == TagView.STATE_INPUT) {
            throw new IllegalStateException("Already has a INPUT state tag in group. " +
                    "You must call endInput() before you append new one.");
        }

        TagView tagView = new TagView(getContext(), TagView.STATE_INPUT, null);
        tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                TagGroup.LayoutParams.WRAP_CONTENT));
        addView(tagView);
    }

    public void setTags(String[] tags) {
        if (isAppendMode) {
            int appendIndex = getChildCount() > 0 ? getChildCount() - 1 : 0;
            for (String tag : tags) {
                TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
                tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                        TagGroup.LayoutParams.WRAP_CONTENT));
                addView(tagView);
            }
        } else {
            removeAllViews();
            for (String tag : tags) {
                TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
                tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                        TagGroup.LayoutParams.WRAP_CONTENT));
                addView(tagView);
            }
        }
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
     * A tag view is a two-states text that can be either normal or input.
     * When the radio button is normal, the user can press or click it to check it.
     * Tag views are normally used in a TagGroup.
     */
    class TagView extends TextView {
        public static final int STATE_NORMAL = 1;
        public static final int STATE_INPUT = 3;

        private int mState = STATE_NORMAL;

        private boolean isChecked = false;

        private Paint mBorderPaint;

        private RectF mLeftCornerRectF;
        private RectF mRightCornerRectF;

        private RectF mHorizontalBlankFillRectF;
        private RectF mVerticalBlankFillRectF;

        private Path mBorderPath;
        private PathEffect mPathEffect;

        public TagView(Context context, int state, String text) {
            super(context);
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mLeftCornerRectF = new RectF();
            mRightCornerRectF = new RectF();

            mHorizontalBlankFillRectF = new RectF();
            mVerticalBlankFillRectF = new RectF();

            mBorderPath = new Path();
            mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);

            int horizontalPadding = (int) dp2px(15.0f);
            int verticalPadding = (int) dp2px(5.0f);
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);

            setGravity(Gravity.CENTER);
            setTextSize(mTextSize);
            setClickable(true);

            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    requestLayout();
                }
            });

            mState = state;

            if (state == STATE_INPUT) {
                setHint("添加标签");
                setFocusable(true);
                setFocusableInTouchMode(true);
            } else {
                setText(text);
            }

            invalidatePaint();
        }

        public void endInput() {
            setFocusable(false);
            setFocusableInTouchMode(false);
            requestLayout();
            mState = STATE_NORMAL;
            invalidatePaint();
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
        }

        @Override
        protected MovementMethod getDefaultMovementMethod() {
            return ArrowKeyMovementMethod.getInstance();
        }

        public int getState() {
            return mState;
        }

        /**
         * Indicates whether the input content is available.
         *
         * @return True if the input content is available, false otherwise.
         */
        public boolean isInputAvailable() {
            return getText() != null && getText().length() > 0;
        }

        private void invalidatePaint() {
            if (mState == STATE_NORMAL) {
                if (isChecked) {
                    mBorderPaint.setStyle(Paint.Style.FILL);
                    mBorderPaint.setColor(mActiveColor);
                    mBorderPaint.setPathEffect(null);
                    setTextColor(Color.WHITE);
                } else {
                    mBorderPaint.setStyle(Paint.Style.STROKE);
                    mBorderPaint.setStrokeWidth(mBorderWidth);
                    mBorderPaint.setColor(mNormalColor);
                    mBorderPaint.setPathEffect(null);
                    setTextColor(mNormalColor);
                }

            } else if (mState == STATE_INPUT) {
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mBorderPaint.setColor(mNormalColor);
                mBorderPaint.setPathEffect(mPathEffect);
                setTextColor(mNormalColor);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isChecked) {
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
    }
}