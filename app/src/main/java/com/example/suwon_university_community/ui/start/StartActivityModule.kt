package com.example.suwon_university_community.ui.start

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StartActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(StartActivityViewModel::class)
    abstract fun bindViewModel(viewModel: StartActivityViewModel): ViewModel
}