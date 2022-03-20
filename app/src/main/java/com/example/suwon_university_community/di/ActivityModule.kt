package com.example.suwon_university_community.di

import com.example.suwon_university_community.di.scope.ActivityScope
import com.example.suwon_university_community.ui.login.AuthActivity
import com.example.suwon_university_community.ui.login.AuthActivityModule
import com.example.suwon_university_community.ui.main.MainActivity
import com.example.suwon_university_community.ui.main.MainActivityModule
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.AddTimeTableActivity
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.AddTimeTableActivityModule
import com.example.suwon_university_community.ui.start.StartActivity
import com.example.suwon_university_community.ui.start.StartActivityModule
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


    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            StartActivityModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getStartActivity(): StartActivity


    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AddTimeTableActivityModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getAddTimeTableActivity(): AddTimeTableActivity

}