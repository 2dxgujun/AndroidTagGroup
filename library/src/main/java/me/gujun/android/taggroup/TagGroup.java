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
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>TagGroup</code> is a special layout that contain a set of tags.
 * This group has two modes:
 * <p>
 * 1. APPEND mode
 * 2. DISPLAY mode
 * </p>
 * Default is DISPLAY mode. When in APPEND mode, the group is capable of input for append new tags
 * and delete tags.
 * <p>
 * When in DISPLAY mode, the group is only contain NORMAL state tags, and the tags in group
 * is not focusable.
 * </p>
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-3 14:16:32
 */
public class TagGroup extends ViewGroup {
    private final int default_bright_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_dim_color = Color.rgb(0xAA, 0xAA, 0xAA);
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;

    /**
     * Indicates whether this TagGroup is set up to APPEND mode or DISPLAY mode. Default is false.
     */
    private boolean isAppendMode;

    /**
     * The text to be displayed when the text of the INPUT state tag is empty.
     */
    private CharSequence mInputTagHint;

    /**
     * The bright color of the tag.
     */
    private int mBrightColor;

    /**
     * The dim color of the tag.
     */
    private int mDimColor;

    /**
     * The tag outline border stroke width, default is 0.5dp.
     */
    private float mBorderStrokeWidth;

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
     * The horizontal tag padding, default is 12.0dp.
     */
    private int mHorizontalPadding;

    /**
     * The vertical tag padding, default is 3.0dp.
     */
    private int mVerticalPadding;

    /**
     * Listener used to dispatch tag change event.
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

        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);
        default_horizontal_padding = dp2px(12.0f);
        default_vertical_padding = dp2px(3.0f);

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TagGroup, defStyleAttr, R.style.TagGroup);
        try {
            isAppendMode = a.getBoolean(R.styleable.TagGroup_isAppendMode, false);
            mInputTagHint = a.getText(R.styleable.TagGroup_inputTagHint);
            mBrightColor = a.getColor(R.styleable.TagGroup_brightColor, default_bright_color);
            mDimColor = a.getColor(R.styleable.TagGroup_dimColor, default_dim_color);
            mBorderStrokeWidth = a.getDimension(R.styleable.TagGroup_borderStrokeWidth, default_border_stroke_width);
            mTextSize = a.getDimension(R.styleable.TagGroup_textSize, default_text_size);
            mHorizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_horizontalSpacing,
                    default_horizontal_spacing);
            mVerticalSpacing = (int) a.getDimension(R.styleable.TagGroup_verticalSpacing,
                    default_vertical_spacing);
            mHorizontalPadding = (int) a.getDimension(R.styleable.TagGroup_horizontalPadding,
                    default_horizontal_padding);
            mVerticalPadding = (int) a.getDimension(R.styleable.TagGroup_verticalPadding,
                    default_vertical_padding);
        } finally {
            a.recycle();
        }

        setUpTagGroup();
    }

    protected void setUpTagGroup() {
        if (isAppendMode) {
            // Append the initial INPUT state tag.
            appendInputTag();

            // Set the TagGroup click listener to handle the end-input click.
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TagView inputTag = getInputTagView();
                    if (inputTag != null && inputTag.isInputAvailable()) {
                        inputTag.endInput();

                        if (mOnTagChangeListener != null) {
                            mOnTagChangeListener.onAppend(TagGroup.this, inputTag.getText().toString());
                        }
                        appendInputTag(); // Append a new INPUT state tag.
                    }
                }
            });
        }
    }

    public int getBrightColor() {
        return mBrightColor;
    }

    public void setBrightColor(int brightColor) {
        mBrightColor = brightColor;
        invalidateAllTagsPaint();
        invalidate();
    }

    public int getDimColor() {
        return mDimColor;
    }

    public void setDimColor(int dimColor) {
        mDimColor = dimColor;
        invalidateAllTagsPaint();
        invalidate();
    }

    public float getBorderStrokeWidth() {
        return mBorderStrokeWidth;
    }

    public void setBorderStrokeWidth(float borderStrokeWidth) {
        mBorderStrokeWidth = borderStrokeWidth;
        invalidateAllTagsPaint();
        requestLayout();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidateAllTagsPaint();
        requestLayout();
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        requestLayout();
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
        requestLayout();
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        requestLayout();
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        requestLayout();
    }

    /**
     * Invalidate all tag views' paint in this group.
     */
    protected void invalidateAllTagsPaint() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TagView tagView = getTagViewAt(i);
            tagView.invalidatePaint();
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

