package com.example.suwon_university_community.ui.main.memo.folder.bookmark

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BookmarkListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkListViewModel::class)
    abstract fun bindViewModel( viewModel: BookmarkListViewModel) : ViewModel
}