package com.example.suwon_university_community.ui.main


import com.example.suwon_university_community.di.ViewModelBuilder
import com.example.suwon_university_community.di.scope.FragmentScope
import com.example.suwon_university_community.ui.main.home.HomeFragment
import com.example.suwon_university_community.ui.main.home.HomeFragmentModule
import com.example.suwon_university_community.ui.main.home.notice.NoticeFragmentModule
import com.example.suwon_university_community.ui.main.home.notice.NoticeListFragment
import com.example.suwon_university_community.ui.main.memo.MemoFragment
import com.example.suwon_university_community.ui.main.memo.MemoFragmentModule
import com.example.suwon_university_community.ui.main.memo.folder.FolderListFragment
import com.example.suwon_university_community.ui.main.memo.folder.FolderListFragmentModule
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
            MemoFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getMemoFragment(): MemoFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TimeTableFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getTimeTableFragment(): TimeTableFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            NoticeFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getNoticeListFragment(): NoticeListFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            FolderListFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getFolderListFragment() : FolderListFragment
}