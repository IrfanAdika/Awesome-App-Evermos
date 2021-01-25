package com.irfan.awesomeapp.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.irfan.awesomeapp.R
import kotlinx.android.synthetic.main.bottom_popup_alert.view.*

class BottomSheetAlert : BottomSheetDialogFragment() {
    private var mBehavior: BottomSheetBehavior<*>? = null
    private lateinit var dialog: BottomSheetDialog
    var message = ""
    var textButton = ""
    var title = ""
    var image = 0
    var allowCancel = true
    var dismissWhenResume = true

    var callback: PopUpAlertCallback? = null
    var withButton = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(
            context,
            R.layout.bottom_popup_alert,
            null
        )

        if (title.isNotEmpty()) {
            view.label_title_popup.text = title
        }

        if (image != 0) {
            view.image_popup.setImageResource(image)
        }

        if (!allowCancel) {
            view.view_dragable.visibility = View.GONE
            this.isCancelable = false
        }

        view.label_description_popup.text = message

        if (withButton) {
            view.button_ok.text = textButton
            view.button_ok.visibility = View.VISIBLE
            view.button_ok.setOnClickListener {
                dialog.dismiss()
                callback?.onButtonClicked()
            }
        } else {
            view.button_ok.visibility = View.GONE
        }

        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog


        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(allowCancel)

        mBehavior =
            BottomSheetBehavior.from(view.parent as View)
        (mBehavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_EXPANDED
        return dialog

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        AppHelper.isPopupConnectionHasShow = false
        callback?.onDismissDialog()
    }

    override fun onPause() {
        super.onPause()

        if (this::dialog.isInitialized && dialog.isShowing && dismissWhenResume) {
            dialog.dismiss()
        }
    }

    /**
     * Set Theme of BottomSheet
     * it will set Round top Background
     */
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    interface PopUpAlertCallback {
        fun onButtonClicked()
        fun onDismissDialog()
    }
}