package com.example.suwon_university_community.ui.main.home

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}