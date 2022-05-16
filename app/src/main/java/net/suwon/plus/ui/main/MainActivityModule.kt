package net.suwon.plus.ui.main


import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelBuilder
import net.suwon.plus.di.ViewModelKey
import net.suwon.plus.di.scope.FragmentScope
import net.suwon.plus.ui.main.home.HomeFragment
import net.suwon.plus.ui.main.home.HomeFragmentModule
import net.suwon.plus.ui.main.home.notice.NoticeFragmentModule
import net.suwon.plus.ui.main.home.notice.NoticeListFragment
import net.suwon.plus.ui.main.memo.MemoFragment
import net.suwon.plus.ui.main.memo.MemoFragmentModule
import net.suwon.plus.ui.main.memo.folder.FolderListFragment
import net.suwon.plus.ui.main.memo.folder.FolderListFragmentModule
import net.suwon.plus.ui.main.memo.folder.FolderSelectSheetFragment
import net.suwon.plus.ui.main.memo.folder.bookmark.BookmarkListFragment
import net.suwon.plus.ui.main.memo.folder.bookmark.BookmarkListFragmentModule
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragmentModule
import net.suwon.plus.ui.main.memo.folder.memolist.MemoListFragment
import net.suwon.plus.ui.main.memo.folder.memolist.MemoListFragmentModule
import net.suwon.plus.ui.main.memo.folder.timetablememolist.TimeTableMemoListFragment
import net.suwon.plus.ui.main.memo.folder.timetablememolist.TimeTableMemoListFragmentModule
import net.suwon.plus.ui.main.time_table.TimeTableFragment
import net.suwon.plus.ui.main.time_table.TimeTableFragmentModule


@Module()
abstract class MainActivityModule {

    companion object {
        @Provides
        fun provideApplication(activity: MainActivity): Application {
            return activity.application
        }
    }


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
    protected abstract fun getFolderListFragment(): FolderListFragment




    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            BookmarkListFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getBookMarkListFragment(): BookmarkListFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TimeTableMemoListFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getTimeTableMemoListFragment(): TimeTableMemoListFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            MemoListFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getMemoListFragment(): MemoListFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            EditMemoFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getEditMemoFragment(): EditMemoFragment

    @FragmentScope
    @ContributesAndroidInjector
    protected abstract fun getFolderSelectBottomFragment(): FolderSelectSheetFragment




    @Binds
    @IntoMap
    @ViewModelKey(MainActivitySharedViewModel::class)
    abstract fun bindViewModel(viewModel: MainActivitySharedViewModel): ViewModel

}