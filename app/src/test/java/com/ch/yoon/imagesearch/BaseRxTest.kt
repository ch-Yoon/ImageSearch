package com.ch.yoon.imagesearch

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.After
import org.junit.Before

/**
 * Creator : ch-yoon
 * Date : 2019-11-14
 **/
abstract class BaseRxTest {

    private var compositeDisposable = CompositeDisposable()

    @Before
    fun init() {
        before()
    }
    
    @After
    fun finish() {
        compositeDisposable.clear()
        after()
    }
    
    protected fun Disposable.register() {
        compositeDisposable.add(this)
    }

    abstract fun before()

    abstract fun after()
}