package com.animo.passionfood.data.remote

import com.irfan.awesomeapp.BuildConfig

enum class UrlConfig(val url: String) {
    API_KEY(BuildConfig.API_KEY),
    BASE_URL(BuildConfig.BASE_URL)
}