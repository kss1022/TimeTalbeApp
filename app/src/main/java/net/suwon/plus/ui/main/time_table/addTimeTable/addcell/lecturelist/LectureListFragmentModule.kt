package net.suwon.plus.ui.main.time_table.addTimeTable.addcell.lecturelist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class LectureListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LectureListViewModel::class)
    abstract fun bindViewModel(viewModel: LectureListViewModel): ViewModel
}