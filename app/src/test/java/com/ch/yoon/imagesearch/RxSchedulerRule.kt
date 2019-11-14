package com.belongings.bag.belongingsbag

import android.util.Log
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException
import java.net.SocketException


/**
 * Creator : ch-yoon
 * Date : 2019-09-26.
 */
class RxSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

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

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}