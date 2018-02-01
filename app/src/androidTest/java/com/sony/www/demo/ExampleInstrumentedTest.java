package com.sony.www.demo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private UiDevice mDevice;

    @Test
    public void useAppContext() throws Exception {

        String BASIC_SAMPLE_PACKAGE = "com.android.settings";

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sony.www.demo", appContext.getPackageName());

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        mDevice.pressHome();

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);


        UiObject systemButton = mDevice.findObject(new UiSelector().text("Sound"));



        systemButton.click();

        UiObject back = mDevice.findObject(new UiSelector().description("Navigate up"));
        back.click();

        UiScrollable settings = new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
        UiObject about = settings.getChildByText(new UiSelector().className("android.widget.LinearLayout"), "System");
        about.click();



    }
}
