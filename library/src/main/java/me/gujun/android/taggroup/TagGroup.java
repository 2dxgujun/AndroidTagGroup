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

import java.util.Arrays;
import java.util.List;

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
    private final int default_foreground_bright = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_foreground_dim = Color.rgb(0xAA, 0xAA, 0xAA);
    private final float default_border_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;

    /**
     * Indicates whether this TagGroup is set up to APPEND mode or DISPLAY mode.
     */
    private boolean isAppendMode;

    private int mForegroundBright;
    private int mForegroundDim;

    /**
     * The tag outline border stroke width, default is 1.0dp.
     */
    private float mBorderWidth;

    /**
     * The tag text size, default is 13sp.
     */
    private float mTextSize;

    /**
     * The horizontal tag spacing, default is 8.0dp.
     */
    private int mHorizontalSpacing;

    /**
     * The vertical tag spacing, default is 4.0dp.
     */
    private int mVerticalSpacing;

    /**
     * Listener used to dispatch tag change events.
     */
    private OnTagChangeListener mOnTagChangeListener;

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
            mForegroundBright = a.getColor(R.styleable.TagGroup_foregroundBright, default_foreground_bright);
            mForegroundDim = a.getColor(R.styleable.TagGroup_foregroundDim, default_foreground_dim);
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
                        if (mOnTagChangeListener != null) {
                            mOnTagChangeListener.onAppend(lastTag.getText().toString());
                        }
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
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + mHorizontalSpacing;
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
     * Register a callback to be invoked when this tag is changed.
     *
     * @param l The callback that will run.
     */
    public void setOnTagChangeListener(OnTagChangeListener l) {
        mOnTagChangeListener = l;
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

    public void setTags(List<String> tagList) {
        if (isAppendMode) {
            int appendIndex = getChildCount() > 0 ? getChildCount() - 1 : 0;
            for (final String tag : tagList) {
                final TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
                tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                        TagGroup.LayoutParams.WRAP_CONTENT));
                tagView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tagView.isChecked) {
                            removeView(tagView);
                            if (mOnTagChangeListener != null) {
                                mOnTagChangeListener.onDelete(tagView.getText().toString());
                            }
                        } else {
                            final int count = getChildCount();
                            for (int i = 0; i < count; i++) {
                                TagView tagV = (TagView) getChildAt(i);
                                if (tagV.isChecked) {
                                    tagV.setChecked(false);
                                    break;
                                }
                            }
                            tagView.setChecked(true);
                        }
                    }
                });
                addView(tagView, appendIndex++);
            }
        } else {
            removeAllViews();
            for (String tag : tagList) {
                TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
                tagView.setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                        TagGroup.LayoutParams.WRAP_CONTENT));
                addView(tagView);
            }
        }
    }

    public void setTags(String[] tags) {
        List<String> tagList = Arrays.asList(tags);
        setTags(tagList);
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
     * Interface definition for a callback to be invoked when a tag is changed.
     */
    public interface OnTagChangeListener {
        /**
         * Called when a tag has been appended.
         *
         * @param tag The appended tag.
         */
        void onAppend(String tag);

        /**
         * Called when a tag has been deleted.
         *
         * @param tag The deleted tag.
         */
        void onDelete(String tag);
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
            mState = STATE_NORMAL;
            invalidatePaint();
            requestLayout();
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
        }

        @Override
        protected MovementMethod getDefaultMovementMethod() {
            if (mState == STATE_INPUT) {
                // mMovement
                return ArrowKeyMovementMethod.getInstance();
            } else {
                return null;
            }
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
                    mBorderPaint.setColor(mForegroundBright);
                    mBorderPaint.setPathEffect(null);
                    setTextColor(Color.WHITE);
                } else {
                    mBorderPaint.setStyle(Paint.Style.STROKE);
                    mBorderPaint.setStrokeWidth(mBorderWidth);
                    mBorderPaint.setColor(mForegroundBright);
                    mBorderPaint.setPathEffect(null);
                    setTextColor(mForegroundBright);
                }

            } else if (mState == STATE_INPUT) {
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mBorderPaint.setColor(mForegroundDim);
                mBorderPaint.setPathEffect(mPathEffect);
                setTextColor(mForegroundDim);
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

        public void setChecked(boolean checked) {
            isChecked = checked;
            invalidatePaint();
        }
    }
}