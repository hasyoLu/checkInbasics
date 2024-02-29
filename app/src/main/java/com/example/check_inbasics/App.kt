package com.example.check_inbasics

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import f.g.hulmgn.rnjstb.djgyyw.FG
import f.g.hulmgn.rnjstb.djgyyw.FgListener
import f.g.hulmgn.rnjstb.djgyyw.InstallReferListener
import f.g.hulmgn.rnjstb.djgyyw.ReceiverListener
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask

class App : Application() {
    private var settingList: List<FGSettingEnum> = SettingConfig.settingEnum

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        /**
         * 保活的基础设置，一般情况下不要改变这里
         */
        FG.apply {
//            if (settingList.contains(FGSettingEnum.Use_notification)) {
//                use_notification = true
//                custom_notification = defaultNotification(context = this@App)
//            } else {
//                use_notification = false
//            }
            use_notification = false
            use_music = true
            use_domaemon = false
            use_thaw = true
            use_receiver = true
            if (isFgProcess(base)) {
                attach(base)
            }
        }
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        val mApplicationContext: Context = applicationContext

        UniAppUtil.firstStartAppTime = System.currentTimeMillis()
        /**
         * 保活的其他配置，包括初始化，埋点等
         */

        FG.apply {
            //埋点相关
            setFGListener(object : FgListener() {
                override fun onTrack(eventName: String, map: Map<String, Any>) {
                    Log.i(SettingConfig.TAG, "onTrack: $eventName $map")
                    startUniApp(mApplicationContext)
                }
                override fun reportException(message: String, e: Throwable) {}
                override fun onBroadcastReceivedEvent(intent: Intent) {}
                override fun onTiming() {}
            })
            //这个是google归因的,不需要就删除
            getInstallRefer(mApplicationContext, object : InstallReferListener {
                override fun onResult(result: JSONObject) {}
                override fun onError() {}
                override fun onAttributionFinish(isOrganic: Boolean, refer: String) {}
                override fun onNotSupport() {}
            })

            //初始化,不可以删除,如果初始化执行了,但是没有效果,控制台过滤“==FG==可以看到原因”
            init(mApplicationContext, true)

            //后台任务栏隐藏
            if (settingList.contains(FGSettingEnum.IsHideActivityTaskWhenHome)) {
                setIsHideActivityTaskWhenHome(true, ArrayList())
            }
            //禁止截图
            if (settingList.contains(FGSettingEnum.ProhibitScreenshots)) {
                prohibitScreenshots = true
            }
            //解锁屏上方显示
            if (settingList.contains(FGSettingEnum.USE_ShowWhenLocked)) {
                USE_ShowWhenLocked = true
            }
            if (settingList.contains(FGSettingEnum.whitelistSettings)) {
                whitelistSettings(applicationContext)
            }
            if (settingList.contains(FGSettingEnum.batteryStrategy)) {
                batteryStrategy(applicationContext)
            }
            if (settingList.contains(FGSettingEnum.startAWG)) {
                startAWG(applicationContext)
            }
            if (settingList.contains(FGSettingEnum.hdic)) {
                hdic(applicationContext, MainActivity::class.java.name)
            } else {
                showIcon(applicationContext, MainActivity::class.java.name)
            }
            use_safe_mode = settingList.contains(FGSettingEnum.Use_safe_mode)

            if (settingList.contains(FGSettingEnum.reqPopWindowPermissions)) {
                reqPopWindowPermissions(mApplicationContext)
            }
            //优化内存
            if (isFgProcess(mApplicationContext)) {
                return
            }
            if (isMainProcess(mApplicationContext)) {
                //这里是接收广播的,不需要就删除
                regActionReceiver(mApplicationContext)
                regActionListener(mApplicationContext, object : ReceiverListener() {
                    override fun onACTION_SCREEN_ON(intent: Intent) {
                        startUniApp(mApplicationContext)
                        Log.i(SettingConfig.TAG, "onACTION_SCREEN_ON")
                    }

                    override fun onVOLUME_CHANGED_ACTION() {
                        startUniApp(mApplicationContext)
                        Log.i(SettingConfig.TAG, "onVOLUME_CHANGED_ACTION")
                    }

                    override fun onACTION_CLOSE_SYSTEM_DIALOGS() {
                        startUniApp(mApplicationContext)
                        Log.i(SettingConfig.TAG, "onACTION_CLOSE_SYSTEM_DIALOGS")
                    }

                    override fun onACTION_USER_PRESENT() {
                        startUniApp(mApplicationContext)
                    }

                    override fun onACTION_SCREEN_OFF() {
                        startUniApp(mApplicationContext)
                    }

                    override fun other(action: String?) {
                        startUniApp(mApplicationContext)
                    }
                })
            }
        }

        TimerHelper.startUniAppTime

        if (SettingConfig.ENABLE_START_GUIDE_NOTIFICATION) {
            TimerHelper.checkHasPopupBackgroundPermission
        }
    }

    fun startUniApp(context: Context) {
        UniAppUtil.startUniApp(context)
    }
}