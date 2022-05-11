package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.media

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class MediaFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    abstract fun bindViewModel(viewModel: MediaViewModel): ViewModel

}