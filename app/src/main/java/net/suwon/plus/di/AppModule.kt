package net.suwon.plus.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
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
import net.suwon.plus.data.api.JsonTimeTableService
import net.suwon.plus.data.api.NoticeService
import net.suwon.plus.data.api.TimeTableService
import net.suwon.plus.data.api.response.lecture.LectureApi
import net.suwon.plus.data.api.response.notice.NoticeApi
import net.suwon.plus.data.db.AppDataBase
import net.suwon.plus.data.db.dao.LectureDao
import net.suwon.plus.data.db.dao.MemoDao
import net.suwon.plus.data.db.dao.NoticeDao
import net.suwon.plus.data.db.dao.TimeTableDao
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.data.preference.PreferenceManager.Companion.PREFERENCES_NAME
import net.suwon.plus.data.repository.lecture.DefaultLectureRepository
import net.suwon.plus.data.repository.lecture.LectureRepository
import net.suwon.plus.data.repository.memo.DefaultMemoRepository
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.data.repository.notice.DefaultNoticeRepository
import net.suwon.plus.data.repository.notice.NoticeRepository
import net.suwon.plus.data.repository.timetable.DefaultTimeTableRepository
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.data.repository.user.DefaultUserRepository
import net.suwon.plus.data.repository.user.UserRepository
import net.suwon.plus.util.converter.RoomTypeConverter
import net.suwon.plus.util.provider.DefaultResourceProvider
import net.suwon.plus.util.provider.ResourceProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    fun provideLectureApi(@Named("Lecture") retrofit: Retrofit): LectureApi = buildLectureService(retrofit)

    @JvmStatic
    @Singleton
    @Provides
    @Named("Lecture")
    fun provideLectureRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = buildLectureRetrofit(gsonConverterFactory, okHttpClient)


    @JvmStatic
    @Singleton
    @Provides
    fun provideNoticeApi(@Named("Notice") retrofit: Retrofit) : NoticeApi = buildNoticeService(retrofit)

    @JvmStatic
    @Singleton
    @Provides
    @Named("Notice")
    fun provideNoticeRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = buildNoticeRetrofit(gsonConverterFactory, okHttpClient)




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
            .addMigrations(AppDataBase.MIGRATION_1_2)
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
    fun provideNoticeDao(appDataBase: AppDataBase) : NoticeDao = appDataBase.getNoticeDao()


    @JvmStatic
    @Singleton
    @Provides
    fun provideMemoDao(appDataBase: AppDataBase) : MemoDao = appDataBase.getMemoDao()

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
    abstract fun provideNoticeService(
        service : net.suwon.plus.data.api.JsonNoticeService
    ) : NoticeService


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

    @Singleton
    @Binds
    abstract fun provideNoticeRepository(
        repo : DefaultNoticeRepository
    ) : NoticeRepository

    @Singleton
    @Binds
    abstract fun provideMemoRepository(
        repo: DefaultMemoRepository
    ) : MemoRepository
}