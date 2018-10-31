package com.example.codoon.myapplication.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.codoon.myapplication.R;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Util
 * 文件名：MyView
 * 创建时间：2018/10/19 上午10:08
 * 描述：自定义view实心圆
 */
public class MyView extends View {
    private int defalutSize;
    private Context context;

    public MyView(Context context) {
        super(context);
    }

    public MyView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        //第二个参数就是我们在styles.xml文件中的<declare-styleable>标签
        //即属性集合的标签，在R文件中名称为R.styleable+name
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        //第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        //第二个参数为，如果没有设置这个属性，则设置的默认的值
        defalutSize = a.getDimensionPixelSize(R.styleable.MyView_default_size, 100);
        //最后记得将TypedArray对象回收
        a.recycle();
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

    /**
     * 绘制实心圆，并在其中添加GO！字体，颜色为白色#fff
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        super.onDraw(canvas);
        //圆心的横坐标为当前的View的左边起始位置
        int centerX = getWidth() / 2;
        //圆心的纵坐标为当前的View的顶部起始位置
        int centerY = getHeight() / 2 + 100;

        Paint paint = new Paint();
        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(150);
        paint1.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        paint.setColor(Color.GREEN);
        //开始绘制
        canvas.drawCircle(centerX, centerY, 200, paint);
        canvas.drawText("GO!",centerX-100,centerY+50,paint1);

    }

}
