package com.dev.nihitb06.lightningnote.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import org.jetbrains.annotations.NotNull

class PermissionManager {

    interface OnPermissionResultListener {
        fun onGranted()
        fun onDenied()
    }

    companion object {
        private val permissionResultListener = ArrayList<OnPermissionResultListener>()

        fun askForPermission(context: Context, permissions: Array<String>, listener: OnPermissionResultListener) {
            val requestCode = permissionResultListener.size

            permissionResultListener.add(requestCode, listener)

            ActivityCompat.requestPermissions(context as Activity, permissions, requestCode)
        }

        fun onRequestPermissionsResult(requestCode: Int, @NotNull permissions: Array<out String>, @NotNull results: IntArray) {
            try {
                val thisListener = permissionResultListener[requestCode]

                for (index in permissions.indices) {
                    if (results[index] == PackageManager.PERMISSION_GRANTED)
                        thisListener.onGranted()
                    else
                        thisListener.onDenied()
                }
            } catch (e: IndexOutOfBoundsException) {
                Log.e("PermissionManager", "Message: "+e.message)
            }
        }
    }
}