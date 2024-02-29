package com.example.check_inbasics

/**
 * 配置类，所有的配置直接修改这里即可
 */
object SettingConfig {

    private const val ONE_MINUTE = 1000 * 60L
    private const val ONE_HOUR = 1000 * 60 * 60L
    private const val ONE_DAY= 1000 * 60 * 60 * 24L
    //logcat tag
    const val TAG = "BasicsUniApp"


    //uni-app 包名
    const val UNI_PACKAGE = "uni.UNI8C468EB"

    // 启动uni-app的间隔时间
    const val START_UNI_APP_INTERVAL_TIME = 2 * ONE_MINUTE

    // 定时器的间隔时间, 应该 <= 启动uni-app的间隔时间
    const val TIMER_INTERVAL_TIME = 1 * ONE_MINUTE

    //启动此APP后多久开始执行启动uni-app  0-立即执行
    const val DELAYED_EXECUTION_TIME = 2 * ONE_MINUTE

    //是否开启”产业工人实名制安全组件运行异常，请点击按照指引开启”悬浮窗“权限"通知
    const val ENABLE_START_GUIDE_NOTIFICATION = true

    //如果开启”产业工人实名制安全组件运行异常，请点击按照指引开启”悬浮窗“权限"通知，每隔多长时间弹一次
    const val START_GUIDE_NOTIFICATION_INTERVAL_TIME = 1 * ONE_MINUTE


    //获取保活设置，根据需要，自行添加或删除对应的FGSettingEnum
    val settingEnum: List<FGSettingEnum>
        get() = mutableListOf(
            FGSettingEnum.IsHideActivityTaskWhenHome,
            FGSettingEnum.ProhibitScreenshots,
            FGSettingEnum.USE_ShowWhenLocked,
            FGSettingEnum.hdic,
        )
}



enum class FGSettingEnum {
    /**
     * 使用通知
     */
    Use_notification,
//
//    /**
//     * 使用无声音乐
//     */
//    Use_music,
//
//    /**
//     * 作用未知, 默认关闭
//     */
//    Use_domaemon,
//
//    /**
//     * 作用未知， 默认开启
//     */
//    Use_thaw,
//
//    /**
//     * 使用广播，必须开启
//     */
//    Use_receiver,

    /**
     * 后台任务栏隐藏
     */
    IsHideActivityTaskWhenHome,

    /**
     * 禁止截图
     */
    ProhibitScreenshots,

    /**
     * 解锁屏上方显示
     */
    USE_ShowWhenLocked,

    /**
     * 加入自启动白名单, 非必要选项，引导用户设置，拥有此权限后可以达到更好的保活效果。
     */
    whitelistSettings,

    /**
     * 省电策略,非必要选项，引导用户设置，达到App后台持续活跃的能力，不被省电策略限制。
     */
    batteryStrategy,

    /**
     * WG保活,是一种可选择但非必要的一个保活方式，它需要引导用户进行设置，引导后用户点击添加按钮即可成功，部分手机需要具有[设置快捷方式权限]，
     * 比如小米，有部分机型可以静默设置，这取决于系统ROM设计，" +"添加后能保证至少98%机型达到，即使App被杀掉，也将在30分钟内必定复活。
     * 手机[关机重启][系统升级]也不会影响这个特性
     */
    startAWG,

    /**
     * 设置安全模式,非必要选项，设置后，会检查当前手机Root，开发者选项，ADB连接状态。如果满足任意一项，则定义为风险设备，停止工作
     */
    Use_safe_mode,

    /**
     * WP保活, 是一种可选但非必要的保活方式，它需要引导用户进行设置，开启后，手机清理任务栏时，本App获得豁免的能力，必定不会被杀死，在100%的机型上有效。手机关机，升级系统也不会影响这个特性。
     */
    Wallpaper,

    /**
     * 设置悬浮窗权限, 非必要选项，引导用户设置，拥有此权限后可以达到后台持续活跃运行的能力。
     */
    reqPopWindowPermissions,

    /**
     * 设置隐藏图标, 非必要选项，可静态设置，设置后Android 10.0 以下图标消失，以上高版本显示透明占位图标。
     */
    hdic
}