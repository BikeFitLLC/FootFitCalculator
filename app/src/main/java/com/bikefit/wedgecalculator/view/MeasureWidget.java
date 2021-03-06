package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;

/**
 * Primary angle calculator widget that allows translating and rotating.
 */
public class MeasureWidget extends View {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------
    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------

    /**
     * Listener for the current angle. This emits the absolute value of the angle, so it doesn't
     * matter about the rotation, it's always an angle relative to the horizontal.
     */
    public interface AngleListener {
        void onAngleUpdate(float angle);
    }

    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private enum MoveMode {
        NONE,
        ROTATE,
        TRANSLATE,
    }

    private static final float DEFAULT_STROKE_WIDTH = 5;
    private static final boolean DEFAULT_DEBUG_MODE = false;
    private static final float DEFAULT_MAXIMUM_ANGLE = 30.0f;
    private static final int DEFAULT_COLOR = Color.RED;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    // Constants
    private Paint mLinePaint;
    private float mDistanceToIconsFromCenter;
    private float mDistanceFromTopOfScreen;
    private Rect mViewAreaInWindow = new Rect();

    // Constraints
    private float mMaximumAngle = DEFAULT_MAXIMUM_ANGLE;
    private float mPaintStrokeWidth = DEFAULT_STROKE_WIDTH;
    private int mPaintColor = DEFAULT_COLOR;

    // Transform state
    private Matrix mMainTransform = new Matrix();
    private Matrix mRotateIconOffsetTransform = new Matrix();
    private Matrix mRotateIconTransform = new Matrix();
    private Matrix mUpDownIconTransform = new Matrix();
    private Matrix mScratchMatrix = new Matrix();
    private float mAngle;
    private FootSide mFootSide;

    // Icon Resources
    private VectorDrawableCompat mUpDownIcon;
    private VectorDrawableCompat mRotateIcon;

    // Input
    private PointF mPrevMovePoint = new PointF();
    private PointF mLastTouchPoint = new PointF();
    private MoveMode mMoveMode = MoveMode.NONE;
    private float mInputTouchRadius;

    // Listener
    private AngleListener mAngleListener;

    // Debugging
    private boolean mDebugMode = DEFAULT_DEBUG_MODE;
    private Paint mDebugPaint;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public MeasureWidget(Context context) {
        super(context);
        init(null);
    }

    public MeasureWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MeasureWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

        if (!isInEditMode() && attrs != null) {
            TypedArray styleValues = BikeFitApplication.getInstance().obtainStyledAttributes(attrs, R.styleable.MeasureWidget);
            mDebugMode = styleValues.getBoolean(R.styleable.MeasureWidget_debugMode, DEFAULT_DEBUG_MODE);
            mMaximumAngle = styleValues.getFloat(R.styleable.MeasureWidget_maximumAngle, DEFAULT_MAXIMUM_ANGLE);
            mPaintStrokeWidth = styleValues.getFloat(R.styleable.MeasureWidget_strokeWidth, DEFAULT_STROKE_WIDTH);
            mPaintColor = styleValues.getInt(R.styleable.MeasureWidget_strokeColor, DEFAULT_COLOR);
            styleValues.recycle();
        } else {
            mDebugMode = DEFAULT_DEBUG_MODE;
            mMaximumAngle = DEFAULT_MAXIMUM_ANGLE;
            mPaintStrokeWidth = DEFAULT_STROKE_WIDTH;
            mPaintColor = DEFAULT_COLOR;
        }

        setupLinePaint();

        if (!isInEditMode()) {
            mFootSide = FootSide.LEFT;
        }

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float screenHeight = dm.heightPixels;
        float screenWidth = dm.widthPixels;

        mUpDownIcon = VectorDrawableCompat.create(getResources(), R.drawable.up_down_icon, null);
        mRotateIcon = VectorDrawableCompat.create(getResources(), R.drawable.rotate_icon, null);

        if (mUpDownIcon == null || mRotateIcon == null) {
            return;
        }

        mUpDownIcon.setBounds(0, 0, mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());
        PointF upDownIconSize = new PointF(mUpDownIcon.getIntrinsicWidth(), mUpDownIcon.getIntrinsicHeight());

