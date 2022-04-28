package net.suwon.plus.ui.main.home.login.signIn

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey


@Module
abstract class SignInFragmentModule {


    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindViewModel( signInViewModel: SignInViewModel) : ViewModel
}