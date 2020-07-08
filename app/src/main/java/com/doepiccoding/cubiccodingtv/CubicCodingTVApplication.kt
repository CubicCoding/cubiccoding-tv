package com.doepiccoding.cubiccodingtv

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class CubicCodingTVApplication: MultiDexApplication() {

    companion object {
        lateinit var instance: CubicCodingTVApplication
            private set
        var IS_DEBUG = BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        if (IS_DEBUG) {//Set clean logs component...
            val timberTree = Timber.DebugTree()
            Timber.plant(timberTree)
        }
    }
}