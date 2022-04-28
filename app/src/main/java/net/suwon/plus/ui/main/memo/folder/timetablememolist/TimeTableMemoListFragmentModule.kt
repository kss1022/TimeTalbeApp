package net.suwon.plus.ui.main.memo.folder.timetablememolist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class TimeTableMemoListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableMemoListViewModel::class)
    abstract fun bindViewModel( viewModel: TimeTableMemoListViewModel) : ViewModel
}