package com.nassafy.aro

import android.app.Application
import com.nassafy.aro.util.SharedPreferencesUtil
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
    } // End of onCreate

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var headerRetrofit: Retrofit
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    } // End of companion object
}  // End of Application class
