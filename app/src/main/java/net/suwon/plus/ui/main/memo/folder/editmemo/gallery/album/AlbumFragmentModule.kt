package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.album

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class AlbumFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    abstract fun bindViewModel(viewModel: AlbumViewModel): ViewModel

}