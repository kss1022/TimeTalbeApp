package com.example.suwon_university_community.ui.main.memo.folder

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class FolderListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(FolderListViewModel::class)
    abstract fun bindViewModel( viewModel : FolderListViewModel) : ViewModel

}