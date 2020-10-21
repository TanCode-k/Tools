package com.easy.tools

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    var permissionRes = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        var packageInfo = this.packageManager.getPackageInfo(packageName, 0)

        var cacheSize = MPackageManager().getCacheSize(this,packageInfo)

        Log.d("Tool",""+cacheSize)

    }

    private fun init() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                var iterator = grantResults.iterator()
                while (iterator.hasNext()){
                    var nextInt = iterator.nextInt()
                    if (nextInt == PackageManager.PERMISSION_DENIED){
                        permissionRes = true
                    }
                }
            }
        }
    }


}
