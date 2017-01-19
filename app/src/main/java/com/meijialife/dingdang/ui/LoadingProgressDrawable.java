package com.meijialife.dingdang.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.meijialife.dingdang.R;

import static android.graphics.Color.TRANSPARENT;


/**
 * 转圈加载动画
 * Created by jiangjianxiong on 16/6/12.
 */
public class LoadingProgressDrawable extends Drawable {

    private static int[] Loadings = {
            R.drawable.load_progress_1,
            R.drawable.load_progress_3,
            R.drawable.load_progress_4,
            R.drawable.load_progress_6,
            R.drawable.load_progress_7,
            R.drawable.load_progress_8,
            R.drawable.load_progress_9,
            R.drawable.load_progress_10,
            R.drawable.load_progress_11,
            R.drawable.load_progress_12
    };
    private Paint mPaint;
    private int mLevel;
    private Context context;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public LoadingProgressDrawable(Context context) {
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Loadings[getIndex()], options);

        int left = getBounds().right / 2 - options.outWidth / 2;
        int top = getBounds().bottom / 2 - options.outHeight / 2;

        canvas.drawBitmap(bitmap, left, top, mPaint);
    }

    private int getIndex() {
        int index = mLevel / 1000;
        if (index < 0) {
            index = 0;
        } else if (index >= Loadings.length) {
            index = Loadings.length - 1;
        }
        return index;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return TRANSPARENT;
    }

    @Override
    protected boolean onLevelChange(int level) {
        this.mLevel = level;
        this.invalidateSelf();
        return true;
    }
}