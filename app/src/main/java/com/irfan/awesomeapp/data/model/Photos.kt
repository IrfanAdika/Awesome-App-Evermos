package com.irfan.awesomeapp.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class ObjectPhoto: RealmObject() {

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("per_page")
    var perPage: Int = 0

    @SerializedName("photos")
    var photos: RealmList<Photos> = RealmList()

    @SerializedName("total_results")
    var totalResult: Int = 0

    @SerializedName("next_page")
    var nextPage: String = ""
}

open class Photos: RealmObject(), Serializable {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("url")
    var url: String = ""

    @SerializedName("photographer")
    var photographer: String = ""

    @SerializedName("photographer_url")
    var photographerUrl: String = ""

    @SerializedName("src")
    var src: Src? = null

}

open class Src: RealmObject(), Serializable {

    @SerializedName("small")
    var small: String = ""

    @SerializedName("landscape")
    var landscape: String = ""
}