        mRotateIcon.setBounds(0, 0, mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());
        PointF rotateIconSize = new PointF(mRotateIcon.getIntrinsicWidth(), mRotateIcon.getIntrinsicHeight());

        mInputTouchRadius = 1.33f * (Math.max(rotateIconSize.y, upDownIconSize.y) * 0.5f);
        mDistanceFromTopOfScreen = 0;   //initial value, will be set with mGlobalLayoutListener

        // Initial transforms
        mDistanceToIconsFromCenter = screenWidth / 3;
        mMainTransform.setTranslate(screenWidth / 2, screenHeight / 2);
        mRotateIconOffsetTransform.setTranslate(mDistanceToIconsFromCenter, 0);
        mRotateIconTransform.setTranslate(-rotateIconSize.x * 0.5f, -rotateIconSize.y * 0.5f);
        mUpDownIconTransform.setTranslate(-upDownIconSize.x * 0.5f, -upDownIconSize.y * 0.5f);

        //setup ViewTreeObserver for capturing this view's size
        getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mScratchMatrix.reset();

        drawVerticalCenterLine(canvas);
        drawHorizontalLine(canvas);
        drawRotateIcon(canvas);
        drawUpDownIcon(canvas);

        if (mDebugMode) {
            drawDebugAxisCanvasTransform(canvas);
            drawDebugAxisInWorldSpace(canvas, mMainTransform, 200);
            debugDrawInput(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF input = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(input);
                break;
            case MotionEvent.ACTION_UP:
                mMoveMode = MoveMode.NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMoveMode == MoveMode.TRANSLATE)
                    moveVertical(input);
                else if (mMoveMode == MoveMode.ROTATE)
                    moveRotate(input);
                break;
        }

        invalidate();
        return true;
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------

    public void setDebugMode(boolean debug) {
        mDebugMode = debug;
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    /**
     * Set the current angle listener to receive angle events.
     *
     * @param listener listener for receiving the current angle.
     */
    public void setAngleListener(@Nullable AngleListener listener) {
        mAngleListener = listener;
        if (listener != null) {
            listener.onAngleUpdate(0.0f);
        }
    }

    /**
     * Set the desired foot for the widget. This swaps the sides that the rotation and translation
     * widgets are on.
     *
     * @param side the new foot side to set.
     */
    public void setFootSide(FootSide side) {
        if (side != mFootSide) {
            if (side == FootSide.LEFT)
                mMainTransform.preRotate(-180);
            else if (side == FootSide.RIGHT)
                mMainTransform.preRotate(180);
        }

        mFootSide = side;
        invalidate();
        sendAngleToListener();
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    //region SETUP

    private void setupLinePaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.SQUARE);
        mLinePaint.setColor(mPaintColor);
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mPaintStrokeWidth);

