package net.suwon.plus.di
import net.suwon.plus.BuildConfig
import net.suwon.plus.data.api.response.lecture.LectureApi
import net.suwon.plus.data.api.response.notice.NoticeApi
import net.suwon.plus.data.api.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


fun buildLectureService(retrofit: Retrofit): LectureApi = retrofit.create(LectureApi::class.java)


fun buildLectureRetrofit (
     gsonConverter : GsonConverterFactory,
    okHttpClient: OkHttpClient
) : Retrofit = Retrofit.Builder()
        .baseUrl(Url.FIRE_STORE_DEFAULT_URL)
        .addConverterFactory(gsonConverter)
        .client(okHttpClient)
        .build()




fun buildNoticeService(retrofit: Retrofit): NoticeApi = retrofit.create(NoticeApi::class.java)


fun buildNoticeRetrofit (
    gsonConverter : GsonConverterFactory,
    okHttpClient: OkHttpClient
) : Retrofit = Retrofit.Builder()
    .baseUrl(Url.FIRE_STORE_DEFAULT_URL)
    .addConverterFactory(gsonConverter)
    .client(okHttpClient)
    .build()





fun buildGsonConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

fun buildOKHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().apply {

        if (BuildConfig.DEBUG) {
            this.level = HttpLoggingInterceptor.Level.BODY
        } else {
            this.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}
