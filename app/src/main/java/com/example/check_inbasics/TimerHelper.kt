package com.example.check_inbasics

import android.util.Log
import java.util.Timer
import java.util.TimerTask

object TimerHelper  {
    val startUniAppTime by lazy {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.i(SettingConfig.TAG, "Timer start")
                UniAppUtil.startUniApp(App.instance)
            }
        }, SettingConfig.TIMER_INTERVAL_TIME, SettingConfig.TIMER_INTERVAL_TIME)
    }

    val checkHasPopupBackgroundPermission by lazy {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.i(SettingConfig.TAG, "checkHasPopupBackgroundPermission Timer start")
                if (!PopBackgroundPermissionUtil.hasPopupBackgroundPermission(App.instance)) {
                    popupBackgroundPermissionNotification(App.instance)
                }
            }
        }, SettingConfig.START_GUIDE_NOTIFICATION_INTERVAL_TIME, SettingConfig.START_GUIDE_NOTIFICATION_INTERVAL_TIME)
    }
}
