package com.ch.yoon.imagesearch.presentation.imagedetail

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import com.ch.yoon.imagesearch.extension.TAG
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
class ImageDetailViewModel(
    application: Application,
    private val imageRepository: ImageRepository
) : BaseViewModel(application) {

    private val _imageUrlInfo = MutableLiveData<String>()
    val imageUrlInfo: LiveData<String> = _imageUrlInfo

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _moveToWebEvent = SingleLiveEvent<String>()
    val moveToWebEvent: LiveData<String> = _moveToWebEvent

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    private var imageDocument: ImageDocument? = null

    fun showImageDetailInfo(receivedImageDocument: ImageDocument?) {
        receivedImageDocument?.let { document ->
            imageDocument = document
            _imageUrlInfo.value = document.imageUrl
            _isFavorite.value = document.isFavorite
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

    fun onClickFavorite() {
        imageDocument?.let { document ->
            document.isFavorite = document.isFavorite.not()
            if(document.isFavorite) {
                saveFavoriteToRepository(document)
            } else {
                deleteFavoriteFromRepository(document)
            }
        }
    }

    private fun saveFavoriteToRepository(target: ImageDocument) {
        imageRepository.saveFavoriteImage(target)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateShowMessage(R.string.success_favorite_save)
                updateImageDocument(target)
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    private fun deleteFavoriteFromRepository(target: ImageDocument) {
        imageRepository.deleteFavoriteImage(target)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateImageDocument(target)
                updateShowMessage(R.string.success_favorite_delete)
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    private fun updateImageDocument(target: ImageDocument) {
        imageDocument = target
        _isFavorite.value = target.isFavorite
    }
}