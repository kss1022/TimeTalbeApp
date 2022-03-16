package com.example.suwon_university_community.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelBuilder {
    @Binds
    internal abstract fun bindViewModelFactory(
        factory: AppViewModelFactory
    ): ViewModelProvider.Factory
}
