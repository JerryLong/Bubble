package com.dralong.bubbles;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by dralong on 2016/8/27.
 */
public class BubbleManager {
    private static BubbleManager mBubbleManager;
    private static Bitmap mDest;
    private BubbleCover mBubbleCover;
    private WindowManager mWindowManager;

    private BubbleManager() {

    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public static BubbleManager getInstance() {
        if (mBubbleManager == null) {
            mBubbleManager = new BubbleManager();
        }
        return mBubbleManager;
    }

    public void init(Activity activity) {
        if (mBubbleCover == null) {
            mBubbleCover = new BubbleCover(activity);
        }
        mBubbleCover.setStatusBarHeight(getStatusBarHeight(activity));
    }

    public void start(View target, float x, float y, BubbleCover.OnDragCompeteListener onDragCompeteListener) {
        if (mBubbleCover != null && mBubbleCover.getParent() == null) {
            mBubbleCover.setOnDragCompeteListener(onDragCompeteListener);
        } else {
            return;
        }

        mDest = drawViewToBitmap(target);
        target.setVisibility(View.INVISIBLE);
        mBubbleCover.setTarget(mDest);
        int[] locations = new int[2];
        target.getLocationOnScreen(locations);
        attachToWindow(target.getContext());
        mBubbleCover.init(locations[0], locations[1]);
    }

    public void update(float x, float y) {
        mBubbleCover.update(x, y);
    }

    public void finish(View target, float x, float y) {
        mBubbleCover.finish(target, x, y);
        mBubbleCover.setOnDragCompeteListener(null);

    }

    private Bitmap drawViewToBitmap(View view) {
        if (mBubbleCover == null) {
            mBubbleCover = new BubbleCover(view.getContext());
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (mDest == null || mDest.getWidth() != width || mDest.getHeight() != height) {
            mDest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(mDest);
        view.draw(c);
        return mDest;
    }

    private void attachToWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (mBubbleCover == null) {
            mBubbleCover = new BubbleCover(context);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowManager.addView(mBubbleCover, params);
    }

    public boolean isRunning() {
        if (mBubbleCover == null) {
            return false;
        } else if (mBubbleCover.getParent() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * please call it before animation start
     *
     * Notice: the unit is frame.
     *
     */
    public void setExplosionTime(int lifeTime) {
        BubbleParticle.setLifeTime(lifeTime);
    }

    public void setMaxDragDistance(int maxDistance) {
        if (mBubbleCover != null) {
            mBubbleCover.setMaxDragDistance(maxDistance);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = activity.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
