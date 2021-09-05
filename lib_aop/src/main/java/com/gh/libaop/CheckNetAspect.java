package com.gh.libaop;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;

import com.gh.libaop.util.ActivityStackManager;
import com.gh.libaop.util.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CheckNetAspect {
    public CheckNetAspect() {
    }

    @Pointcut("execution(@com.wcy.app.lib.aop.CheckNet * *(..))")
    public void method() {
    }

    @Around("method() && @annotation(checkNet)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, CheckNet checkNet) throws Throwable {
        Application application = ActivityStackManager.getInstance().getApplication();
        if (application != null) {
            ConnectivityManager manager = (ConnectivityManager)ContextCompat.getSystemService(application, ConnectivityManager.class);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    ToastUtils.show(R.string.common_network);
                    return;
                }
            }
        }

        joinPoint.proceed();
    }
}
