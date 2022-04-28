package net.suwon.plus.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.suwon.plus.di.scope.ActivityScope
import net.suwon.plus.ui.main.MainActivity
import net.suwon.plus.ui.main.MainActivityModule
import net.suwon.plus.ui.main.home.login.AuthActivity
import net.suwon.plus.ui.main.home.login.AuthActivityModule
import net.suwon.plus.ui.main.home.setting.SettingActivity
import net.suwon.plus.ui.main.home.setting.SettingActivityModule
import net.suwon.plus.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivity
import net.suwon.plus.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivityModule
import net.suwon.plus.ui.main.time_table.addTimeTable.tablelist.TimeTableListActivity
import net.suwon.plus.ui.main.time_table.addTimeTable.tablelist.TimeTableListActivityModule
import net.suwon.plus.ui.start.StartActivity
import net.suwon.plus.ui.start.StartActivityModule


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