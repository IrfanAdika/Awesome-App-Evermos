package com.irfan.awesomeapp.data.remote

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("curated")
    fun getPhotos(@Query("page") page: Int): Call<JsonObject>
}