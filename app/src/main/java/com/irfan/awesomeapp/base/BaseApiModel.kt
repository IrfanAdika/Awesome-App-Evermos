package com.irfan.awesomeapp.base

import com.google.gson.annotations.SerializedName

/**
 * Base api model
 * from server
 */
data class BaseApiModel (
    var success: Boolean = true,
    var code: Int = 200,
    var message: String = "Success",
    var data: Any? = null
)

/**
 * Error api model
 * from server
 */
data class ErrorApiModel (
    var code: Int?,
    var error: Any? = null,
    var message: String = "Error from server",
    var data: Any? = null
)