package net.suwon.plus.ui.main.memo.folder.editmemo.editimage

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class EditImageDetailActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditImageDetailViewModel::class)
    abstract fun bindViewModel(detailViewModel: EditImageDetailViewModel): ViewModel
}