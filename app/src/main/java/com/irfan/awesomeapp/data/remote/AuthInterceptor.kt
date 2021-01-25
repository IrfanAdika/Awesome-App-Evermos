package com.animo.passionfood.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.irfan.awesomeapp.BuildConfig
import com.irfan.awesomeapp.app.AppController
import com.irfan.awesomeapp.base.BaseNetwork
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw BaseNetwork.NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }

        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", BuildConfig.API_KEY)

        return chain.proceed(requestBuilder.build())
    }

    /**
     * Checking connection
     */
    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = AppController.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

}