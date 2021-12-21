package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BaseObject {
    protected float x, y;
    protected int width, height;
    protected Rect rect;
    protected Bitmap bm;

    public BaseObject(){

    }

    public BaseObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Rect getRect() {
        return new Rect((int)this.x, (int)this.y, (int)this.x+this.width, (int)this.y+this.height);
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
