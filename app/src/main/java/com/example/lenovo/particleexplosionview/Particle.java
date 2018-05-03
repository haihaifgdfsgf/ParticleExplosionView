package com.example.lenovo.particleexplosionview;

import android.graphics.Rect;

import java.util.Random;

/**
 * Created by 曹海 on 2018/5/3.
 * QQ：185493676
 */
public class Particle {
    public static final int BLOCK_WIDTH = 6;
    private float radius = BLOCK_WIDTH / 2;
    private float centerX;
    private float centerY;
    private int color;
    private int positionX;
    private int positionY;
    private Rect mRect;
    private Random mRandom;
    private float alpha;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Particle(int color, int positionX, int positionY, Rect mRect) {
        alpha = 1f;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.mRect = mRect;
        this.centerX = mRect.left + positionY * BLOCK_WIDTH + BLOCK_WIDTH / 2;
        this.centerY = mRect.top + positionX * BLOCK_WIDTH + BLOCK_WIDTH / 2;
        mRandom = new Random();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void updata(float factor) {
        centerX = centerX + factor * mRandom.nextInt(mRect.width()) * (mRandom.nextFloat() - 0.5f);
        centerY = centerY + factor * (mRect.height() / (mRandom.nextInt(4) + 1));
        radius = radius - factor * mRandom.nextInt(3);
        if (radius <= 0) {
            radius = 0;
        }
        alpha = 1f - factor;
    }
}
