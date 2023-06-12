package com.android.shopmaker.presentation.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.shopmaker.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)


        overridePendingTransition(R.anim.from_right, R.anim.to_right)
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    /***
     * 앱 강제 종료
     */
    fun forceFinish() {
        ActivityCompat.finishAffinity(this)
        System.runFinalization()
        System.exit(0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left, R.anim.to_left)
    }
}