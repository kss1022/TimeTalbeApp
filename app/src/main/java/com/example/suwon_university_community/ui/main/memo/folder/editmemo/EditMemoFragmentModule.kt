package com.example.suwon_university_community.ui.main.memo.folder.editmemo

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface EditMemoFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditMemoViewModel::class)
    abstract fun bindViewModel( viewModel : EditMemoViewModel) : ViewModel
}