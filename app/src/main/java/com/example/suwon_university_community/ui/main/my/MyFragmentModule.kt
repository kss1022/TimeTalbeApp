package com.example.suwon_university_community.ui.main.my

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MyFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyViewModel::class)
    abstract fun bindViewModel(viewModel: MyViewModel): ViewModel
}