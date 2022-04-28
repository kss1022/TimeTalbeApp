package net.suwon.plus.ui.main.memo.folder.bookmark

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class BookmarkListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkListViewModel::class)
    abstract fun bindViewModel( viewModel: BookmarkListViewModel) : ViewModel
}