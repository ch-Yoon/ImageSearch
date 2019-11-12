package com.ch.yoon.imagesearch

import android.app.Application
import com.ch.yoon.imagesearch.di.*
import org.koin.android.ext.android.startKoin

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class App : Application() {

    init {
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(viewModelModule, repositoryModule, networkModule, databaseModule, helperModule))
    }

}