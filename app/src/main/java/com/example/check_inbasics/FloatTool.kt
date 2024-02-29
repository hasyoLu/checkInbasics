package com.example.check_inbasics

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object FloatTool {
    var CanShowFloat = false
    private const val REQUEST_OVERLAY = 5004
    private const val ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION"


    fun hasOverlayPermission(activity: Activity): Boolean {
        return Settings.canDrawOverlays(activity)
    }

    /**
     * 动态请求悬浮窗权限
     */
    fun requestOverlayPermission(activity: Activity) {
        val intent = Intent(
            ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + activity.packageName)
        )
        activity.startActivityForResult(intent, REQUEST_OVERLAY)
    }

    /**
     * 浮窗权限请求，Activity执行结果，回调函数
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, activity: Activity,
                         callBack: ((isSuccess: Boolean) -> Unit)? = null ) {
        // Toast.makeText(activity, "onActivityResult设置权限！", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_OVERLAY) { // 从应用权限设置界面返回
            if (resultCode == Activity.RESULT_OK) {
                // 设置标识为可显示悬浮窗
                if (callBack != null) {
                    callBack(true)
                }
            } else {
                if (!Settings.canDrawOverlays(activity)) {  // 若当前未允许显示悬浮窗，则提示授权
                    if (callBack != null) {
                        callBack(false)
                    }
                } else {
                    if (callBack != null) {
                        callBack(true)
                    }
                }

            }
        }
    }

}