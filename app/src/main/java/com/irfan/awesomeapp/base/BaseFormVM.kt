package com.irfan.awesomeapp.base

data class BaseFormVM (
    val isSuccess: Boolean = false,
    val data: Any? = null,
    val error: String? = null
)