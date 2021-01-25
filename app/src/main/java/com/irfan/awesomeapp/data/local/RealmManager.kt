package com.irfan.awesomeapp.data.local

class RealmManager {



    companion object {
        private var realmManager: RealmManager? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): RealmManager {
            if (realmManager == null) {
                realmManager = RealmManager()
            }

            return realmManager!!
        }
    }
}