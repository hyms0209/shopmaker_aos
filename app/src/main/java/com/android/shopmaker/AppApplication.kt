package com.android.shopmaker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication:Application() {

    companion object {
        var ctx: AppApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this

    }
}