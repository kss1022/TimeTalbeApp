package com.example.suwon_university_community.di

import com.example.suwon_university_community.di.scope.ActivityScope
import com.example.suwon_university_community.ui.main.home.login.AuthActivity
import com.example.suwon_university_community.ui.main.home.login.AuthActivityModule
import com.example.suwon_university_community.ui.main.MainActivity
import com.example.suwon_university_community.ui.main.MainActivityModule
import com.example.suwon_university_community.ui.main.home.setting.SettingActivity
import com.example.suwon_university_community.ui.main.home.setting.SettingActivityModule
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivity
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivityModule
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist.TimeTableListActivity
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist.TimeTableListActivityModule
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
            AddTimeTableCellActivityModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getAddTimeTableActivity(): AddTimeTableCellActivity


    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            TimeTableListActivityModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getTimeTableListActivity(): TimeTableListActivity


    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            SettingActivityModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getSettingActivity() : SettingActivity
}