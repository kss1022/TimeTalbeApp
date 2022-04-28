package net.suwon.plus.ui.main.home.login

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.suwon.plus.di.ViewModelBuilder
import net.suwon.plus.di.scope.FragmentScope
import net.suwon.plus.ui.main.home.login.login.LoginFragment
import net.suwon.plus.ui.main.home.login.login.LoginFragmentModule
import net.suwon.plus.ui.main.home.login.signIn.SignInFragment
import net.suwon.plus.ui.main.home.login.signIn.SignInFragmentModule


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