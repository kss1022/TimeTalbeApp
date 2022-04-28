package net.suwon.plus.ui.main.memo

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class MemoFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoViewModel::class)
    abstract fun bindViewModel(viewModel: MemoViewModel): ViewModel
}