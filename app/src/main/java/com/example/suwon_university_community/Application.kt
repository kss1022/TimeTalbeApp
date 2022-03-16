package com.example.suwon_university_community

import android.content.Context
import com.example.suwon_university_community.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

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