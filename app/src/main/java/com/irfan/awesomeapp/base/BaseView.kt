package com.irfan.awesomeapp.base

import android.view.ViewGroup

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(errorMessage: String?, type: ToastType = ToastType.SUCCESS)
    fun showAlert(title: String, message: String, textBtnPositive: String, textBtnNegative: String, positiveAction: () -> Unit, negativeAction: () -> Unit, cancelable: Boolean = true)
    fun addEmptyList(view: ViewGroup, image: Int, title: String, subTitle: String)
    fun removeEmptyList(view: ViewGroup)
}

enum class ToastType {
    ERROR,
    SUCCESS
}