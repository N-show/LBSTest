package com.sony.edittextfirework;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nsh on 2017/12/11. 上午9:52
 */

public class Firework {

    private final String TAG = this.getClass().getSimpleName();

    private final static int DEFAULT_ELEMENT_COUNT = 12;
    private final static float DEFAULT_ELEMENT_SIZE = 8;
    private final static int DEFAULT_DURATION = 400;
    private final static float DEFAULT_LAUNCH_SPEED = 18;
    private final static float DEFAULT_WIND_SPEED = 6;
    private final static float DEFAULT_GRAVITY = 6;

    private Location location;
    private int windDirection; //1 or -1
    private float lanuchSpeed;
    private float gravity;
    private float windSpeed;
    private float elementSize;
    private int duration;
    private int count;
    private int[] colors;
    private int color;

    private ArrayList<Element> elements = new ArrayList<>();
    private Paint mPaint;

    public Firework(Location location, int windDirection) {
        this.location = location;
        this.windDirection = windDirection;

        lanuchSpeed = DEFAULT_LAUNCH_SPEED;
        gravity = DEFAULT_GRAVITY;
        windSpeed = DEFAULT_WIND_SPEED;
        elementSize = DEFAULT_ELEMENT_SIZE;
        duration = DEFAULT_DURATION;
        count = DEFAULT_ELEMENT_COUNT;
        colors = baseColors;

        init();

    }

    private void init() {

        Random random = new Random();
        color = colors[random.nextInt(colors.length)];

        for (int i = 0; i < count; i++) {
            elements.add(new Element(color, Math.toRadians(random.nextInt(180)), random.nextFloat() * lanuchSpeed));
        }

        mPaint = new Paint();
        mPaint.setColor(color);

    }

    private static final int[] baseColors = {0xFFFF43, 0x00E500, 0x44CEF6, 0xFF0040, 0xFF00FFB7, 0x008CFF
            , 0xFF5286, 0x562CFF, 0x2C9DFF, 0x00FFFF, 0x00FF77, 0x11FF00, 0xFFB536, 0xFF4618, 0xFF334B, 0x9CFA18};

    static class Location {
        public float x;
        public float y;

        public Location(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
