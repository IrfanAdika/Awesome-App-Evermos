package com.irfan.awesomeapp.module.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animo.passionfood.data.remote.RetrofitConfig
import com.google.gson.Gson
import com.irfan.awesomeapp.base.BaseFormVM
import com.irfan.awesomeapp.base.BaseNetwork
import com.irfan.awesomeapp.base.NetworkCallback
import com.irfan.awesomeapp.data.model.ObjectPhoto
import com.irfan.awesomeapp.data.model.Photos
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.save

class HomeViewModel : ViewModel() {
    private val service = RetrofitConfig.createService()
    var formResult = MutableLiveData<BaseFormVM>()

    fun getPhotos(page: Int) {
        BaseNetwork.getInstance()
            .networkService(service = service.getPhotos(page = page), callback = object : NetworkCallback {
                override fun onSuccess(data: String) {
                    val photos = Gson().fromJson(data, ObjectPhoto::class.java)
                    if (page == 1) {
                        Photos().deleteAll()
                    }
                    photos.save()

                    formResult.value = BaseFormVM(isSuccess = true, data = photos)
                }

                override fun onError(message: String?, data: Any?) {
                    formResult.value = BaseFormVM(isSuccess = false, error = message)
                }

            })
    }
}