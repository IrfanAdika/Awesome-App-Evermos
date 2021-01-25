package com.irfan.awesomeapp.base

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.irfan.awesomeapp.app.AppController
import com.irfan.awesomeapp.data.model.ObjectPhoto
import com.irfan.awesomeapp.data.model.Photos
import com.irfan.awesomeapp.utils.BroadcastReceiverHelper
import com.irfan.awesomeapp.utils.Constants
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

interface NetworkCallback {

    fun onSuccess(data: String)
    fun onError(message: String?, data: Any? = null)
}

/**
 * Networking using retrofit2
 */
class BaseNetwork {
    fun networkService(service: Call<JsonObject>?, callback: NetworkCallback) {
        service?.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable) {
                callback.onError("")
                if(t is NoConnectivityException) {
                    notificationNoInternet()
                } else {
                    notificationRequestTimeOut()
                }
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                try {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val model =
                            gson.fromJson<ObjectPhoto>(response.body(), ObjectPhoto::class.java)

                        val apiModel = BaseApiModel()
                        apiModel.data = model

                        when (apiModel.code) {
                            in 200..201 -> {
                                val data = gson.toJson(apiModel.data)
                                callback.onSuccess(data)
                            }
                            else -> {
                                callback.onError(apiModel.message)
                            }
                        }
                    } else {
                        when (response.code()) {
                            401 -> {

                                callback.onError("")
                                //DO REFRESH TOKEN
                            }
                            400 -> {
                                try {
                                    val res = Gson().fromJson(response.errorBody()?.string(), ErrorApiModel::class.java)
                                    callback.onError(res.message, res.data)
                                } catch (e: java.lang.Exception) {
                                    callback.onError("")
                                }
                            }
                            else -> {
                                callback.onError("Gagal mengakses halaman")
                            }
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Oops gagal, harap coba kembali ya!")
                    Log.i("NetworkException", e.localizedMessage ?: "")
                }

            }
        })
    }

    /**
     * When no internet
     * call broadcast receiver for consume to BaseActivity
     */
    private fun notificationNoInternet() {
        val params = HashMap<String, String>()
        params[Constants.BROADCAST_ALERT] = Constants.NO_CONNECTION
        BroadcastReceiverHelper.postNotification(
            AppController.instance,
            BroadcastReceiverHelper.NotificationType.NOCONNECTION, params
        )
    }

    /**
     * When time out request
     * call broadcast receiver for consume to BaseActivity
     */
    private fun notificationRequestTimeOut() {
        val params = HashMap<String, String>()
        params[Constants.BROADCAST_ALERT] = Constants.REQ_TIME_OUT
        BroadcastReceiverHelper.postNotification(
            AppController.instance,
            BroadcastReceiverHelper.NotificationType.NOCONNECTION, params
        )
    }

    companion object {
        private var baseNetwork: BaseNetwork? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): BaseNetwork {
            if (baseNetwork == null) {
                baseNetwork = BaseNetwork()
            }

            return baseNetwork!!
        }
    }

    class NoConnectivityException : IOException() {
        // You can send any message whatever you want from here.

        override val message: String?
            get() = "No Internet Connection"
    }
}