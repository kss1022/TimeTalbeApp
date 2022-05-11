package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.detail

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class MediaDetailFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MediaDetailViewModel::class)
    abstract fun bindViewModel(viewModel: MediaDetailViewModel): ViewModel
}