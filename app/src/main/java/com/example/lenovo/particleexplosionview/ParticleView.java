package com.example.lenovo.particleexplosionview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 因为该view要全屏显示在页面上所以就免去测量的部分
 */
public class ParticleView extends View {
    public ParticleView(Context context) {
        super(context);
        initial(context);
    }

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context);
    }

    public ParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ParticleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initial(context);
    }
    private void initial(Context context) {
        attachActivity(context);
    }

    /**
     * 把view自身对象添加到DecorView
     * @param context
     */
    private void attachActivity(Context context) {
        //获取根布局
        ViewGroup decorView= (ViewGroup) ((Activity)context).getWindow().getDecorView();
        //设置布局参数
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //添加到根布局
        decorView.addView(this,params);
    }
}
