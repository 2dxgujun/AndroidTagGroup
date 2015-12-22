package me.gujun.android.taggroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>TagGroup</code> is a special layout with a set of tags.
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
 * @version 2.0
 * @since 2015-2-3 14:16:32
 */
public class AndroidTagGroup extends ViewGroup {
    private final int DEFAULT_BORDER_COLOR = Color.rgb(0x49, 0xC1, 0x20);
    private final int DEFAULT_TEXT_COLOR = Color.rgb(0x49, 0xC1, 0x20);
    private final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final int DEFAULT_DASH_BORDER_COLOR = Color.rgb(0xAA, 0xAA, 0xAA);
    private final int DEFAULT_INPUT_HINT_COLOR = Color.argb(0x80, 0x00, 0x00, 0x00);
    private final int DEFAULT_INPUT_TEXT_COLOR = Color.argb(0xDE, 0x00, 0x00, 0x00);
    private final int DEFAULT_CHECKED_BORDER_COLOR = Color.rgb(0x49, 0xC1, 0x20);
    private final int DEFAULT_CHECKED_TEXT_COLOR = Color.WHITE;
    private final int DEFAULT_CHECKED_MARKER_COLOR = Color.WHITE;
    private final int DEFAULT_CHECKED_BACKGROUND_COLOR = Color.rgb(0x49, 0xC1, 0x20);
    private final int DEFAULT_PRESSED_BACKGROUND_COLOR = Color.rgb(0xED, 0xED, 0xED);
    private final float mDefaultBorderStrokeWidth;
    private final float mDefaultTextSize;
    private final float mDefaultHorizontalSpacing;
    private final float mDefaultVerticalSpacing;
    private final float mDefaultHorizontalPadding;
    private final float mDefaultVerticalPadding;
    // Characters limitation (Default: no limitation)
    private int mCharsLimitation;
    // Indicates whether this TagGroup is set up to APPEND mode or DISPLAY mode. Default is false.
    private boolean mIsAppendMode;
    // The text to be displayed when the text of the INPUT tag is empty.
    private CharSequence mInputHint;
    // The tag outline border color.
    private int mBorderColor;
    // The tag text color.
    private int mTextColor;
    // The tag background color.
    private int mBackgroundColor;
    // The dash outline border color.
    private int mDashBorderColor;
    // The  input tag hint text color.
    private int mInputHintColor;
    // The input tag type text color.
    private int mInputTextColor;
    // The checked tag outline border color.
    private int mCheckedBorderColor;
    // The check text color
    private int mCheckedTextColor;
    // The checked marker color.
    private int mCheckedMarkerColor;
    // The checked tag background color.
    private int mCheckedBackgroundColor;
    // The tag background color, when the tag is being pressed.
    private int mPressedBackgroundColor;
    // The tag outline border stroke width, default is 0.5dp.
    private float mBorderStrokeWidth;
    // The tag text size, default is 13sp.
    private float mTextSize;
    // The horizontal tag spacing, default is 8.0dp.
    private int mHorizontalSpacing;
    // The vertical tag spacing, default is 4.0dp.
    private int mVerticalSpacing;
    // The horizontal tag padding, default is 12.0dp.
    private int mHorizontalPadding;
    // The vertical tag padding, default is 3.0dp.
    private int mVerticalPadding;
    // Listener used to dispatch tag change event.
    private OnTagChangeListener mOnTagChangeListener;
    private OnTagLimitationExceedListener mOnTagLimitationExceedListener;
    // Listener used to dispatch tag click event.
    private OnTagClickListener mOnTagClickListener;
    // Adding tags limitation.
    private int mTagsLimitation = -1;
    // Listener used to handle tag click event.
    private InternalTagClickListener mInternalTagClickListener = new InternalTagClickListener();

    public AndroidTagGroup(Context context) {
        this(context, null);
    }

