package com.example.suwon_university_community.ui.main.home.login

import com.example.suwon_university_community.di.ViewModelBuilder
import com.example.suwon_university_community.di.scope.FragmentScope
import com.example.suwon_university_community.ui.main.home.login.login.LoginFragment
import com.example.suwon_university_community.ui.main.home.login.login.LoginFragmentModule
import com.example.suwon_university_community.ui.main.home.login.signIn.SignInFragment
import com.example.suwon_university_community.ui.main.home.login.signIn.SignInFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AuthActivityModule {


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            LoginFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getLoginFragment(): LoginFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SignInFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun getSignInFragment(): SignInFragment
}