package com.simple.xposetool;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.ColorInt;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposeMain implements IXposedHookLoadPackage {

    public static final String XPOSETEST = "XPOSETEST";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d(XPOSETEST, "handleLoadPackage entry");
//        Thread mainThread = Looper.getMainLooper().getThread();
//        StackTraceElement[] stackTrace = mainThread.getStackTrace();
//
//        for (StackTraceElement stackTraceElement : stackTrace) {
//            Log.d(XPOSETEST, stackTraceElement.toString());
//        }
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Context ctx = (Context)param.args[0];
                Log.d(XPOSETEST,"entry: "+ctx.getPackageName());
                try {
                    loadPlugin(ctx, lpparam);
                } catch (Exception e) {
                    Log.d(XPOSETEST,"error");
                    e.printStackTrace();
                }
            }
        });

//        if (lpparam.packageName.equals("com.example.testmode")) {
//
//            XposedHelpers.findAndHookMethod("com.example.testmode.Risk.RiskTest",
//                    lpparam.classLoader,
//                    "testStack",
//                    Context.class, new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            super.beforeHookedMethod(param);
//                        }
//
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            super.afterHookedMethod(param);
//                            Log.d(XPOSETEST,"afterHook");
//                            if (param.args[0] == null){
//                                Log.d(XPOSETEST,"test ->"+Arrays.toString(param.args));
//                            }
//                            try {
//                                Context ctx = (Context) param.args[0];
//
//                            } catch (Exception e) {
//                                Log.d(XPOSETEST,"terror:"+e.getMessage());
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//        }
    }

    public void hookMainStack(XC_MethodHook.MethodHookParam param) {
        StackTraceElement[] stackTraceElements = new StackTraceElement[1];
        stackTraceElements[0] = new StackTraceElement("h", "i", "a", 12);
        param.setResult(stackTraceElements);
    }

    public void loadPlugin(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        Log.d(XPOSETEST,"into loadPlugin");
        String cachePath = context.getCacheDir().getAbsolutePath();
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xposeplugin-release.apk";

        DexClassLoader myClassLoader = new DexClassLoader(apkPath, cachePath, cachePath, context.getClassLoader());

        try {
            Log.d(XPOSETEST,"get plu element");
            //得到插件APK的的中DexPathList对象中的dexElement数组
            Class<?> baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathList = baseDexClassLoader.getDeclaredField("pathList");
            pathList.setAccessible(true);
            //获取插件pathList的Object
            Object pathListObject = pathList.get(myClassLoader);
            //dexElements

            Class<?> pClass = pathListObject.getClass();
            Field dexElements = pClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            // 然后在从myPathListObj这个对象身上去dexElement数组
            Object dexElementArray = dexElements.get(pathListObject);

            //获取系统Elements

            PathClassLoader classLoader = (PathClassLoader)context.getClassLoader();
            Class<?> aClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field syspathList = aClass.getDeclaredField("pathList");
            syspathList.setAccessible(true);
//
            Log.d(XPOSETEST,"get sys element");
            //拿到系统pathList对象
            Object syspathListObject = syspathList.get(classLoader);

            Class<?> pathListObjectClass = syspathListObject.getClass();
            Field sysdexElements = pathListObjectClass.getDeclaredField("dexElements");
            sysdexElements.setAccessible(true);
            Object mSystemElementArray = sysdexElements.get(pathListObject);

            Log.d(XPOSETEST,"create elements");

            //合并Elements数组
            // 获取长度
            int pluginElementsLength = Array.getLength(dexElementArray);
            int totalLength = pluginElementsLength + Array.getLength(mSystemElementArray);

            //生成新数组
            Class<?> componentType = dexElementArray.getClass().getComponentType();
            Object resArray = Array.newInstance(componentType, totalLength);
            Log.d(XPOSETEST,"make arrays");

            //合并数组
            for (int i = 0; i < totalLength; i++) {
                if (i < pluginElementsLength) {
                    Array.set(resArray,i,Array.get(dexElementArray,i));
                } else {
                    Array.set(resArray,i,Array.get(mSystemElementArray,i - pluginElementsLength));
                }
            }
            Log.d(XPOSETEST,"result array o: " + Arrays.toString((Object[])dexElementArray));
            Log.d(XPOSETEST,"result array t: " + Arrays.toString((Object[])mSystemElementArray));
            Log.d(XPOSETEST,"result array thr: " + Arrays.toString((Object[]) resArray));

            sysdexElements.set(syspathListObject,resArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getSystemElement(Context context) {
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class<?> aClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathList = aClass.getDeclaredField("pathList");
            pathList.setAccessible(true);

            Object pathListObject = pathList.get(classLoader);
            Class<?> pathListObjectClass = pathListObject.getClass();
            Field dexElements = pathListObjectClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            Object elements = dexElements.get(pathListObject);

            return elements;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
