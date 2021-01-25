package com.animo.passionfood.data.remote

import com.irfan.awesomeapp.BuildConfig
import com.irfan.awesomeapp.data.remote.Services
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitConfig {

    //interceptor
    private fun getInterceptor() : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(logging)
        }

        okHttpClient.apply {
            addInterceptor(getAuthToken())
            connectTimeout(30, TimeUnit.SECONDS)
        }


        return okHttpClient.build()
    }

    //retrofit instance create
    fun retrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //getting token from user for authorization
    private fun getAuthToken(): Interceptor {
        return AuthInterceptor()
    }

    //getting retrofit instance from this class
    companion object {
        private lateinit var retrofitConfig: RetrofitConfig

        @Synchronized
        @JvmStatic
        fun createService(): Services {
            if (!this::retrofitConfig.isInitialized) {
                retrofitConfig = RetrofitConfig()
            }
            return retrofitConfig.retrofitInstance().create(Services::class.java)
        }
    }
}