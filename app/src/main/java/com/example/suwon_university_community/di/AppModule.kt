package com.example.suwon_university_community.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [AppModuleBinds::class])
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAuth() = Firebase.auth


//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideIoDispatcher() = Dispatchers.IO

//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideAppDataBase(context: Context): AppDataBase =
//        Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase.APP_DATABASE_NAME)
//            .build()
//
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideAppDao(appDataBase: AppDataBase): AppDao = appDataBase.getAppDao()
}


@Module
abstract class AppModuleBinds {

//    @Singleton
//    @Binds
//    abstract fun bindRepository(repo: DefaultAppRepository): AppRepository
}