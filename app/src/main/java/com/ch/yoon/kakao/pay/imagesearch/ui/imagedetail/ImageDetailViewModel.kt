package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.extention.TAG
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
class ImageDetailViewModel(application: Application) : BaseViewModel(application) {

    private val _imageUrlInfo = MutableLiveData<String>()
    val imageUrlInfo: LiveData<String> = _imageUrlInfo

    private val _moveToWebEvent = SingleLiveEvent<String>()
    val moveToWebEvent: LiveData<String> = _moveToWebEvent

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    private var imageDocument: ImageDocument? = null

    fun showImageDetailInfo(receivedImageDocument: ImageDocument?) {
        receivedImageDocument?.run {
            imageDocument = this
            _imageUrlInfo.value = imageUrl
        } ?: finish()
    }

    fun onClickWebButton() {
        imageDocument?.run {
            _moveToWebEvent.value = docUrl
        } ?: finish()
    }

    fun onClickBackPress() {
        _finishEvent.call()
    }

    private fun finish() {
        updateShowMessage(R.string.error_unknown_error)
        _finishEvent.call()
        Log.d(TAG, "image document is null")
    }
}