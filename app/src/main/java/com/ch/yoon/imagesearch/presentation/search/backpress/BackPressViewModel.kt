package com.ch.yoon.imagesearch.presentation.search.backpress

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.extension.TAG
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Creator : ch-yoon
 * Date : 2019-12-16.
 */
class BackPressViewModel(
    application: Application
) : BaseViewModel(application) {

    companion object {
        private const val FINISH_TIME_LIMIT = 2000
    }

    private val backPressBehaviorSubject = BehaviorSubject.createDefault(0L)

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    init {
        observeBackPressBehaviorSubject()
    }

    fun onBackPress() {
        backPressBehaviorSubject.onNext(System.currentTimeMillis())
    }

    private fun observeBackPressBehaviorSubject() {
        backPressBehaviorSubject.toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(AndroidSchedulers.mainThread())
            .buffer(2, 1)
            .subscribe({ time ->
                if (time[1] - time[0] < FINISH_TIME_LIMIT) {
                    _finishEvent.call()
                } else {
                    updateShowMessage(R.string.back_press_guide_message)
                }
            }, { throwable ->
                Log.d(TAG, throwable.message ?: "unknown error")
            })
            .disposeByOnCleared()
    }
}