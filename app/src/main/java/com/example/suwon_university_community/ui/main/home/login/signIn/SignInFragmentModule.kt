package com.example.suwon_university_community.ui.main.home.login.signIn

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class SignInFragmentModule {


    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindViewModel( signInViewModel: SignInViewModel) : ViewModel
}