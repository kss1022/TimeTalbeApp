package com.example.suwon_university_community.ui.main.time_table

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TimeTableFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableViewModel::class)
    abstract fun bindViewModel(viewModel: TimeTableViewModel): ViewModel
}