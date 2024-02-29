package com.example.check_inbasics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import f.g.hulmgn.rnjstb.djgyyw.FG

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("Lishenyang", "MainActivity onCreate")
        if (!FloatTool.hasOverlayPermission(this)) {
            Log.i("Lishenyang", "MainActivity requestOverlayPermission")
            FloatTool.requestOverlayPermission(this)
        }

//        if (!PopBackgroundPermissionUtil.hasPopupBackgroundPermission(this)) {
//            Log.d("MainActivity", "MainActivity: PopupBackgroundPermission")
//            SystemAlertWindow.requestOverlayPermissionByActivity(mSource = this)
//        } else {
//            Log.d("MainActivity", "MainActivity: finish")
//            TimerHelper.startUniAppTime
//            finish()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FloatTool.onActivityResult(requestCode, resultCode, this) { isSuccess ->
            if (isSuccess) {
                Log.i("Lishenyang", "MainActivity isSuccess")
                if (!PopBackgroundPermissionUtil.hasPopupBackgroundPermission(this)) {
                    Log.i("Lishenyang", "MainActivity whitelistSettings")
                    FG.whitelistSettings(this)
                }
            } else {
                Log.i("Lishenyang", "MainActivity finish")
                finish()
            }
        }
    }
}