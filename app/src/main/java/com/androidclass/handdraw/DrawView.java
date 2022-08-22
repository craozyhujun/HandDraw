package com.androidclass.handdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {

    public Paint paint;
    private Bitmap cacheBitmap = null;
    private Canvas cacheCanvas = null;
    private Path path;
    private float preX;
    private float preY;

    public DrawView(Context context, @Nullable int width, int height) {
        super(context);
        // 创建一个与该View具有相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
        // 设置画笔的颜色
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        // 反锯齿
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取拖动事件发生的位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.moveTo(x,y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.quadTo(preX,preY,x,y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path,paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint bmpPaint = new Paint();
        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap,0,0,bmpPaint);
        canvas.drawPath(path,paint);
    }
}
