package com.gh.libaop.util;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

public final class ActivityStackManager implements ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLife";
    private static volatile ActivityStackManager sInstance;
    private final ArrayMap<String, Activity> mActivitySet = new ArrayMap();
    private Application mApplication;
    private String mCurrentTag;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        if (sInstance == null) {
            Class var0 = ActivityStackManager.class;
            synchronized(ActivityStackManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityStackManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Application application) {
        this.mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public Application getApplication() {
        return this.mApplication;
    }

    public Activity getTopActivity() {
        return (Activity)this.mActivitySet.get(this.mCurrentTag);
    }

    public void finishAllActivities() {
        this.finishAllActivities((Class)null);
    }

    @SafeVarargs
    public final void finishAllActivities(Class<? extends Activity>... classArray) {
        String[] keys = (String[])this.mActivitySet.keySet().toArray(new String[0]);
        String[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String key = var3[var5];
            Activity activity = (Activity)this.mActivitySet.get(key);
            if (activity != null && !activity.isFinishing()) {
                boolean whiteClazz = false;
                if (classArray != null) {
                    Class[] var9 = classArray;
                    int var10 = classArray.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        Class<? extends Activity> clazz = var9[var11];
                        if (activity.getClass() == clazz) {
                            whiteClazz = true;
                        }
                    }
                }

                if (!whiteClazz) {
                    activity.finish();
                    this.mActivitySet.remove(key);
                }
            }
        }

    }

    private static String getObjectTag(Object object) {
        return object.getClass().getName() + Integer.toHexString(object.hashCode());
    }

    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        this.mCurrentTag = getObjectTag(activity);
        this.mActivitySet.put(getObjectTag(activity), activity);
    }

    public void onActivityStarted(@NonNull Activity activity) {
        this.mCurrentTag = getObjectTag(activity);
    }

    public void onActivityResumed(@NonNull Activity activity) {
        this.mCurrentTag = getObjectTag(activity);
    }

    public void onActivityPaused(@NonNull Activity activity) {
    }

    public void onActivityStopped(@NonNull Activity activity) {
    }

    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    public void onActivityDestroyed(@NonNull Activity activity) {
        this.mActivitySet.remove(getObjectTag(activity));
        if (getObjectTag(activity).equals(this.mCurrentTag)) {
            this.mCurrentTag = null;
        }

    }

    public boolean isDebug() {
        try {
            ApplicationInfo info = this.mApplication.getApplicationInfo();
            return (info.flags & 2) != 0;
        } catch (Exception var2) {
            return false;
        }
    }
}
