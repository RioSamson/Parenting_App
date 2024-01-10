package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.HashSet;
import java.util.Set;

public class CoinImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Interpolator mInterpolator = new DecelerateInterpolator();
    private Drawable mFrontDrawable;
    private Drawable mReversetDrawable;
    private CoinAnimation.CoinAnimationListener mCoinAnimationListener;
    private Set<Animation> mOtherAnimation = new HashSet<Animation>();

    public static final int RESULT_HEAD = 1; // Head
    public static final int RESULT_TAIL = -1; // Tail

    private int circle_count;
    private int mXAxisDir;
    private int mYAxisDir;
    private int mZAxisDir;

    private int mResult;
    private int mDuration;
    private int mStartOffset;

    public CoinImageView(Context context) {
        super(context);
        setCoinDrawableIfNecessage();
    }

    public CoinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoinAnimation);

        circle_count = a.getInteger(R.styleable.CoinAnimation_circleCount, context.getResources().getInteger(R.integer.coin_default_circle_count));
        mXAxisDir = a.getInteger(R.styleable.CoinAnimation_xAxisDir, context.getResources().getInteger(R.integer.coin_default_xAxisDir));
        mYAxisDir = a.getInteger(R.styleable.CoinAnimation_yAxisDir, context.getResources().getInteger(R.integer.coin_default_yAxisDir));
        mZAxisDir = a.getInteger(R.styleable.CoinAnimation_zAxisDir, context.getResources().getInteger(R.integer.coin_default_zAxisDir));
        mResult = a.getInteger(R.styleable.CoinAnimation_result, context.getResources().getInteger(R.integer.coin_default_result));

        mFrontDrawable = a.getDrawable(R.styleable.CoinAnimation_headDrawable);
        mReversetDrawable = a.getDrawable(R.styleable.CoinAnimation_tailDrawable);

        mDuration = a.getInteger(R.styleable.CoinAnimation_durationTime, context.getResources().getInteger(R.integer.coin_default_duration_time));
        mStartOffset = a.getInteger(R.styleable.CoinAnimation_startOffset, context.getResources().getInteger(R.integer.coin_default_start_offset));

        a.recycle();

        setCoinDrawableIfNecessage();
    }

    public CoinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoinAnimation, defStyleAttr, 0);

        circle_count = a.getInteger(R.styleable.CoinAnimation_circleCount, context.getResources().getInteger(R.integer.coin_default_circle_count));
        mXAxisDir = a.getInteger(R.styleable.CoinAnimation_xAxisDir, context.getResources().getInteger(R.integer.coin_default_xAxisDir));
        mYAxisDir = a.getInteger(R.styleable.CoinAnimation_yAxisDir, context.getResources().getInteger(R.integer.coin_default_yAxisDir));
        mZAxisDir = a.getInteger(R.styleable.CoinAnimation_zAxisDir, context.getResources().getInteger(R.integer.coin_default_zAxisDir));
        mResult = a.getInteger(R.styleable.CoinAnimation_result, context.getResources().getInteger(R.integer.coin_default_result));

        mFrontDrawable = a.getDrawable(R.styleable.CoinAnimation_headDrawable);
        mReversetDrawable = a.getDrawable(R.styleable.CoinAnimation_tailDrawable);

        mDuration = a.getInteger(R.styleable.CoinAnimation_durationTime, context.getResources().getInteger(R.integer.coin_default_duration_time));
        mStartOffset = a.getInteger(R.styleable.CoinAnimation_startOffset, context.getResources().getInteger(R.integer.coin_default_start_offset));

        a.recycle();

        setCoinDrawableIfNecessage();
    }

    private void setCoinDrawableIfNecessage() {
        if (mFrontDrawable == null) {
            mFrontDrawable = getDrawable();
        }
        if (mReversetDrawable == null) {
            mReversetDrawable = getDrawable();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setCoinDrawableIfNecessage();
    }

    public CoinImageView setCircleCount(int circleCount) {
        this.circle_count = circleCount;
        return this;
    }

    public CoinImageView setXAxisDirection(int xAxisDir) {
        if(Math.abs(xAxisDir) > 1){
            throw new RuntimeException("Math.abs(Direction) must be less than 1");
        }
        this.mXAxisDir = xAxisDir;
        return this;
    }

    public CoinImageView setYAxisDirection(int yAxisDir) {
        if(Math.abs(yAxisDir) > 1){
            throw new RuntimeException("Math.abs(Direction) must be less than 1");
        }
        this.mYAxisDir = yAxisDir;
        return this;
    }

    public CoinImageView setResult(int result) {
        if(Math.abs(result) != 1){
            throw new RuntimeException("Math.abs(Direction) must be 1");
        }
        this.mResult = result;
        return this;
    }

    public CoinImageView setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public CoinImageView setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public CoinImageView clearOtherAnimation() {
        mOtherAnimation.clear();
        return this;
    }

    public CoinImageView setCoinAnimationListener(CoinAnimation.CoinAnimationListener coinAnimationListener) {
        this.mCoinAnimationListener = coinAnimationListener;
        return this;
    }

    public void startFlipCoin() {
        clearAnimation();

        CoinAnimation CoinAnimation = new CoinAnimation(circle_count, mXAxisDir, mYAxisDir, mZAxisDir, mResult);
        CoinAnimation.setDuration(mDuration);
        CoinAnimation.setStartOffset(mStartOffset);
        CoinAnimation.setInterpolator(mInterpolator);
        CoinAnimation.setCoinAnimationListener(new QTCoinAnimationListener(mCoinAnimationListener));

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(CoinAnimation);

        for (Animation animation : mOtherAnimation) {
            animationSet.addAnimation(animation);
        }
        startAnimation(animationSet);
    }

    public class QTCoinAnimationListener implements CoinAnimation.CoinAnimationListener {
        private CoinAnimation.CoinAnimationListener mTossAnimationListener;

        public QTCoinAnimationListener(CoinAnimation.CoinAnimationListener tossAnimationListener) {
            mTossAnimationListener = tossAnimationListener;
        }

        @Override
        public void onDrawableChange(int result, CoinAnimation animation) {
            switch (result) {
                case CoinAnimation.RESULT_FRONT:
                    setImageDrawable(mFrontDrawable);
                    break;
                case CoinAnimation.RESULT_REVERSE:
                    setImageDrawable(mReversetDrawable);
                    break;
            }
            if (mTossAnimationListener != null) {
                mTossAnimationListener.onDrawableChange(result, animation);
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mTossAnimationListener != null) {
                mTossAnimationListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mTossAnimationListener != null) {
                mTossAnimationListener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            if (mTossAnimationListener != null) {
                mTossAnimationListener.onAnimationRepeat(animation);
            }
        }
    }
}
