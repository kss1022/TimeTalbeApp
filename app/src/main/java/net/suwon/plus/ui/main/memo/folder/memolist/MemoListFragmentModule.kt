package net.suwon.plus.ui.main.memo.folder.memolist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class MemoListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoListViewModel::class)
    abstract fun bindViewModel( viewModel: MemoListViewModel) : ViewModel
}