        int row = 0; // The row counter.
        int rowWidth = 0; // Calc the current row width.
        int rowMaxHeight = 0; // Calc the max tag height, in current row.

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) { // Next line.
                    rowWidth = childWidth; // The next row width.
                    height += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = childHeight; // The next row max height.
                    row++;
                } else { // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += mHorizontalSpacing;
            }
        }
        // Account for the last row height.
        height += rowMaxHeight;

        // Account for the padding too.
        height += getPaddingTop() + getPaddingBottom();

        // If the tags grouped in one row, set the width to wrap the tags.
        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {// If the tags grouped exceed one line, set the width to match the parent.
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + mHorizontalSpacing;
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.tags = getTags();
        ss.checkedPosition = getCheckedTagIndex();
        if (getInputTagView() != null) {
            ss.input = getInputTagView().getText().toString();
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setTags(ss.tags);
        TagView checkedTagView = getTagViewAt(ss.checkedPosition);
        if (checkedTagView != null) {
            checkedTagView.setChecked(true);
        }
        if (getInputTagView() != null) {
            getInputTagView().setText(ss.input);
        }
    }

    /**
     * Returns the INPUT state tag view in this group.
     *
     * @return the INPUT state tag view or null if not exists
     */
    protected TagView getInputTagView() {
        if (isAppendMode) {
            final int inputTagIndex = getChildCount() - 1;
            final TagView inputTag = getTagViewAt(inputTagIndex);
            if (inputTag != null && inputTag.mState == TagView.STATE_INPUT) {
                return inputTag;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the INPUT state tag in this group.
     *
     * @return the INPUT state tag view or null if not exists
     */
    public String getInputTag() {
        final TagView inputTagView = getInputTagView();
        if (inputTagView != null) {
            return inputTagView.getText().toString();
        }
        return null;
    }

    /**
     * Return the last NORMAL state tag view in this group.
     *
     * @return the last NORMAL state tag view or null if not exists
     */
    protected TagView getLastNormalTagView() {
        final int lastNormalTagIndex = isAppendMode ? getChildCount() - 2 : getChildCount() - 1;
        TagView lastNormalTagView = getTagViewAt(lastNormalTagIndex);
        return lastNormalTagView;
    }

    /**
     * Returns the NORMAL state tags array in group.
     *
     * @return the tag array
     */
    public String[] getTags() {
        final int count = getChildCount();
        final List<String> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final TagView tagView = getTagViewAt(i);
            if (tagView.mState == TagView.STATE_NORMAL) {
                tagList.add(tagView.getText().toString());
            }
        }

        return tagList.toArray(new String[]{});
    }

    /**
     * Returns the tag view at the specified position in the group.
     *
     * @param index the position at which to get the tag view from
     * @return the tag view at the specified position or null if the position
     * does not exists within this group
     */
    protected TagView getTagViewAt(int index) {
        return (TagView) getChildAt(index);
    }

    /**
     * Returns the checked tag view in the group.
     *
     * @return the checked tag view or null if it does not exists within this group
     */
    protected TagView getCheckedTagView() {
        final int checkedTagIndex = getCheckedTagIndex();
        if (checkedTagIndex != -1) { // exists
            return getTagViewAt(checkedTagIndex);
        }
        return null;
    }

    /**
     * Return the checked tag index.
     *
     * @return the checked tag index, or -1 if there is no checked tag exists
     */
    protected int getCheckedTagIndex() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final TagView tagView = getTagViewAt(i);
            if (tagView.isChecked) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Register a callback to be invoked when this tag group is changed.
     *
     * @param l the callback that will run
     */
    public void setOnTagChangeListener(OnTagChangeListener l) {
        mOnTagChangeListener = l;
    }


    /**
     * @see #appendInputTag(String)
     */
    protected void appendInputTag() {
        appendInputTag(null);
    }

    /**
     * Append a INPUT state tag to this group. It will check the group state first.
     *
     * @param tag the tag text
     */
    protected void appendInputTag(String tag) {
        TagView lastTag = getInputTagView();
        if (lastTag != null) {
            throw new IllegalStateException("Already has a INPUT state tag in group. " +
                    "You must call endInput() before you append new one.");
        }

        TagView tagView = new TagView(getContext(), TagView.STATE_INPUT, tag);
        tagView.setOnClickListener(new OnTagClickListener());
        addView(tagView);
    }

    /**
     * @see #setTags(String...)
     */
    public void setTags(List<String> tagList) {
        setTags(tagList.toArray(new String[]{}));
    }

    /**
     * Set the NORMAL state tag to this group. It will remove all tags first.
     *
     * @param tags the tag list to set
     */
    public void setTags(String... tags) {
        removeAllViews();
        for (final String tag : tags) {
            appendTag(tag);
        }

        if (isAppendMode) {
            appendInputTag();
        }
    }

    /**
     * Append NORMAL state tag to this group.
     *
     * @param tag the tag to append
     */
    protected void appendTag(CharSequence tag) {
        if (isAppendMode) {
            final int appendIndex = getChildCount();
            final TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
            tagView.setOnClickListener(new OnTagClickListener());
            addView(tagView, appendIndex);
        } else {
            final TagView tagView = new TagView(getContext(), TagView.STATE_NORMAL, tag);
            addView(tagView);
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
     * Interface definition for a callback to be invoked when a tag group is changed.
     */
    public interface OnTagChangeListener {
        /**
         * Called when a tag has been appended to the group.
         *
         * @param tag the appended tag
         */
        void onAppend(TagGroup tagGroup, String tag);

        /**
         * Called when a tag has been deleted from the the group.
         *
         * @param tag the deleted tag.
         */
        void onDelete(TagGroup tagGroup, String tag);
    }

    /**
     * Per-child layout information for layouts.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /**
     * For {@link TagGroup} save and restore state.
     */
    static class SavedState extends BaseSavedState {
        int tagCount;
        String[] tags;
        int checkedPosition;
        String input;

        public SavedState(Parcel source) {
            super(source);
            tagCount = source.readInt();
            tags = new String[tagCount];
            source.readStringArray(tags);
            checkedPosition = source.readInt();
            input = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            tagCount = tags.length;
            dest.writeInt(tagCount);
            dest.writeStringArray(tags);
            dest.writeInt(checkedPosition);
            dest.writeString(input);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    /**
     * The tag view click listener.
     */
    class OnTagClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final TagView clickedTagView = (TagView) v;
            if (clickedTagView.mState == TagView.STATE_INPUT) {
                // If the clicked tag is in INPUT state,
                // uncheck the previous checked tag if exists.
                final TagView checkedTagView = getCheckedTagView();
                if (checkedTagView != null) {
                    checkedTagView.setChecked(false);
                }
            } else {
                // If the clicked tag is checked, remove the tag
                // and dispatch the tag group changed event.
                if (clickedTagView.isChecked) {
                    removeView(clickedTagView);
                    if (mOnTagChangeListener != null) {
                        mOnTagChangeListener.onDelete(TagGroup.this, clickedTagView.getText().toString());
                    }
                } else {
                    // If the clicked tag is unchecked, uncheck the previous checked
                    // tag if exists, then set the clicked tag checked.
                    final TagView checkedTagView = getCheckedTagView();
                    if (checkedTagView != null) {
                        checkedTagView.setChecked(false);
                    }
                    clickedTagView.setChecked(true);
                }
            }
        }
    }

    /**
     * The tag view which has two states can be either NORMAL or INPUT.
     */
    class TagView extends TextView {
        public static final int STATE_NORMAL = 1;
        public static final int STATE_INPUT = 2;

        /**
         * The current state.
         */
        private int mState;

        /**
         * Indicates the tag if checked.
         */
        private boolean isChecked = false;

        /**
         * The paint of tag outline border and text.
         */
        private Paint mPaint;

        /**
         * The paint of the checked mark.
         */
        private Paint mMarkPaint;

        /**
         * The rect for the tag's left corner drawing.
         */
        private RectF mLeftCornerRectF;

        /**
         * The rect for the tag's right corner drawing.
         */
        private RectF mRightCornerRectF;

        /**
         * The rect for the tag's horizontal blank fill area.
         */
        private RectF mHorizontalBlankFillRectF;

        /**
         * The rect for the tag's vertical blank fill area.
         */
        private RectF mVerticalBlankFillRectF;

        /**
         * The rect for the checked mark draw bound.
         */
        private RectF mCheckedMarkDrawBound;

        /**
         * The offset to the text.
         */
        private int mCheckedMarkOffset;

        /**
         * The path for draw the tag's outline border.
         */
        private Path mBorderPath;

        /**
         * The path effect provide draw the dash border.
         */
        private PathEffect mPathEffect;

        public TagView(Context context, int state, CharSequence text) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mMarkPaint.setColor(Color.WHITE);
            mMarkPaint.setStrokeWidth(4);

            mLeftCornerRectF = new RectF();
            mRightCornerRectF = new RectF();

            mHorizontalBlankFillRectF = new RectF();
            mVerticalBlankFillRectF = new RectF();

            mCheckedMarkDrawBound = new RectF();
            mCheckedMarkOffset = 3;

            mBorderPath = new Path();
            mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);

            setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
            setLayoutParams(new TagGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT,
                    TagGroup.LayoutParams.WRAP_CONTENT));

            setGravity(Gravity.CENTER);
            setText(text);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

            mState = state;

            setClickable(isAppendMode);
            setFocusable(state == STATE_INPUT);
            setFocusableInTouchMode(state == STATE_INPUT);
            setHint(state == STATE_INPUT ? mInputTagHint : null);
            setMovementMethod(state == STATE_INPUT ? ArrowKeyMovementMethod.getInstance() : null);

            if (state == STATE_INPUT) {
                requestFocus();

                // Handle the ENTER key down.
                setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_NULL
                                && (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN)) {
                            if (isInputAvailable()) {
                                // If the input content is available, end the input and dispatch
                                // the event, then append a new INPUT state tag.
                                endInput();
                                if (mOnTagChangeListener != null) {
                                    mOnTagChangeListener.onAppend(TagGroup.this, getText().toString());
                                }
                                appendInputTag();
                            }
                            return true;
                        }
                        return false;
                    }
                });

                // Handle the BACKSPACE key down.
                setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                            // If the input content is empty, check or remove the last NORMAL state tag.
                            if (TextUtils.isEmpty(getText().toString())) {
                                TagView lastNormalTagView = getLastNormalTagView();
                                if (lastNormalTagView != null) {
                                    if (lastNormalTagView.isChecked) {
                                        removeView(lastNormalTagView);
                                        if (mOnTagChangeListener != null) {
                                            mOnTagChangeListener.onDelete(TagGroup.this, lastNormalTagView.getText().toString());
                                        }
                                    } else {
                                        final TagView checkedTagView = getCheckedTagView();
                                        if (checkedTagView != null) {
                                            checkedTagView.setChecked(false);
                                        }
                                        lastNormalTagView.setChecked(true);
                                    }
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }

            invalidatePaint();
        }

        /**
         * Set whether this tag view is in the checked state.
         *
         * @param checked true is checked, false otherwise
         */
        public void setChecked(boolean checked) {
            isChecked = checked;
            // Make the checked mark drawing region.
            setPadding(mHorizontalPadding,
                    mVerticalPadding,
                    isChecked ? (int) (mHorizontalPadding + getHeight() / 2.5f + mCheckedMarkOffset)
                            : mHorizontalPadding,
                    mVerticalPadding);
            invalidatePaint();
        }

        /**
         * Call this method to end this tag's INPUT state.
         */
        public void endInput() {
            // Make the view not focusable.
            setFocusable(false);
            setFocusableInTouchMode(false);
            // Set the hint empty, make the TextView measure correctly.
            setHint(null);
            // Take away the cursor.
            setMovementMethod(null);

            mState = STATE_NORMAL;
            invalidatePaint();
            requestLayout();
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
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
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(mBrightColor);
                    mPaint.setPathEffect(null);
                    setTextColor(Color.WHITE);
                } else {
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(mBorderStrokeWidth);
                    mPaint.setColor(mBrightColor);
                    mPaint.setPathEffect(null);
                    setTextColor(mBrightColor);
                }

            } else if (mState == STATE_INPUT) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mBorderStrokeWidth);
                mPaint.setColor(mDimColor);
                mPaint.setPathEffect(mPathEffect);
                setTextColor(mDimColor);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isChecked) {
                canvas.drawArc(mLeftCornerRectF, -180, 90, true, mPaint);
                canvas.drawArc(mLeftCornerRectF, -270, 90, true, mPaint);
                canvas.drawArc(mRightCornerRectF, -90, 90, true, mPaint);
                canvas.drawArc(mRightCornerRectF, 0, 90, true, mPaint);
                canvas.drawRect(mHorizontalBlankFillRectF, mPaint);
                canvas.drawRect(mVerticalBlankFillRectF, mPaint);

                canvas.save();
                canvas.rotate(45, mCheckedMarkDrawBound.centerX(), mCheckedMarkDrawBound.centerY());
                canvas.drawLine(mCheckedMarkDrawBound.left, mCheckedMarkDrawBound.centerY(),
                        mCheckedMarkDrawBound.right, mCheckedMarkDrawBound.centerY(), mMarkPaint);
                canvas.drawLine(mCheckedMarkDrawBound.centerX(), mCheckedMarkDrawBound.top,
                        mCheckedMarkDrawBound.centerX(), mCheckedMarkDrawBound.bottom, mMarkPaint);
                canvas.restore();
            } else {
                canvas.drawPath(mBorderPath, mPaint);
            }
            super.onDraw(canvas);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            // Cast to int
            int left = (int) mBorderStrokeWidth;
            int top = (int) mBorderStrokeWidth;
            int right = (int) (left + w - mBorderStrokeWidth * 2);
            int bottom = (int) (top + h - mBorderStrokeWidth * 2);

            int d = bottom - top;

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

            int m = (int) (h / 2.5f);
            h = bottom - top;
            mCheckedMarkDrawBound.set(right - m - mHorizontalPadding + mCheckedMarkOffset,
                    top + h / 2 - m / 2,
                    right - mHorizontalPadding + mCheckedMarkOffset,
                    bottom - h / 2 + m / 2);

            // Ensure the checked mark drawing region is correct across screen orientation changes.
            if (isChecked) {
                setPadding(mHorizontalPadding,
                        mVerticalPadding,
                        isChecked ? (int) (mHorizontalPadding + h / 2.5f + mCheckedMarkOffset)
                                : mHorizontalPadding,
                        mVerticalPadding);
            }
        }
    }
}