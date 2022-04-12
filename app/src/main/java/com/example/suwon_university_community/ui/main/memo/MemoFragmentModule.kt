package com.example.suwon_university_community.ui.main.memo

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemoFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoViewModel::class)
    abstract fun bindViewModel(viewModel: MemoViewModel): ViewModel
}