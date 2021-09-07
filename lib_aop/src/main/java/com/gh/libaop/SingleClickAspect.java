package com.gh.libaop;


import android.util.Log;
import android.view.View;
import java.util.Calendar;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SingleClickAspect {
    private long mLastTime;
    private String mLastId;

    public SingleClickAspect() {
    }

    @Pointcut("execution(@com.gh.libaop.SingleClick * *(..))")
    private void method() {
    }

    @Around("method() && @annotation(singleClick)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        String name = joinPoint.getSignature().getName();
        Log.d("SingleClickAspect", "点击了" + name);
        View view = null;
        Object[] var5 = joinPoint.getArgs();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Object arg = var5[var7];
            if (arg instanceof View) {
                view = (View)arg;
            }
        }

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (view != null) {
            String id = view.getId() + "";
            if (currentTime - this.mLastTime < singleClick.value() && this.mLastId.equals(id)) {
                Log.d("SingleClick", "发生快速点击");
                return;
            }

            this.mLastId = id;
        } else {
            if (currentTime - this.mLastTime < singleClick.value() && this.mLastId.equals(name)) {
                Log.d("SingleClick", "发生快速点击");
                return;
            }

            this.mLastId = name;
        }

        this.mLastTime = currentTime;
        Log.d("SingleClick", "执行了");
        joinPoint.proceed();
    }
}
