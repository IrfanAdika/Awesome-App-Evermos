package com.irfan.awesomeapp.app

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class AppController: Application() {

    private val TAG = this.javaClass.name

    override fun onCreate() {
        super.onCreate()

        instance = this

        /**
         * Create Realm database with name
         * Realm database is local database
         * For saving data in local storage
         */
        Realm.init(this)
        val realm = RealmConfiguration.Builder()
        realm.name("evermost.realm")
        realm.deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(realm.build())
    }

    /**
     * Create global context
     */
    companion object {
        lateinit var instance: AppController
            private set
    }
}