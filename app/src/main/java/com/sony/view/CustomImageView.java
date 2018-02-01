package com.sony.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sony.www.demo.R;

/**
 * Created by nsh on 2017/12/7. 下午3:35
 */

public class CustomImageView extends View {

    private String mTitleText;
    private int mTitleColor;
    private float mTitleSize;
    private Bitmap mImage;
    private int mImageScale;
    private Paint mPaint;
    private Rect mRect;
    private Rect mTextRect;
    private int mWidth;
    private int mHeight;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);

        int indexCount = typedArray.getIndexCount();

        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.CustomImageView_titleText1:
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleColor:
                    mTitleColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleSize:
                    mTitleSize = typedArray.getDimension(attr, 10);
                    break;
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = typedArray.getInt(attr, 0);
                    break;
                default:
                    break;

            }
        }

        typedArray.recycle();

        mRect = new Rect();
        mPaint = new Paint();
        mTextRect = new Rect();
        mPaint.setTextSize(mTitleSize);
        //计算描绘字体范围的
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextRect);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            int desireByImage = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextRect.width();

            if (widthMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImage, desireByTitle);
                mWidth = Math.min(desire, widthSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextRect.height();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(heightSize, desire);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //边框
        mPaint.setStrokeWidth(14);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTitleColor);
        mPaint.setStyle(Paint.Style.FILL);


        //当字体的宽度大于设置的宽度
        if (mTextRect.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            //正常情况，将字体居中
            canvas.drawText(mTitleText, mWidth / 2 - mTextRect.width() * 1.0f / 2, mHeight - getPaddingBottom(), mPaint);
        }

        //取消使用掉的快
        mRect.bottom -= mTextRect.height();


        if (mImageScale == 0) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else {
            //计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mTextRect.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mTextRect.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }


    }
}
