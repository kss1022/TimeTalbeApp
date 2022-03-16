package com.example.suwon_university_community.di

import com.example.suwon_university_community.di.scope.ActivityScope
import com.example.suwon_university_community.ui.StartActivity
import com.example.suwon_university_community.ui.login.AuthActivity
import com.example.suwon_university_community.ui.login.AuthActivityModule
import com.example.suwon_university_community.ui.main.MainActivity
import com.example.suwon_university_community.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun getMainActivity(): MainActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [(AuthActivityModule::class)])
    abstract fun getAuthActivity(): AuthActivity
}