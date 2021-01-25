package com.irfan.awesomeapp.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.irfan.awesomeapp.R
import com.irfan.awesomeapp.utils.*
import com.irozon.sneaker.Sneaker
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.view_empty_list.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.textColor

open class BaseActivity : AppCompatActivity(), BaseView {
    private lateinit var emptyView: View
    private var loading = ProgressDialog()

    /**
     * Broadcastreceiver from Retrofit config
     * receive when no connection or time out
     */
    private val broadReceiverNetwork = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            when (intent.getStringExtra(Constants.BROADCAST_ALERT)) {
                Constants.NO_CONNECTION -> {
                    noConnection(getString(R.string.cek_koneksi_internet))
                }
                Constants.REQ_TIME_OUT -> {
                    noConnection(getString(R.string.koneksi_kamu_lambat))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        observerBroadcastReceiver()
    }

    override fun onPause() {
        super.onPause()
        removeObserver()
    }

    private fun observerBroadcastReceiver() {
        BroadcastReceiverHelper.addObserver(
            applicationContext,
            BroadcastReceiverHelper.NotificationType.NOCONNECTION,
            broadReceiverNetwork
        )
    }

    /**
     * Show loading
     */
    override fun showLoading() {
        if (!AppHelper.isLoadingShow) {
            AppHelper.isLoadingShow = true
            loading.show(this, "Loading...")
        }

    }

    /**
     * Hide loading
     */
    override fun hideLoading() {
        AppHelper.isLoadingShow = false
        loading.hide()
    }

    /**
     * Show error
     */
    override fun showError(errorMessage: String?, type: ToastType) {
        if (!errorMessage.isNullOrEmpty()) {
            val sneaker = Sneaker.with(this) // Activity, Fragment or ViewGroup
            val view =
                LayoutInflater.from(this).inflate(R.layout.custom_toast, sneaker.getView(), false)

            when (type) {
                ToastType.ERROR -> {
                    view.view_toast.setCardBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorRed
                        )
                    )
                    view.label_toast.textColor = R.color.colorWhite
                }
                else -> {
                    view.view_toast.setCardBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorAccent
                        )
                    )
                    view.label_toast.textColor = R.color.colorPrimary
                }
            }


            // Your custom view code
            view.findViewById<TextView>(R.id.label_toast).text = errorMessage
            sneaker.sneakCustom(view)
        }
    }

    /**
     * Show alert
     */
    override fun showAlert(
        title: String,
        message: String,
        textBtnPositive: String,
        textBtnNegative: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit,
        cancelable: Boolean
    ) {
        alert(message, title) {
            positiveButton(textBtnPositive) {
                positiveAction()
            }

            if (textBtnNegative.isNotEmpty()) {
                negativeButton(textBtnNegative) {
                    negativeAction()
                }
            }
        }.show().setCancelable(cancelable)
    }

    /**
     * Add empty list
     * used for page contain list data
     */
    override fun addEmptyList(
        view: ViewGroup,
        image: Int,
        title: String,
        subTitle: String
    ) {

        if (view.findViewById<ViewGroup>(R.id.view_empty) == null) {
            val layoutInflater: LayoutInflater = LayoutInflater.from(this)
            emptyView = layoutInflater.inflate(
                R.layout.view_empty_list, // Custom view/ layout
                view, // Root layout to attach the view
                false // Attach with root layout or not
            )

            emptyView.image_empty.setImageResource(image)
            emptyView.title_empty.text = title
            emptyView.subtitle_empty.text = subTitle
            view.addView(emptyView)

        }
    }

    /**
     * Hide empty list
     */
    override fun removeEmptyList(view: ViewGroup) {
        if (this::emptyView.isInitialized) {
            Handler(Looper.getMainLooper()).post { view.removeView(emptyView) }
        }
    }

    /**
     * Show pop up when no connection or time out
     */
    fun noConnection(message: String) {

        if (!AppHelper.isPopupConnectionHasShow) {
            AppHelper.isPopupConnectionHasShow = true
            supportFragmentManager.bottomSheetDialog("Ok", message, false)
        }
    }

    /**
     * Remove observer
     * when activity onPause
     */
    private fun removeObserver() {
        BroadcastReceiverHelper.removeObserver(applicationContext, broadReceiverNetwork)
    }

    /**
     * Hide keyboard
     * when tap any view
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}