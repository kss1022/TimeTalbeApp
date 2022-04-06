package com.example.suwon_university_community.ui.main.home.notice

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NoticeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoticeViewModel::class)
    abstract fun bindViewModel(viewModel: NoticeViewModel): ViewModel
}