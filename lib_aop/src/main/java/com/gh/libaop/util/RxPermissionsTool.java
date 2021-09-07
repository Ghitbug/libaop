package com.gh.libaop.util;


import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RxPermissionsTool {
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    public static final String PERMISSION_READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String PERMISSION_CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    public static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES";

    public RxPermissionsTool() {
    }

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private Activity mActivity;
        private List<String> permissionList;

        public Builder(@NonNull Activity activity) {
            this.mActivity = activity;
            this.permissionList = new ArrayList();
        }

        public Builder addPermission(@NonNull String permission) {
            if (!this.permissionList.contains(permission)) {
                this.permissionList.add(permission);
            }

            return this;
        }

        public List<String> initPermission() {
            List<String> list = new ArrayList();
            Iterator var2 = this.permissionList.iterator();

            while(var2.hasNext()) {
                String permission = (String)var2.next();
                if (ActivityCompat.checkSelfPermission(this.mActivity, permission) != 0) {
                    list.add(permission);
                }
            }

            if (list.size() > 0) {
                ActivityCompat.requestPermissions(this.mActivity, (String[])list.toArray(new String[list.size()]), 1);
            }

            return list;
        }
    }
}
