package com.example.codoon.myapplication.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Util
 * 文件名：SportMyView
 * 创建时间：2018/10/22 下午1:55
 * 描述：TODO
 */
public class SportMyView extends View {
    private int defalutSize;
    public SportMyView(Context context) {
        super(context);
    }

    public SportMyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SportMyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(defalutSize, widthMeasureSpec);
        int height = getMySize(defalutSize, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width, height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆心的横坐标为当前的View的左边起始位置
        int centerX = getWidth() / 2;
        //圆心的纵坐标为当前的View的顶部起始位置
        int centerY = getHeight()-200;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle(centerX, centerY, 100, paint);

        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        //设置实心
        paint1.setStyle(Paint.Style.FILL);
        // 设置画笔的锯齿效果
        paint1.setAntiAlias(true);
        //实例化路径
        Path path = new Path();
        path.moveTo(centerX-40, centerY-50);// 此点为多边形的起点
        path.lineTo(centerX-40, centerY+50);
        path.lineTo(centerX+50, centerY);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint1);

    }
}
