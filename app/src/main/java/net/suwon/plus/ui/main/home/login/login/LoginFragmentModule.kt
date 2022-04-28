package net.suwon.plus.ui.main.home.login.login

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class LoginFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindViewModel(viewModel: LoginViewModel) : ViewModel
}