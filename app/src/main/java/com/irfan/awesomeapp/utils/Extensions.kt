package com.irfan.awesomeapp.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import coil.load
import com.irfan.awesomeapp.R

/**
 * Show Bottom dialog
 * for pop up alert.
 * ex: Pop up No connection, Time Out Request, success OVO Payment
 */
fun FragmentManager.bottomSheetDialog(
    textButton: String,
    message: String,
    withButton: Boolean,
    callback: BottomSheetAlert.PopUpAlertCallback? = null,
    title: String = "",
    image: Int = 0,
    allowCancelable: Boolean = true,
    dismissWhenResume: Boolean = true
) {
    try {
        val fragment = BottomSheetAlert()
        fragment.textButton = textButton
        fragment.message = message
        fragment.title = title
        fragment.image = image
        fragment.dismissWhenResume = dismissWhenResume
        fragment.allowCancel = allowCancelable
        if (callback != null) {
            fragment.callback = callback
        }
        fragment.withButton = withButton
        fragment.show(this, "popup_alert")
    } catch (e: IllegalStateException) {
        Log.i("CannotShowDialog", "true")
    }
}

/**
 * Load image using picasso library
 */
@BindingAdapter("image_url")
fun ImageView.load(url: String) {
    this.load(url) {
        placeholder(R.drawable.icon_image_blank)
        error(R.drawable.icon_image_blank)
    }
    this.scaleType = ImageView.ScaleType.CENTER_CROP
}