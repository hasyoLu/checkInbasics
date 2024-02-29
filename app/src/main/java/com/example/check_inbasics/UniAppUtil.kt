package com.example.check_inbasics

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

object UniAppUtil {
    private val isRunning = AtomicBoolean(false)
    var firstStartAppTime: Long = 0L

    private const val KEY_LAST_START_TIME = "last_start_time"

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
        val lastTime = SPUtils.getInstance().getLong(KEY_LAST_START_TIME, 0L)
        if (time - lastTime < SettingConfig.START_UNI_APP_INTERVAL_TIME) {
            Log.i(SettingConfig.TAG, "UniAppUtil: time - lastTime > intervalTime$time    $lastTime")
            isRunning.set(false)
            return
        }
        realStartUniApp(context, time, lastTime)
        isRunning.set(false)
    }


    /**
     * 立即启动，不会受间隔时间限制，用在MainActivity
     * 注意：这种方式启动后会更新最后一次启动时间
     */
    fun startUniAppNow(context: Context) {
        if (isRunning.get()) {
            Log.i(SettingConfig.TAG, "startUniAppNow: isRunning == true")
            return
        }
        isRunning.set(true)
        val lastTime = SPUtils.getInstance().getLong(KEY_LAST_START_TIME, 0L)
        realStartUniApp(context, System.currentTimeMillis(), lastTime)
        isRunning.set(false)
    }

    private fun realStartUniApp(context: Context, time: Long, lastTime: Long) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(SettingConfig.UNI_PACKAGE)
        if (intent != null) {
            try {
                context.startActivity(intent)
                SPUtils.getInstance().put(KEY_LAST_START_TIME, time)
                Log.i(SettingConfig.TAG, "UniAppUtil: startUniApp success")
            } catch (e: Exception) {
                SPUtils.getInstance().put(KEY_LAST_START_TIME, lastTime)
                Log.i(SettingConfig.TAG, "UniAppUtil: startUniApp Exception $e")
            }
        } else {
            Log.i(SettingConfig.TAG, "UniAppUtil: startUniApp, intent == null")
        }
    }


}