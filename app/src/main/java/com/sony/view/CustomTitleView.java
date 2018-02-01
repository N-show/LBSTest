package com.sony.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.sony.www.demo.R;

/**
 * Created by nsh on 2017/12/6. 下午3:48
 */

public class CustomTitleView extends View {

    public final String TAG = this.getClass().getSimpleName();
    //文本内容
    private String mTitleText;
    //文本颜色
    private int mTitleTextColor;
    //文本大小
    private int mTitleTextSize;
    //画笔
    private Paint mPaint;
    private Rect mBound;


    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //获取自定义样式属性
    public CustomTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取样式属性，参数 自定义属性集合attr是在xml中定义的属性  自定义view的类  默认值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        //获取自定义属性的个数
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            //循环获取代表属性的值
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    mTitleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        //释放资源
        typedArray.recycle();

        //初始化画笔
        mPaint = new Paint();
        //设置颜色和大小
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTitleTextSize);
//        mPaint.setColor(mTitleTextColor);

        mBound = new Rect();
        //得到绘制文本的宽高   根据以上的设置的大小来量取字符的长度
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        Log.d(TAG, mBound.width() + "---" + mBound.height());


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }


        setMeasuredDimension(width, height);
        Log.d(TAG, width+"---width");
        Log.d(TAG, height+"---height");
        Log.d(TAG, getPaddingLeft()+"---getPaddingLeft");
        Log.d(TAG, getPaddingRight()+"---getPaddingRight");
        Log.d(TAG, getPaddingTop()+"---getPaddingTop");
        Log.d(TAG, getPaddingBottom()+"---getPaddingBottom");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 - mBound.height() / 2, mPaint);

        Log.d(TAG, getWidth() + "---");
        Log.d(TAG, getHeight() + "---");
        Log.d(TAG, mBound.width() + "---");
        Log.d(TAG, mBound.height() + "---");


    }
}









