package com.example.suwon_university_community.di

import android.annotation.SuppressLint
import android.content.Context
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.data.preference.PreferenceManager.Companion.PREFERENCES_NAME
import com.example.suwon_university_community.data.repository.DefaultUserRepository
import com.example.suwon_university_community.data.repository.UserRepository
import com.example.suwon_university_community.util.provider.DefaultResourceProvider
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@SuppressLint("JvmStaticProvidesInObjectDetector")
@Module(includes = [AppModuleBinds::class])
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth


    @JvmStatic
    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore


    @JvmStatic
    @Singleton
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider =
        DefaultResourceProvider(context)


    @JvmStatic
    @Singleton
    @Provides
    fun providePreferenceManager(context: Context): PreferenceManager = PreferenceManager(
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    )


    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO


    @JvmStatic
    @Singleton
    @Provides
    fun provideUserRepository(
        fireStore: FirebaseFirestore,
        ioDispatcher: CoroutineDispatcher
    ): UserRepository = DefaultUserRepository(fireStore, ioDispatcher)


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