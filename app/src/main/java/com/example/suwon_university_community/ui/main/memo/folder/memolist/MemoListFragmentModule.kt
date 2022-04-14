package com.example.suwon_university_community.ui.main.memo.folder.memolist

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemoListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoListViewModel::class)
    abstract fun bindViewModel( viewModel: MemoListViewModel) : ViewModel
}