package com.gh.libaop.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public ToastUtils() {
    }

    public static void show(int id) {
        Context context = ActivityStackManager.getInstance().getApplication();
        Toast.makeText(context, context.getResources().getString(id), 1).show();
    }
}
