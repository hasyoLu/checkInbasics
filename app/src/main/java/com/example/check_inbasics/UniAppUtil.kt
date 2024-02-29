package com.example.check_inbasics

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import java.util.concurrent.atomic.AtomicBoolean

object UniAppUtil {
    private val isRunning = AtomicBoolean(false)
    var firstStartAppTime: Long = 0L

    fun startUniApp(context: Context) {
        if (isRunning.get()) {
            Log.i(SettingConfig.TAG, "UniAppUtil: isRunning == true")
            return
        }
        isRunning.set(true)
        val time = System.currentTimeMillis()
        if (time - firstStartAppTime < SettingConfig.DELAYED_EXECUTION_TIME) {
            Log.i(SettingConfig.TAG, "time - firstStartAppTime < SettingConfig.DELAYED_EXECUTION_TIME")
            isRunning.set(false)
            return
        }
        val lastTime = SPUtils.getInstance().getLong("last_start_time", 0L)
        val intervalTime = SettingConfig.START_UNI_APP_INTERVAL_TIME
        if (time - lastTime < intervalTime) {
            Log.i(SettingConfig.TAG, "UniAppUtil: time - lastTime > intervalTime$time    $lastTime")
            isRunning.set(false)
            return
        }
        SPUtils.getInstance().put("last_start_time", time)
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(SettingConfig.UNI_PACKAGE)
        if (intent != null) {
            context.startActivity(intent)
            Log.i(SettingConfig.TAG, "UniAppUtil: startUniApp success")
        } else {
            Log.i(SettingConfig.TAG, "UniAppUtil: startUniApp, intent == null")
        }
        isRunning.set(false)
    }


}