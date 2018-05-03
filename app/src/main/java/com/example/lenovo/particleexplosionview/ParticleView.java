package com.example.lenovo.particleexplosionview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;

/**
 * 因为该view要全屏显示在页面上所以就免去测量的部分
 */
public class ParticleView extends View {
    private ValueAnimator mAnimator;
    private Particle[][] particles;
    private Paint mPaint;
    private OnAnimationListener mOnAnimationListener;

    public ParticleView(Context context) {
        super(context);
        init(context);
    }

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ParticleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        attachActivity(context);
    }

    /**
     * 把view自身对象添加到DecorView
     *
     * @param context
     */
    private void attachActivity(Context context) {
        //获取根布局
        ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        //设置布局参数
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //添加到根布局
        decorView.addView(this, params);
    }

    public void startBoom(final View view, final OnAnimationListener mOnAnimationListener) {
        this.mOnAnimationListener = mOnAnimationListener;
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() == 0 || (mAnimator != null && mAnimator.isRunning())) {
            return;
        }
        int[] locations = new int[2];
        //获取view在window左上角的坐标
        view.getLocationInWindow(locations);
        //获取view的区域
        Rect vRect = new Rect(locations[0], locations[1], locations[0] + view.getMeasuredWidth(), locations[1] + view.getMeasuredHeight());
        particles = getPointFromView(getViewBitmap(view), vRect);
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mOnAnimationListener.onAnimationEnd(view, mAnimator);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mOnAnimationListener.onAnimationStart(view, mAnimator);
            }
        });
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimator != null) {
            drawCicle(canvas);
        }
    }

    /**
     * 循环画点
     *
     * @param canvas
     */
    private void drawCicle(Canvas canvas) {
        for (Particle[] particle : particles) {
            for (Particle p : particle) {
                //改变每个点的参数
                p.updata((Float) mAnimator.getAnimatedValue());
                mPaint.setColor(p.getColor());
                mPaint.setAlpha((int) (Color.alpha(p.getColor()) * p.getAlpha()));
                canvas.drawCircle(p.getCenterX(), p.getCenterY(), p.getRadius(), mPaint);
            }
        }
    }

    /**
     * 获取view缓存的bitmap
     *
     * @param view
     * @return
     */
    private Bitmap getViewBitmap(View view) {
        //开启View缓存Bitmap
        view.setDrawingCacheEnabled(true);
        //获取View缓存之后的bitmap
        Bitmap bitmap = view.getDrawingCache();
        Bitmap bitmap1;
        if (bitmap != null) {
            bitmap1 = Bitmap.createBitmap(bitmap);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
            bitmap1 = null;
        }

        return bitmap1;
    }

    /**
     * 把view的bitmap分成很多点，再获取每个点上的颜色和坐标
     *
     * @param bitmap
     * @param rect
     * @return
     */
    private Particle[][] getPointFromView(Bitmap bitmap, Rect rect) {
        Particle[][] particles;
        //计算总共有几行
        int rowCount = rect.height() / Particle.BLOCK_WIDTH;
        //计算总过有几列
        int columnCount = rect.width() / Particle.BLOCK_WIDTH;
        particles = new Particle[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                //获取点在bitmap上的x坐标
                int x = j * Particle.BLOCK_WIDTH + Particle.BLOCK_WIDTH / 2;
                int y = i * Particle.BLOCK_WIDTH + Particle.BLOCK_WIDTH / 2;
                //获取该点的颜色
                int color = bitmap.getPixel(x, y);
                particles[i][j] = new Particle(color, i, j, rect);
            }
        }
        return particles;
    }

    public interface OnAnimationListener {
        //当前正在执行的view
        void onAnimationStart(View v, Animator animation);

        void onAnimationEnd(View v, Animator animation);
    }
}
