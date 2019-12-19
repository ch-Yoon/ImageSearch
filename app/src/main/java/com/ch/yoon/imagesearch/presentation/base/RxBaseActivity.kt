package com.ch.yoon.imagesearch.presentation.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Creator : ch-yoon
 * Date : 2019-12-19.
 */
abstract class RxBaseActivity<B : ViewDataBinding> : BaseActivity<B>() {

    private val compositeDisposableMap = HashMap<Lifecycle.Event, CompositeDisposable>().apply {
        put(Lifecycle.Event.ON_PAUSE, CompositeDisposable())
        put(Lifecycle.Event.ON_STOP, CompositeDisposable())
        put(Lifecycle.Event.ON_DESTROY, CompositeDisposable())
    }

    override fun onPause() {
        compositeDisposableMap[Lifecycle.Event.ON_PAUSE]?.clear()
        super.onPause()
    }

    override fun onStop() {
        compositeDisposableMap[Lifecycle.Event.ON_STOP]?.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposableMap[Lifecycle.Event.ON_DESTROY]?.clear()
        super.onDestroy()
    }

    protected fun Disposable.disposeByOnPause() {
        compositeDisposableMap[Lifecycle.Event.ON_PAUSE]?.add(this)
    }

    protected fun Disposable.disposeByOnStop() {
        compositeDisposableMap[Lifecycle.Event.ON_STOP]?.add(this)
    }

    protected fun Disposable.disposeByOnDestroy() {
        compositeDisposableMap[Lifecycle.Event.ON_DESTROY]?.add(this)
    }
}
