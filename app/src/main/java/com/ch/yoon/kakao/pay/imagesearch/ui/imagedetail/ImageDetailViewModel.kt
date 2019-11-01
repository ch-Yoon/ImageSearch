package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageDocument
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
        } ?: run {
            updateShowMessage(R.string.unknown_error)
            _finishEvent.call()
        }
    }

    fun onClickWebButton() {
        imageDocument?.run {
            if(TextUtils.isEmpty(docUrl)) {
                updateShowMessage(R.string.non_existent_url_error)
            } else {
                _moveToWebEvent.value = docUrl
            }
        }
    }

    fun onClickBackPress() {
        _finishEvent.call()
    }

}