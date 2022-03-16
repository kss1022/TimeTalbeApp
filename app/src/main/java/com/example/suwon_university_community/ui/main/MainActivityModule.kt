package com.example.suwon_university_community.ui.main


import com.example.suwon_university_community.di.ViewModelBuilder
import com.example.suwon_university_community.di.scope.FragmentScope
import com.example.suwon_university_community.ui.main.chat.ChatFragment
import com.example.suwon_university_community.ui.main.chat.ChatFragmentModule
import com.example.suwon_university_community.ui.main.home.HomeFragment
import com.example.suwon_university_community.ui.main.home.HomeFragmentModule
import com.example.suwon_university_community.ui.main.my.MyFragment
import com.example.suwon_university_community.ui.main.my.MyFragmentModule
import com.example.suwon_university_community.ui.main.time_table.TimeTableFragment
import com.example.suwon_university_community.ui.main.time_table.TimeTableFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module()
abstract class MainActivityModule {


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            ChatFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getChatFragment(): ChatFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            MyFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getMyFragment(): MyFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TimeTableFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getTimeTableFragment(): TimeTableFragment

}