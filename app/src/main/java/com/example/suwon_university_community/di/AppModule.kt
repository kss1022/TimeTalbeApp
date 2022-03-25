package com.example.suwon_university_community.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.suwon_university_community.data.api.JsonTimeTableService
import com.example.suwon_university_community.data.api.TimeTableService
import com.example.suwon_university_community.data.api.response.LectureApi
import com.example.suwon_university_community.data.db.AppDataBase
import com.example.suwon_university_community.data.db.dao.LectureDao
import com.example.suwon_university_community.data.db.dao.TimeTableDao
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.data.preference.PreferenceManager.Companion.PREFERENCES_NAME
import com.example.suwon_university_community.data.repository.lecture.DefaultLectureRepository
import com.example.suwon_university_community.data.repository.lecture.LectureRepository
import com.example.suwon_university_community.data.repository.timetable.DefaultTimeTableRepository
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.data.repository.user.DefaultUserRepository
import com.example.suwon_university_community.data.repository.user.UserRepository
import com.example.suwon_university_community.util.converter.RoomTypeConverter
import com.example.suwon_university_community.util.provider.DefaultResourceProvider
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideFireStorage(): FirebaseStorage = Firebase.storage


    @JvmStatic
    @Singleton
    @Provides
    fun provideLectureApi(retrofit: Retrofit): LectureApi = buildLectureService(retrofit)

    @JvmStatic
    @Singleton
    @Provides
    fun provideLectureRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = buildLectureRetrofit(gsonConverterFactory, okHttpClient)


    @JvmStatic
    @Singleton
    @Provides
    fun provideGsonConvert(): GsonConverterFactory = buildGsonConvertFactory()

    @JvmStatic
    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient = buildOKHttpClient()

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
    fun provideAppDataBase(context: Context, typeConverter: RoomTypeConverter): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase.APP_DATABASE_NAME)
            .addTypeConverter(typeConverter)
            .build()


    @JvmStatic
    @Singleton
    @Provides
    fun provideLectureDao(appDataBase: AppDataBase): LectureDao = appDataBase.getLectureDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideTimeTableDao(appDataBase: AppDataBase): TimeTableDao = appDataBase.getTimeTableDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @JvmStatic
    @Singleton
    @Provides
    fun provideRoomTypeConverter(gson: Gson): RoomTypeConverter = RoomTypeConverter(gson)
}

@Module
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun provideResourceProvider(
        provider: DefaultResourceProvider
    ): ResourceProvider


    @Singleton
    @Binds
    abstract fun provideTimeTableService(
        service: JsonTimeTableService
    ): TimeTableService


    @Singleton
    @Binds
    abstract fun provideUserRepository(
        rep: DefaultUserRepository
    ): UserRepository


    @Singleton
    @Binds
    abstract fun provideLectureRepository(
        repo: DefaultLectureRepository
    ): LectureRepository


    @Singleton
    @Binds
    abstract fun provideTimeTableRepository(
        repo: DefaultTimeTableRepository
    ): TimeTableRepository
}