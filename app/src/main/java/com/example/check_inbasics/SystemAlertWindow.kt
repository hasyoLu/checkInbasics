package com.example.check_inbasics

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.util.Locale

object SystemAlertWindow {

    private const val REQUEST_OVERLAY = 5004

    fun requestOverlayPermissionByActivity(requestCode: Int = REQUEST_OVERLAY, mSource: Activity) {
        var intent = getSystemAlertWindowIntent(mSource)
        try {
            mSource.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            intent = appDetailsApi(mSource)
            mSource.startActivityForResult(intent, requestCode)
        }
    }

    fun requestOverlayPermissionByContext(context: Context) {
        val intent = getSystemAlertWindowIntent(context)
        try {
            context.startActivity(intent)
        } catch (_: Exception) { }
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
                    val builder = AlertDialog.Builder(activity)
                    builder.setCancelable(false)
                    builder.setTitle("悬浮窗权限未授权")
                    builder.setMessage("应用需要悬浮窗权限，以展示浮标")
                    builder.setPositiveButton("去添加 权限") { dialog, which ->
                        dialog.dismiss()
                        FloatTool.requestOverlayPermission(activity)
                    }
                    builder.setNegativeButton("拒绝则 退出") { dialog, which ->
                        dialog.dismiss()

                        // 若拒绝了所需的权限请求，则退出应用
                        activity.finish()
                        System.exit(0)
                    }
                    builder.show()
                }

            }
        }
    }

    fun getSystemAlertWindowIntent(mSource: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MARK.contains("meizu")) {
                meiZuApi(mSource)
            } else {
                MdefaultApi(mSource)
            }
        } else {
            if (MARK.contains("huawei")) {
                huaweiApi(mSource)
            } else if (MARK.contains("xiaomi")) {
                xiaomiApi(mSource)
            } else if (MARK.contains("oppo")) {
                oppoApi(mSource)
            } else if (MARK.contains("vivo")) {
                vivoApi(mSource)
            } else if (MARK.contains("meizu")) {
                meizuApi(mSource)
            } else {
                LdefaultApi(mSource)
            }
        }
    }

    private fun huaweiApi(context: Context): Intent {
        val intent = Intent()
        intent.setClassName(
            "com.huawei.systemmanager",
            "com.huawei.permissionmanager.ui.MainActivity"
        )
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity"
        )
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName(
            "com.huawei.systemmanager",
            "com.huawei.notificationmanager.ui.NotificationManagmentActivity"
        )
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    private fun xiaomiApi(context: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", context.packageName)
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
        )
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    private fun oppoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.setClassName(
            "com.color.safecenter",
            "com.color.safecenter.permission.floatwindow.FloatWindowListActivity"
        )
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName(
            "com.coloros.safecenter",
            "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"
        )
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity")
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    private fun vivoApi(context: Context): Intent {
        val intent = Intent()
        intent.setClassName(
            "com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager"
        )
        intent.putExtra("packagename", context.packageName)
        if (hasActivity(context, intent)) {
            return intent
        }
        intent.setClassName(
            "com.iqoo.secure",
            "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"
        )
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    private fun meizuApi(context: Context): Intent {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.component = ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    @SuppressLint("ConstantLocale")
    private val MARK = Build.MANUFACTURER.lowercase(Locale.getDefault())
    const val REQUEST_OVERLY = 7562
    private fun LdefaultApi(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    private fun appDetailsApi(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    private fun MdefaultApi(context: Context): Intent {
        var intent: Intent? = null
        intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return if (hasActivity(context, intent)) {
            intent
        } else appDetailsApi(context)
    }

    private fun meiZuApi(context: Context): Intent {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return if (hasActivity(context, intent)) {
            intent
        } else MdefaultApi(context)
    }

    private fun hasActivity(context: Context, intent: Intent?): Boolean {
        val packageManager = context.packageManager
        return packageManager.queryIntentActivities(
            intent!!,
            PackageManager.MATCH_DEFAULT_ONLY
        ).size > 0
    }
}
