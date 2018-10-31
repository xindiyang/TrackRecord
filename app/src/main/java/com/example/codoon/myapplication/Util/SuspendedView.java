package com.example.codoon.myapplication.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Util
 * 文件名：SuspendedView
 * 创建时间：2018/10/22 下午3:38
 * 描述：TODO
 */
public class SuspendedView extends View {
    private int defalutSize;

    public SuspendedView(Context context) {
        super(context);
    }

    public SuspendedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SuspendedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        int centerY = getHeight() - 200;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle(centerX, centerY, 100, paint);

        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setStrokeWidth(15);
        canvas.drawLine(centerX - 25, centerY - 50, centerX - 25, centerY +50, paint1);
        canvas.drawLine(centerX+25,centerY - 50,centerX +25,centerY+50,paint1);
    }
}
