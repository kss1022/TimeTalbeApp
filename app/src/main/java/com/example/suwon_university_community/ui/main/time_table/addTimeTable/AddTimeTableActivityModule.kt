package com.example.suwon_university_community.ui.main.time_table.addTimeTable

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class AddTimeTableActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddTimeTableViewModel::class)
    abstract fun bindViewModel(viewModel: AddTimeTableViewModel): ViewModel
}