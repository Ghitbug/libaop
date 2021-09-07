package com.gh.libaop;


import android.app.Activity;
import android.util.Log;

import com.gh.libaop.util.ActivityStackManager;
import com.gh.libaop.util.ToastUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.gh.libaop.R.string;

import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionsAspect {
    public PermissionsAspect() {
    }

    @Pointcut("execution(@com.gh.libaop.Permissions * *(..))")
    private void method() {
    }

    @Around("method() && @annotation(permissions)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permissions permissions) {
        final Activity activity = ActivityStackManager.getInstance().getTopActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            Log.i("aroundJoinPoint", "选择了权限");
            XXPermissions.with(activity).permission(permissions.value()).request(new OnPermission() {
                public void hasPermission(List<String> granted, boolean all) {
                    if (all) {
                        Log.i("aroundJoinPoint", "已经授权");

                        try {
                            joinPoint.proceed();
                        } catch (Throwable var4) {
                        }
                    }

                }

                public void noPermission(List<String> denied, boolean quick) {
                    if (permissions.rejectExecute()) {
                        if (quick) {
                            if (permissions.isMsg()) {
                                ToastUtils.show(string.common_permission_fail);
                            }

                            XXPermissions.startPermissionActivity(activity, denied);
                        } else if (permissions.isMsg()) {
                            ToastUtils.show(string.common_permission_hint);
                        }
                    } else {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable var4) {
                        }
                    }

                }
            });
        }
    }
}
