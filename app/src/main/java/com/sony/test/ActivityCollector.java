package com.sony.test;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsh on 2017/10/25.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();
    public static StringBuilder stringBuilder = new StringBuilder();

    public static void addActivity(Activity activity) {
        activities.add(activity);
        dayin();
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
        dayin();
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
                dayin();
            }
        }
    }

    public static void dayin() {
        int i = 1;
        for (Activity activity : activities) {
            String className = activity.getClass().getSimpleName();
            if (i == activities.size()) {
                stringBuilder.append(className);
            } else {
                stringBuilder.append(className + "---");
            }
            i++;

        }
        System.out.println(stringBuilder.toString());
    }
}