    public AndroidTagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tagGroupStyle);
    }

    public AndroidTagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultBorderStrokeWidth = AndroidUtils.dp2px(getContext(), 0.5f);
        mDefaultTextSize = AndroidUtils.sp2px(getContext(), 13.0f);
        mDefaultHorizontalSpacing = AndroidUtils.dp2px(getContext(), 8.0f);
        mDefaultVerticalSpacing = AndroidUtils.dp2px(getContext(), 4.0f);
        mDefaultHorizontalPadding = AndroidUtils.dp2px(getContext(), 12.0f);
        mDefaultVerticalPadding = AndroidUtils.dp2px(getContext(), 3.0f);

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagGroup, defStyleAttr, R.style.AndroidTagGroup);
        try {
            mIsAppendMode = a.getBoolean(R.styleable.AndroidTagGroup_atg_isAppendMode, false);
            mInputHint = a.getText(R.styleable.AndroidTagGroup_atg_inputHint);
            mBorderColor = a.getColor(R.styleable.AndroidTagGroup_atg_borderColor, DEFAULT_BORDER_COLOR);
            mTextColor = a.getColor(R.styleable.AndroidTagGroup_atg_textColor, DEFAULT_TEXT_COLOR);
            mBackgroundColor = a.getColor(R.styleable.AndroidTagGroup_atg_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            mDashBorderColor = a.getColor(R.styleable.AndroidTagGroup_atg_dashBorderColor, DEFAULT_DASH_BORDER_COLOR);
            mInputHintColor = a.getColor(R.styleable.AndroidTagGroup_atg_inputHintColor, DEFAULT_INPUT_HINT_COLOR);
            mInputTextColor = a.getColor(R.styleable.AndroidTagGroup_atg_inputTextColor, DEFAULT_INPUT_TEXT_COLOR);
            mCheckedBorderColor = a.getColor(R.styleable.AndroidTagGroup_atg_checkedBorderColor, DEFAULT_CHECKED_BORDER_COLOR);
            mCheckedTextColor = a.getColor(R.styleable.AndroidTagGroup_atg_checkedTextColor, DEFAULT_CHECKED_TEXT_COLOR);
            mCheckedMarkerColor = a.getColor(R.styleable.AndroidTagGroup_atg_checkedMarkerColor, DEFAULT_CHECKED_MARKER_COLOR);
            mCheckedBackgroundColor = a.getColor(R.styleable.AndroidTagGroup_atg_checkedBackgroundColor, DEFAULT_CHECKED_BACKGROUND_COLOR);
            mPressedBackgroundColor = a.getColor(R.styleable.AndroidTagGroup_atg_pressedBackgroundColor, DEFAULT_PRESSED_BACKGROUND_COLOR);
            mBorderStrokeWidth = a.getDimension(R.styleable.AndroidTagGroup_atg_borderStrokeWidth, mDefaultBorderStrokeWidth);
            mTextSize = a.getDimension(R.styleable.AndroidTagGroup_atg_textSize, mDefaultTextSize);
            mHorizontalSpacing = (int) a.getDimension(R.styleable.AndroidTagGroup_atg_horizontalSpacing, mDefaultHorizontalSpacing);
            mVerticalSpacing = (int) a.getDimension(R.styleable.AndroidTagGroup_atg_verticalSpacing, mDefaultVerticalSpacing);
            mHorizontalPadding = (int) a.getDimension(R.styleable.AndroidTagGroup_atg_horizontalPadding, mDefaultHorizontalPadding);
            mVerticalPadding = (int) a.getDimension(R.styleable.AndroidTagGroup_atg_verticalPadding, mDefaultVerticalPadding);
            mTagsLimitation = a.getInteger(R.styleable.AndroidTagGroup_atg_tagsLimitation, -1);
            mCharsLimitation = a.getInteger(R.styleable.AndroidTagGroup_atg_charsLimitation, -1);
        } finally {
            a.recycle();
        }

        if (mIsAppendMode) {
            // Append the initial INPUT tag.
            appendInputTag();

            // Set the click listener to detect the end-input event.
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitTag();
                }
            });
        }
    }

    public void setAppendMode(boolean appendMode) {
        mIsAppendMode = appendMode;
    }

    public void setInputHint(CharSequence inputHint) {
        mInputHint = inputHint;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        mBorderColor = borderColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        mTextColor = textColor;
    }

    public void setViewBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setDashBorderColor(@ColorInt int dashBorderColor) {
        mDashBorderColor = dashBorderColor;
    }

    public void setInputHintColor(@ColorInt int inputHintColor) {
        mInputHintColor = inputHintColor;
    }

    public void setInputTextColor(@ColorInt int inputTextColor) {
        mInputTextColor = inputTextColor;
    }

    public void setCheckedBorderColor(@ColorInt int checkedBorderColor) {
        mCheckedBorderColor = checkedBorderColor;
    }

    public void setCheckedTextColor(@ColorInt int checkedTextColor) {
        mCheckedTextColor = checkedTextColor;
    }

    public void setCheckedMarkerColor(@ColorInt int checkedMarkerColor) {
        mCheckedMarkerColor = checkedMarkerColor;
    }

    public void setCheckedBackgroundColor(@ColorInt int checkedBackgroundColor) {
        mCheckedBackgroundColor = checkedBackgroundColor;
    }

    public void setPressedBackgroundColor(@ColorInt int pressedBackgroundColor) {
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public void setBorderStrokeWidth(float borderStrokeWidth) {
        mBorderStrokeWidth = borderStrokeWidth;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
    }

    /**
     * Register a callback to be invoked when limitation exceed.
     *
     * @param onTagLimitationExceedListener
     */
    public void setOnTagLimitationExceedListener(OnTagLimitationExceedListener onTagLimitationExceedListener) {
        mOnTagLimitationExceedListener = onTagLimitationExceedListener;
    }

    public void setCharsLimitation(int limitation) {
        mCharsLimitation = limitation;
    }

    /**
     * Call this to submit the INPUT tag.
     */
    public void submitTag() {
        final TagView inputTag = getInputTag();
        if (inputTag != null && inputTag.isInputAvailable()) {
            inputTag.endInput();

            if (mOnTagChangeListener != null) {
                mOnTagChangeListener.onAppend(AndroidTagGroup.this, inputTag.getText().toString());
            }
            appendInputTag();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();

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
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * Return the last NORMAL state tag view in this group.
     *
     * @return the last NORMAL state tag view or null if not exists
     */
    protected TagView getLastNormalTagView() {
        final int lastNormalTagIndex = mIsAppendMode ? getChildCount() - 2 : getChildCount() - 1;
        return getTagAt(lastNormalTagIndex);
    }

    /**
     * Returns the tag view at the specified position in the group.
     *
     * @param index the position at which to get the tag view from.
     * @return the tag view at the specified position or null if the position
     * does not exists within this group.
     */
    protected TagView getTagAt(int index) {
        return (TagView) getChildAt(index);
    }

    /**
     * Returns the checked tag view in the group.
     *
     * @return the checked tag view or null if not exists.
     */
    protected TagView getCheckedTag() {
        final int checkedTagIndex = getCheckedTagIndex();
        if (checkedTagIndex != -1) {
            return getTagAt(checkedTagIndex);
        }
        return null;
    }

    /**
     * Return the checked tag index.
     *
     * @return the checked tag index, or -1 if not exists.
     */
    protected int getCheckedTagIndex() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final TagView tag = getTagAt(i);
            if (tag.isChecked) {
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

    public void setTagsLimitation(int tagsLimitation) {
        this.mTagsLimitation = tagsLimitation;
    }

    /**
     * Register a callback to be invoked when a tag is clicked.
     *
     * @param l the callback that will run.
     */
    public void setOnTagClickListener(OnTagClickListener l) {
        mOnTagClickListener = l;
    }

    protected void deleteTag(TagView tagView) {
        removeView(tagView);
        if (mOnTagChangeListener != null) {
            mOnTagChangeListener.onDelete(AndroidTagGroup.this, tagView.getText().toString());
        }
        if (getInputTag().mState == TagView.STATE_INPUT) {
            if (!getInputTag().isEnabled()) {
                getInputTag().setEnabled(true);
            }
        }
    }

    /**
     * Returns the INPUT tag view in this group.
     *
     * @return the INPUT state tag view or null if not exists
     */
    protected TagView getInputTag() {
        if (mIsAppendMode) {
            final int inputTagIndex = getChildCount() - 1;
            final TagView inputTag = getTagAt(inputTagIndex);
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
     * Interface definition for a callback to be invoked when adding tags limitation exceed.
     */
    public interface OnTagLimitationExceedListener {
        /**
         * Called when limitation exceed.
         */
        void onLimitationExceed();
    }

    /**
     * Interface definition for a callback to be invoked when a tag group is changed.
     */
    public interface OnTagChangeListener {
        /**
         * Called when a tag has been appended to the group.
         *
         * @param tag the appended tag.
         */
        void onAppend(AndroidTagGroup androidTagGroup, String tag);

        /**
         * Called when a tag has been deleted from the the group.
         *
         * @param tag the deleted tag.
         */
        void onDelete(AndroidTagGroup androidTagGroup, String tag);
    }

    /**
     * Interface definition for a callback to be invoked when a tag is clicked.
     */
    public interface OnTagClickListener {
        /**
         * Called when a tag has been clicked.
         *
         * @param tag The tag text of the tag that was clicked.
         */
        void onTagClick(String tag);
    }

    /**
     * For {@link AndroidTagGroup} save and restore state.
     */
    static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
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
    }

    /**
     * The tag view click listener for internal use.
     */
    class InternalTagClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final TagView tag = (TagView) v;
            if (mIsAppendMode) {
                if (tag.mState == TagView.STATE_INPUT) {
                    // If the clicked tag is in INPUT state, uncheck the previous checked tag if exists.
                    final TagView checkedTag = getCheckedTag();
                    if (checkedTag != null) {
                        checkedTag.setChecked(false);
                    }
                } else {
                    // If the clicked tag is currently checked, delete the tag.
                    if (tag.isChecked) {
                        deleteTag(tag);
                    } else {
                        // If the clicked tag is unchecked, uncheck the previous checked tag if exists,
                        // then check the clicked tag.
                        final TagView checkedTag = getCheckedTag();
                        if (checkedTag != null) {
                            checkedTag.setChecked(false);
                        }
                        tag.setChecked(true);
                    }
                }
            } else {
                if (mOnTagClickListener != null) {
                    mOnTagClickListener.onTagClick(tag.getText().toString());
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
         * The offset to the text.
         */
        private static final int CHECKED_MARKER_OFFSET = 3;

        /**
         * The stroke width of the checked marker
         */
        private static final int CHECKED_MARKER_STROKE_WIDTH = 4;

        /**
         * The current state.
         */
        private int mState;

        /**
         * Indicates the tag if checked.
         */
        private boolean isChecked = false;

        /**
         * Indicates the tag if pressed.
         */
        private boolean isPressed = false;

        private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mCheckedMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /**
         * The rect for the tag's left corner drawing.
         */
        private RectF mLeftCornerRectF = new RectF();

        /**
         * The rect for the tag's right corner drawing.
         */
        private RectF mRightCornerRectF = new RectF();

        /**
         * The rect for the tag's horizontal blank fill area.
         */
        private RectF mHorizontalBlankFillRectF = new RectF();

        /**
         * The rect for the tag's vertical blank fill area.
         */
        private RectF mVerticalBlankFillRectF = new RectF();

        /**
         * The rect for the checked mark draw bound.
         */
        private RectF mCheckedMarkerBound = new RectF();

        /**
         * Used to detect the touch event.
         */
        private Rect mOutRect = new Rect();

        /**
         * The path for draw the tag's outline border.
         */
        private Path mBorderPath = new Path();

        /**
         * The path effect provide draw the dash border.
         */
        private PathEffect mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);

        {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStrokeWidth(CHECKED_MARKER_STROKE_WIDTH);
            mCheckedMarkerPaint.setColor(mCheckedMarkerColor);
        }


        public TagView(Context context, final int state, CharSequence text) {
            super(context);
            setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
            setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));

            setGravity(Gravity.CENTER);
            setText(text);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

            mState = state;

            setClickable(mIsAppendMode);
            setFocusable(state == STATE_INPUT);
            setFocusableInTouchMode(state == STATE_INPUT);
            setHint(state == STATE_INPUT ? mInputHint : null);
            setMovementMethod(state == STATE_INPUT ? ArrowKeyMovementMethod.getInstance() : null);

            // Interrupted long click event to avoid PAUSE popup.
            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return state != STATE_INPUT;
                }
            });

            if (state == STATE_INPUT) {
                requestFocus();
                //Replace Enter (new line) button with Action Go
                setRawInputType(InputType.TYPE_CLASS_TEXT);
                setImeOptions(EditorInfo.IME_ACTION_GO);

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
                                    mOnTagChangeListener.onAppend(AndroidTagGroup.this, getText().toString());
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
                                            mOnTagChangeListener.onDelete(AndroidTagGroup.this, lastNormalTagView.getText().toString());
                                        }
                                    } else {
                                        final TagView checkedTagView = getCheckedTag();
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

                // Handle the INPUT tag content changed.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // When the INPUT state tag changed, uncheck the checked tag if exists.
                        final TagView checkedTagView = getCheckedTag();
                        if (checkedTagView != null) {
                            checkedTagView.setChecked(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }

            invalidatePaint();
        }

        /**
         * Indicates whether the input content is available.
         *
         * @return True if the input content is available, false otherwise.
         */
        public boolean isInputAvailable() {
            return getText() != null && getText().length() > 0;
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
                    isChecked ? (int) (mHorizontalPadding + getHeight() / 2.5f + CHECKED_MARKER_OFFSET)
                            : mHorizontalPadding,
                    mVerticalPadding);
            invalidatePaint();
        }

        private void invalidatePaint() {
            if (mIsAppendMode) {
                if (mState == STATE_INPUT) {
                    mBorderPaint.setColor(mDashBorderColor);
                    mBorderPaint.setPathEffect(mPathEffect);
                    mBackgroundPaint.setColor(mBackgroundColor);
                    setHintTextColor(mInputHintColor);
                    setTextColor(mInputTextColor);
                } else {
                    mBorderPaint.setPathEffect(null);
                    if (isChecked) {
                        mBorderPaint.setColor(mCheckedBorderColor);
                        mBackgroundPaint.setColor(mCheckedBackgroundColor);
                        setTextColor(mCheckedTextColor);
                    } else {
                        mBorderPaint.setColor(mBorderColor);
                        mBackgroundPaint.setColor(mBackgroundColor);
                        setTextColor(mTextColor);
                    }
                }
            } else {
                mBorderPaint.setColor(mBorderColor);
                mBackgroundPaint.setColor(mBackgroundColor);
                setTextColor(mTextColor);
            }

            if (isPressed) {
                mBackgroundPaint.setColor(mPressedBackgroundColor);
            }
        }

        @Override
        protected boolean getDefaultEditable() {
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawArc(mLeftCornerRectF, -180, 90, true, mBackgroundPaint);
            canvas.drawArc(mLeftCornerRectF, -270, 90, true, mBackgroundPaint);
            canvas.drawArc(mRightCornerRectF, -90, 90, true, mBackgroundPaint);
            canvas.drawArc(mRightCornerRectF, 0, 90, true, mBackgroundPaint);
            canvas.drawRect(mHorizontalBlankFillRectF, mBackgroundPaint);
            canvas.drawRect(mVerticalBlankFillRectF, mBackgroundPaint);

            if (isChecked) {
                canvas.save();
                canvas.rotate(45, mCheckedMarkerBound.centerX(), mCheckedMarkerBound.centerY());
                canvas.drawLine(mCheckedMarkerBound.left, mCheckedMarkerBound.centerY(),
                        mCheckedMarkerBound.right, mCheckedMarkerBound.centerY(), mCheckedMarkerPaint);
                canvas.drawLine(mCheckedMarkerBound.centerX(), mCheckedMarkerBound.top,
                        mCheckedMarkerBound.centerX(), mCheckedMarkerBound.bottom, mCheckedMarkerPaint);
                canvas.restore();
            }
            canvas.drawPath(mBorderPath, mBorderPaint);
            super.onDraw(canvas);
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
            /*
            Following line returns null if the view is not enabled
            We need to check if the returned value is null or not because of enabling or disabling the input view for
            have the limitation feature.
             */
            InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
            if (inputConnection != null) {
                return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
            }
            return null;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (mState == STATE_INPUT) {
                // The INPUT tag doesn't change background color on the touch event.
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    getDrawingRect(mOutRect);
                    isPressed = true;
                    invalidatePaint();
                    invalidate();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (!mOutRect.contains((int) event.getX(), (int) event.getY())) {
                        isPressed = false;
                        invalidatePaint();
                        invalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    isPressed = false;
                    invalidatePaint();
                    invalidate();
                    break;
                }
            }
            return super.onTouchEvent(event);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
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
            mCheckedMarkerBound.set(right - m - mHorizontalPadding + CHECKED_MARKER_OFFSET,
                    top + h / 2 - m / 2,
                    right - mHorizontalPadding + CHECKED_MARKER_OFFSET,
                    bottom - h / 2 + m / 2);

            // Ensure the checked mark drawing region is correct across screen orientation changes.
            if (isChecked) {
                setPadding(mHorizontalPadding,
                        mVerticalPadding,
                        (int) (mHorizontalPadding + h / 2.5f + CHECKED_MARKER_OFFSET),
                        mVerticalPadding);
            }
        }

        /**
         * Solve edit text delete(backspace) key detect, see<a href="http://stackoverflow.com/a/14561345/3790554">
         * Android: Backspace in WebView/BaseInputConnection</a>
         */
        private class ZanyInputConnection extends InputConnectionWrapper {
            public ZanyInputConnection(android.view.inputmethod.InputConnection target, boolean mutable) {
                super(target, mutable);
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
                if (beforeLength == 1 && afterLength == 0) {
                    // backspace
                    return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                            && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
                return super.deleteSurroundingText(beforeLength, afterLength);
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                return super.sendKeyEvent(event);
            }
        }
    }

    /**
     * Returns the INPUT state tag in this group.
     *
     * @return the INPUT state tag view or null if not exists
     */
    public String getInputTagText() {
        final TagView inputTagView = getInputTag();
        if (inputTagView != null) {
            return inputTagView.getText().toString();
        }
        return null;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width;
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
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.tags = getTags();
        ss.checkedPosition = getCheckedTagIndex();
        if (getInputTag() != null) {
            ss.input = getInputTag().getText().toString();
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
        TagView checkedTagView = getTagAt(ss.checkedPosition);
        if (checkedTagView != null) {
            checkedTagView.setChecked(true);
        }
        if (getInputTag() != null) {
            getInputTag().setText(ss.input);
        }
    }


    /**
     * Returns the tag array in group, except the INPUT tag.
     *
     * @return the tag array.
     */
    public String[] getTags() {
        final int count = getChildCount();
        final List<String> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final TagView tagView = getTagAt(i);
            if (tagView.mState == TagView.STATE_NORMAL) {
                tagList.add(tagView.getText().toString());
            }
        }

        return tagList.toArray(new String[tagList.size()]);
    }


    /**
     * Set the tags. It will remove all previous tags first.
     *
     * @param tags the tag list to set.
     */
    public void setTags(String... tags) {
        if (mTagsLimitation != -1 && tags.length > mTagsLimitation) {
            throw new IllegalStateException(String.format("There is a limitation (%1$d) in adding tags.", mTagsLimitation));
        }
        removeAllViews();
        for (final String tag : tags) {
            appendTag(tag);
        }

        if (mIsAppendMode) {
            appendInputTag();
        }
    }


    /**
     * @see #setTags(String...)
     */
    public void setTags(List<String> tagList) {
        setTags(tagList.toArray(new String[tagList.size()]));
    }


    /**
     * @see #appendInputTag(String)
     */
    protected void appendInputTag() {
        appendInputTag(null);
    }


    /**
     * Append a INPUT tag to this group. It will throw an exception if there has a previous INPUT tag.
     *
     * @param tag the tag text.
     */
    protected void appendInputTag(String tag) {
        final TagView previousInputTag = getInputTag();
        if (previousInputTag != null) {
            throw new IllegalStateException("Already has an INPUT tag in group.");
        }

        final TagView newInputTag = new TagView(getContext(), TagView.STATE_INPUT, tag);

        if (mCharsLimitation != -1) {
            newInputTag.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getInputTagText().length() - 1 == mCharsLimitation) {
                        newInputTag.setText(getInputTagText().substring(0, mCharsLimitation));
                        newInputTag.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    }
                }
            });
        }

        // If limitation exceed, disable the input and invoke a callback.
        if (mTagsLimitation != -1 && getTags().length >= mTagsLimitation) {
            if (mOnTagLimitationExceedListener != null) {
                mOnTagLimitationExceedListener.onLimitationExceed();
            }
            newInputTag.setEnabled(false);
        }
        newInputTag.setOnClickListener(mInternalTagClickListener);
        addView(newInputTag);
    }


    /**
     * Append tag to this group.
     *
     * @param tag the tag to append.
     */
    protected void appendTag(CharSequence tag) {
        final TagView newTag = new TagView(getContext(), TagView.STATE_NORMAL, tag);
        newTag.setOnClickListener(mInternalTagClickListener);
        addView(newTag);
    }


}