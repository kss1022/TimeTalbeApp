package net.suwon.plus.ui.main.memo.folder.editmemo

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class EditMemoFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditMemoViewModel::class)
    abstract fun bindViewModel( viewModel : EditMemoViewModel) : ViewModel
}