        //setup debug paint
        mDebugPaint = new Paint(mLinePaint);
        mDebugPaint.setStrokeWidth(3);
    }

    //endregion

    //region DRAW

    private void drawHorizontalLine(Canvas canvas) {
        canvas.save();

        canvas.setMatrix(mMainTransform);
        final float maxExtent = Math.max(mViewAreaInWindow.width(), mViewAreaInWindow.height()) * 2;
        canvas.drawLine(-maxExtent, 0, maxExtent, 0, mLinePaint);

        canvas.restore();
    }

    private void drawVerticalCenterLine(Canvas canvas) {
        int x = mViewAreaInWindow.width() / 2;
        canvas.drawLine(x, 0, x, mViewAreaInWindow.height(), mLinePaint);
    }

    private void drawRotateIcon(Canvas canvas) {
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);
        mScratchMatrix.preConcat(mRotateIconOffsetTransform);
        mScratchMatrix.preConcat(mRotateIconTransform);

        canvas.save();
        canvas.setMatrix(mScratchMatrix);
        if (mDebugMode) {
            mDebugPaint.setColor(Color.MAGENTA);
            canvas.drawRect(mRotateIcon.getBounds(), mDebugPaint);
        }
        mRotateIcon.draw(canvas);
        canvas.restore();
    }

    private void drawUpDownIcon(Canvas canvas) {
        float[] iconAnchorPoint = getUpDownIconWorldSpaceCenter();

        mScratchMatrix.reset();
        mScratchMatrix.setTranslate(iconAnchorPoint[0], iconAnchorPoint[1]);
        mScratchMatrix.postTranslate(0, mDistanceFromTopOfScreen);
        mScratchMatrix.postConcat(mUpDownIconTransform);

        canvas.save();
        canvas.setMatrix(mScratchMatrix);
        if (mDebugMode) {
            mDebugPaint.setColor(Color.MAGENTA);
            canvas.drawRect(mUpDownIcon.getBounds(), mDebugPaint);
        }
        mUpDownIcon.draw(canvas);
        canvas.restore();
    }

    //endregion

    //region INPUT

    private void sendAngleToListener() {
        if (mAngleListener != null) {
            float angle = mAngle;
            if (mFootSide == FootSide.RIGHT)
                angle = -mAngle;

            mAngleListener.onAngleUpdate(angle);
        }
    }

    private void touchStart(PointF input) {
        mLastTouchPoint.set(input);
        mPrevMovePoint.set(input);

        float[] upDownIconCenter = getUpDownIconWorldSpaceCenter();
        float[] rotateIconCenter = getRotateIconWorldSpaceCenter();

        boolean touchUpDownIcon = touchIconRadiusCheck(input, new PointF(upDownIconCenter[0], upDownIconCenter[1]), mInputTouchRadius);
        boolean touchRotate = touchIconRadiusCheck(input, new PointF(rotateIconCenter[0], rotateIconCenter[1]), mInputTouchRadius);

        if (touchUpDownIcon) {
            mMoveMode = MoveMode.TRANSLATE;
        } else if (touchRotate) {
            mMoveMode = MoveMode.ROTATE;
        }
    }

    private boolean touchIconRadiusCheck(PointF input, PointF iconCenter, float radius) {
        PointF delta = pointSubtract(input, iconCenter);
        return delta.length() <= radius;
    }

    private void moveVertical(PointF input) {
        PointF delta = pointSubtract(input, mPrevMovePoint);
        mPrevMovePoint.set(input);
        mMainTransform.postTranslate(0, delta.y);

        // Confine the center position to the screen by undoing any extra delta translation
        float[] center = {0, 0};
        mMainTransform.mapPoints(center);
        float bottomEdge = mViewAreaInWindow.bottom;
        float topEdge = mDistanceFromTopOfScreen;
        if (center[1] > bottomEdge) {
            float deltaY = bottomEdge - center[1];
            mMainTransform.postTranslate(0, deltaY);
        } else if (center[1] < topEdge) {
            float deltaY = topEdge - center[1];
            mMainTransform.postTranslate(0, deltaY);
        }
    }

    private void moveRotate(PointF input) {
        float[] center = {0, 0};
        mMainTransform.mapPoints(center);

        // Calculate the angles between the previous and current input points to determine a delta
        float anglePrev = getAngleBetweenPoints(center[0], center[1], mPrevMovePoint.x, mPrevMovePoint.y);
        float angleCurrent = getAngleBetweenPoints(center[0], center[1], input.x, input.y);
        float angleDelta = clampAngle(angleCurrent - anglePrev);

        // Apply the delta to the current angle, and clamp
        float newAngle = clampAngle(mAngle + angleDelta);
        float clampedAngle = clamp(-mMaximumAngle, mMaximumAngle, newAngle);

        // Adjust the delta by the clamped amount, ensuring the angle and delta never go across the boundary
        angleDelta += clampedAngle - newAngle;

        // Store the clamped absolute and delta angles, apply the delta to the main transform
        mAngle = clampedAngle;
        mMainTransform.preRotate(-angleDelta);

        sendAngleToListener();

        mPrevMovePoint.set(input);
    }

    //endregion

    //region MATH

    private float getAngleBetweenPoints(float startX, float startY, float endX, float endY) {
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        angle *= -1;
        return clampAngle(angle);
    }

    private PointF pointSubtract(PointF a, PointF b) {
        return new PointF(a.x - b.x, a.y - b.y);
    }

    /**
     * @return the rotate icon's center position in the world / input space, relative to the window
     * without the status bar height.
     */
    private float[] getRotateIconWorldSpaceCenter() {
        // Concatenate the main transform with the offset, accounting for the status bar height
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);
        mScratchMatrix.preConcat(mRotateIconOffsetTransform);
        mScratchMatrix.postTranslate(0, -mDistanceFromTopOfScreen);

        float[] position = {0, 0};
        mScratchMatrix.mapPoints(position);
        return position;
    }

    /**
     * @return the up-down icon's center position in the world / input space, relative to the window
     * without the status bar height.
     */
    private float[] getUpDownIconWorldSpaceCenter() {
        mScratchMatrix.reset();
        mScratchMatrix.set(mMainTransform);

        // Store the origin of the main transform
        float[] centerWorldSpace = {0, 0};
        mScratchMatrix.mapPoints(centerWorldSpace);

        // Determine a forward vector to determine the base angle
        float[] forwardAxis = {10, 0};
        mScratchMatrix.mapVectors(forwardAxis);
        float directionMultiplier = forwardAxis[0] > 0.0f ? -1.0f : 1.0f;

        // Calculate how far along the main rotated line to anchor the icon that matches the
        // fixed distance from center
        float distOfIconFromOrigin = mDistanceToIconsFromCenter * directionMultiplier;
        float distanceAlongRotatedLine = distOfIconFromOrigin / (float) Math.cos(Math.toRadians(mAngle));

        // Create a transform that represents the distance along the main axis at the calculated distance
        mScratchMatrix.reset();
        mScratchMatrix.setTranslate(distanceAlongRotatedLine, 0);
        mScratchMatrix.postRotate(-mAngle);
        mScratchMatrix.postTranslate(centerWorldSpace[0], centerWorldSpace[1]);
        mScratchMatrix.postTranslate(0, -mDistanceFromTopOfScreen);

        // The transformed origin is the world-space anchor point
        float[] iconAnchorPoint = {0, 0};
        mScratchMatrix.mapPoints(iconAnchorPoint);
        return iconAnchorPoint;
    }

    private float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp a degrees angle value between [-180, 180].
     *
     * @param angle an angle in degrees to constrain within the bounds.
     * @return an angle between [-180, 180].
     */
    private float clampAngle(float angle) {
        while (angle > 180.0f)
            angle -= 360.0f;
        while (angle < -180.0f)
            angle += 360.0f;
        return angle;
    }

    //endregion

    //region DEBUG

    private void drawDebugAxisCanvasTransform(Canvas canvas) {
        final float axisLength = 200;
        canvas.save();
        canvas.setMatrix(mMainTransform);
        mDebugPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, axisLength, 0, mDebugPaint);
        mDebugPaint.setColor(Color.GREEN);
        canvas.drawLine(0, 0, 0, axisLength, mDebugPaint);
        canvas.restore();
    }

    private void drawDebugAxisInWorldSpace(Canvas canvas, Matrix transform, float axisLength) {
        float[] center = {0, 0};
        float[] xAxis = {axisLength, 0};
        float[] yAxis = {0, axisLength};
        transform.mapPoints(center);
        transform.mapPoints(xAxis);
        transform.mapPoints(yAxis);
        mDebugPaint.setColor(Color.RED);
        canvas.drawLine(center[0], center[1], xAxis[0], xAxis[1], mDebugPaint);
        mDebugPaint.setColor(Color.GREEN);
        canvas.drawLine(center[0], center[1], yAxis[0], yAxis[1], mDebugPaint);
    }

    private void debugDrawInput(Canvas canvas) {
        float radius = 10;
        mDebugPaint.setColor(Color.YELLOW);
        canvas.drawCircle(mLastTouchPoint.x, mLastTouchPoint.y, radius, mDebugPaint);

        mDebugPaint.setColor(Color.CYAN);
        canvas.drawCircle(mPrevMovePoint.x, mPrevMovePoint.y, radius, mDebugPaint);

        float[] upDownIconPos = getUpDownIconWorldSpaceCenter();
        mDebugPaint.setColor(Color.GRAY);
        canvas.drawCircle(upDownIconPos[0], upDownIconPos[1], mInputTouchRadius, mDebugPaint);

        float[] rotateIconPos = getRotateIconWorldSpaceCenter();
        mDebugPaint.setColor(Color.GRAY);
        canvas.drawCircle(rotateIconPos[0], rotateIconPos[1], mInputTouchRadius, mDebugPaint);
    }

    //endregion

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
            getGlobalVisibleRect(mViewAreaInWindow);
            mDistanceFromTopOfScreen = mViewAreaInWindow.top;
        }
    };

    //endregion
}
