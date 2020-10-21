package com.easy.tools

import android.content.Context
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.nfc.Tag
import android.os.IBinder
import android.util.Log
import java.lang.reflect.Method

class MPackageManager {

    companion object{
        val TAG = "Tool"
    }

    /**
     * 获取包名集合
     */
    fun getPackageInfo(context: Context): List<String> {
        var packageManager = context.packageManager

        var installedPackages = packageManager.getInstalledPackages(0)

        var mList = mutableListOf<String>()

        var size = installedPackages.forEach {
            mList.add(it.packageName)
            mList.size
        }

        Log.d(TAG,"已安装包的数量: " + size);

        return mList;
    }

    fun getCacheSize(context: Context,packageInfo: PackageInfo) : Int{

        try {
            var packageManager = context.packageManager
            var jClass = PackageManager :: class.java

            var declaredMethod = jClass.getDeclaredMethod(
                "getPackageSizeInfo",
                String.javaClass,
                IPackageStatsObserver::class.java
            )

            var vInvoke = declaredMethod.invoke(packageManager,packageInfo, MPackageStatsObserver())

            return vInvoke as Int
        } catch (e: Exception) {
            Log.e("tool",e.message)
            e.printStackTrace()
        }
        return 0
    }

    open class MPackageStatsObserver : IPackageStatsObserver {
        override fun onGetStatsCompleted(pStats: PackageStats?, succeeded: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            Log.d(TAG,"onGetStatsCompleted")
        }

        override fun asBinder(): IBinder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            Log.d(TAG,"asBinder")
        }

    }

}