package com.ch.yoon.imagesearch

import android.app.Application
import com.ch.yoon.imagesearch.di.*
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.startKoin
import java.io.IOException
import java.net.SocketException

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

        initKoin()
        initRx()
    }

    private fun initKoin() {
        startKoin(this, listOf(viewModelModule, repositoryModule, networkModule, databaseModule, helperModule))
    }

    private fun initRx() {
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
        }
    }
}