package com.example.check_inbasics

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat


/**
 * 设置全屏Notification
 *
 * 设置Notification时通过setFullScreenIntent添加一个全屏Intent对象，可以在Android 10上从后台启动一个Activity界面，
 * 需要在Manifest.xml清单文件中加上：<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT"/>
 *
 * 然后在 APP/Activity里启动这个方法即可
 */
fun getChannelNotificationQ(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(SettingConfig.UNI_PACKAGE)
    val fullScreenPendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //只在Android 8.O之上需要渠道，这里的第一个参数要和下面的channelId一样，8.0以下没有渠道这一说法
        val notificationChannel = NotificationChannel(
            "2",
            "name",
            NotificationManager.IMPORTANCE_HIGH
        ) //通知重要度，DEFAULT及以上，通知时手机默认会有振动
        //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
        manager.createNotificationChannel(notificationChannel)
    }
    val notificationBuilder = NotificationCompat.Builder(context, "2")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        //                .setContentText(content)
        //.setContentTitle()
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(Notification.CATEGORY_CALL)
        .setOngoing(true)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .build()
    manager.notify(1, notificationBuilder) //让通知显示出来
}

/**
 * FG 使用自定义通知
 * FG.INSTANCE.setCustom_notification(defaultNotification(base))
 */
fun defaultNotification(context: Context): Notification.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager =
            context.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
        val Channel =
            NotificationChannel("0000", "可关闭通知", NotificationManager.IMPORTANCE_DEFAULT)
        Channel.lightColor = Color.GREEN
        Channel.importance = NotificationManager.IMPORTANCE_MIN
        Channel.setShowBadge(false)
        Channel.description = ""
        Channel.enableLights(false)
        Channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        Channel.enableVibration(false)
        Channel.setSound(null, null)
        manager.createNotificationChannel(Channel)
        Notification.Builder(context, "0000")
            .setContentTitle("我是自定义通知")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
    } else {
        Notification.Builder(context)
            .setContentTitle("我是自定义通知")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
    }
}

fun popupBackgroundPermissionNotification(context: Context) {

    val pendingIntent =
        PendingIntent.getActivity(context, SystemAlertWindow.REQUEST_OVERLY, SystemAlertWindow.getSystemAlertWindowIntent(context), PendingIntent.FLAG_IMMUTABLE)

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel("2001",
            "popupBackgroundPermissionNotification", NotificationManager.IMPORTANCE_HIGH)
        //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
        manager.createNotificationChannel(notificationChannel)
    }
    val notificationBuilder = NotificationCompat.Builder(context, "2001")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentText("产业工人实名制安全组件运行异常，请点击按照指引开启”悬浮窗“权限")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(Notification.CATEGORY_CALL)
       // .setOngoing(true)
        .setContentIntent(pendingIntent)
        .build()
    manager.notify(2001, notificationBuilder) //让通知显示出来
}


