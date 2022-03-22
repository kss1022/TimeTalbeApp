package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class LectureListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LectureListViewModel::class)
    abstract fun bindViewModel(viewModel: LectureListViewModel): ViewModel
}