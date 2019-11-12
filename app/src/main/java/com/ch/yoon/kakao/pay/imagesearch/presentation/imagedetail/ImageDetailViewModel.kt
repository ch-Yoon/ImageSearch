package com.ch.yoon.kakao.pay.imagesearch.presentation.imagedetail

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.presentation.common.livedata.SingleLiveEvent

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
        receivedImageDocument?.let { document ->
            imageDocument = document
            _imageUrlInfo.value = document.imageUrl
        } ?: run {
            updateShowMessage(R.string.unknown_error)
            _finishEvent.call()
        }
    }

    fun onClickWebButton() {
        imageDocument?.let { document ->
            if(TextUtils.isEmpty(document.docUrl)) {
                updateShowMessage(R.string.non_existent_url_error)
            } else {
                _moveToWebEvent.value = document.docUrl
            }
        }
    }

    fun onClickBackPress() {
        _finishEvent.call()
    }

}