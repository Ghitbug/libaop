package com.gh.libaop;


import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import androidx.annotation.NonNull;

import com.gh.libaop.DebugLog;
import com.gh.libaop.util.ActivityStackManager;

import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class DebugLogAspect {
    public DebugLogAspect() {
    }

    @Pointcut("execution(@com.wcy.app.lib.aop.DebugLog *.new(..))")
    public void constructor() {
    }

    @Pointcut("execution(@com.wcy.app.lib.aop.DebugLog * *(..))")
    public void method() {
    }

    @Around("(method() || constructor()) && @annotation(debugLog)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, DebugLog debugLog) throws Throwable {
        this.enterMethod(joinPoint, debugLog);
        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        this.exitMethod(joinPoint, debugLog, result, TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos));
        return result;
    }

    private void enterMethod(ProceedingJoinPoint joinPoint, DebugLog debugLog) {
        CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
        String className = codeSignature.getDeclaringType().getName();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        StringBuilder builder = this.getMethodLogInfo(className, methodName, parameterNames, parameterValues);
        this.log(debugLog.value(), builder.toString());
        String section = builder.toString().substring(2);
        Trace.beginSection(section);
    }

    @NonNull
    private StringBuilder getMethodLogInfo(String className, String methodName, String[] parameterNames, Object[] parameterValues) {
        StringBuilder builder = new StringBuilder("⇢ ");
        builder.append(className).append(".").append(methodName).append('(');

        for(int i = 0; i < parameterValues.length; ++i) {
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(parameterNames[i]).append('=');
            builder.append(parameterValues[i].toString());
        }

        builder.append(')');
        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        return builder;
    }

    private void exitMethod(ProceedingJoinPoint joinPoint, DebugLog debugLog, Object result, long lengthMillis) {
        if (ActivityStackManager.getInstance().isDebug()) {
            Trace.endSection();
            Signature signature = joinPoint.getSignature();
            String className = signature.getDeclaringType().getName();
            String methodName = signature.getName();
            StringBuilder builder = (new StringBuilder("⇠ ")).append(className).append(".").append(methodName).append(" [").append(lengthMillis).append("ms]");
            if (signature instanceof MethodSignature && ((MethodSignature)signature).getReturnType() != Void.TYPE) {
                builder.append(" = ");
                builder.append(result.toString());
            }

            this.log(debugLog.value(), builder.toString());
        }
    }

    private void log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
