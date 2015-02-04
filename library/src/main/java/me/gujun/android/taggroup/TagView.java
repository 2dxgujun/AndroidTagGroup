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
import android.widget.TextView;

/**
 * Tag view.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-3 16:46:52
 */
public class TagView extends TextView {
    /**
     * The state specifies if the tag view is normal, active, checked,
     * or input. The default is ACTIVE.
     */
    public enum State {
        NORMAL,
        ACTIVE,
        CHECKED,
        INPUT
    }

    private State mState = State.ACTIVE;

    private Paint mBorderPaint;

    private int mActiveColor;
    private int mNormalColor;
    private float mBorderStrokeWidth;

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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        try {
            mActiveColor = a.getColor(R.styleable.TagView_colorActive, Color.rgb(0x49, 0xC1, 0x20));
            mNormalColor = a.getColor(R.styleable.TagView_colorNormal, Color.GRAY);
            mBorderStrokeWidth = a.getDimension(R.styleable.TagView_borderWidth, 2);
        } finally {
            a.recycle();
        }

        mLeftCornerRectF = new RectF();
        mRightCornerRectF = new RectF();
        mHorizontalBlankFillRectF = new RectF();
        mVerticalBlankFillRectF = new RectF();

        mBorderPath = new Path();
        mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(0);

        setGravity(Gravity.CENTER);
        int horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                10, getResources().getDisplayMetrics());
        int verticalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics());
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        setTextSize(10);
        invalidatePaint();
    }

    private void invalidatePaint() {
        if (mState == State.NORMAL) {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
            mBorderPaint.setColor(mNormalColor);
            mBorderPaint.setPathEffect(null);
            setTextColor(mNormalColor);
            setEnabled(false);
        } else if (mState == State.ACTIVE) {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
            mBorderPaint.setColor(mActiveColor);
            mBorderPaint.setPathEffect(null);
            setTextColor(mActiveColor);
            setEnabled(false);
        } else if (mState == State.CHECKED) {
            mBorderPaint.setStyle(Paint.Style.FILL);
            mBorderPaint.setColor(mActiveColor);
            mBorderPaint.setPathEffect(null);
            setTextColor(Color.WHITE);
            setEnabled(false);
        } else if (mState == State.INPUT) {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
            mBorderPaint.setColor(mNormalColor);
            mBorderPaint.setPathEffect(mPathEffect);
            setTextColor(mNormalColor);
            setEnabled(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mState == State.CHECKED) {
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
        float left = mBorderStrokeWidth;
        float top = mBorderStrokeWidth;
        float right = left + w - mBorderStrokeWidth * 2;
        float bottom = top + h - mBorderStrokeWidth * 2;

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

    public void setState(State state) {
        mState = state;
        invalidatePaint();
    }
}