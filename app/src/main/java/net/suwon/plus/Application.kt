package net.suwon.plus

import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import net.suwon.plus.di.DaggerAppComponent

class Application : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()

        appContext = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}