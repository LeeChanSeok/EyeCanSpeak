package com.nassafy.aro.util.di


import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nassafy.aro.Application
import com.nassafy.aro.domain.api.*
import com.nassafy.aro.util.SERVER_URL
import com.nassafy.aro.util.WEATHER_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeaderInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithoutHeaderInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeaderInterceptorApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithoutHeaderInterceptorApi


class WithoutHeaderInterceptor @Inject constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val newRequest = request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        proceed(newRequest)
    }
} // End of AuthInterceptor

class HeaderInterceptor @Inject constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val newRequest = request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", Application.sharedPreferencesUtil.getUserAccessToken())
            .build()
        proceed(newRequest)
    }
} // End of AuthInterceptor

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private val gson: Gson = GsonBuilder() //날짜 데이터 포맷
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    //BASE_URL 제공
    @Provides
    fun provideBaseUrl() = SERVER_URL

    //HttpClient 제공
    @WithoutHeaderInterceptorOkHttpClient
    @Provides
    fun provideOtherInterceptorOkHttpClient(withoutHeaderInterceptor: WithoutHeaderInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addInterceptor(withoutHeaderInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()
    } // End of provideOkHttpClient

    //헤더 포함 HttpClient 제공
    @HeaderInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(headerInterceptor)
            .build()
    } // End of provideHeaderOkHttpClient

    @WithoutHeaderInterceptorApi
    @Provides
    fun provideTestApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): TestApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TestApi::class.java)
    }

    @HeaderInterceptorApi
    @Provides
    fun provideHeaderTestApi(@HeaderInterceptorOkHttpClient okHeaderOkHttpClient: OkHttpClient): TestApi {
        return Retrofit.Builder()
            .client(okHeaderOkHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TestApi::class.java)
    }

    // ============================================ Main ============================================
    @Provides
    fun provideMainApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): MainApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MainApi::class.java)
    } // End of provideMainApi)

    // ============================================ Main ============================================
    @Provides
    fun provideMyPageApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): MyPageApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MyPageApi::class.java)
    } // End of provideMyPageApi)

    @Provides
    fun provideWithoutHeaderMyPageApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): WithoutHeaderMyPageApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WithoutHeaderMyPageApi::class.java)
    } // End of provideWithoutHeaderStampApi


    // ============================================ Stamp ============================================

    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderStampApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): StampApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StampApi::class.java)
    } // End of provideWithoutHeaderStampApi


    @Provides
    @HeaderInterceptorApi
    fun provideHeaderStampApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): StampApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StampApi::class.java)
    } // End of provideHeaderStampApi

    // ============================================ UserAccess ============================================
    @Provides
    fun provideUserAccessApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): UserAccessApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserAccessApi::class.java)
    }


    // ============================================ Diary ============================================
    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderDiaryApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): DiaryApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DiaryApi::class.java)
    } // End of provideWithoutHeaderDiaryApi

    @Provides
    @HeaderInterceptorApi
    fun provideHeaderDiaryApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): DiaryApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DiaryApi::class.java)
    } // End of provideHeaderDiaryApi

    // ============================================ Aurora ============================================
    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderAuroraApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): AuroraApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuroraApi::class.java)
    } // End of provideWithoutHeaderAuroraApi

    @Provides
    @HeaderInterceptorApi
    fun provideHeaderAuroraApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): AuroraApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuroraApi::class.java)
    } // End of provideHeaderDiaryApi

    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderWeatherApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): WeatherApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApi::class.java)
    } // End of provideWithoutHeaderWeatherApi

    // ============================================= Splash =============================================
    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderSplashApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): SplashApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SplashApi::class.java)
    } // End of provideWithoutHeaderSplashApi


    @Provides
    @HeaderInterceptorApi
    fun provideHeaderSplashApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): SplashApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SplashApi::class.java)
    } // End of provideHeaderSplashApi

    // ============================================= Validate =============================================
    @Provides
    @WithoutHeaderInterceptorApi
    fun provideWithoutHeaderValidateApi(@WithoutHeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): ValidateApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ValidateApi::class.java)
    } // End of provideWithoutHeaderValidateApi

    // ============================================= Setting =============================================

    @Provides
    fun provideHeaderSettingApi(@HeaderInterceptorOkHttpClient okHttpClient: OkHttpClient): SettingApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SettingApi::class.java)
    } // End of provideHeaderSplashApi
} // End of ApiModule
