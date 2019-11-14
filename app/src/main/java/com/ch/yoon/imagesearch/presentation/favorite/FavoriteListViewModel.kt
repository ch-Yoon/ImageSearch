package com.ch.yoon.imagesearch.presentation.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import com.ch.yoon.imagesearch.extension.TAG
import com.ch.yoon.imagesearch.extension.removeFirst
import com.ch.yoon.imagesearch.extension.removeFirstAndAddFirst
import com.ch.yoon.imagesearch.extension.replace
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-11-13
 **/
class FavoriteListViewModel(
    application: Application,
    private val imageRepository: ImageRepository
) : BaseViewModel(application) {

    private val _favoriteImageList = MutableLiveData<MutableList<ImageDocument>>()
    val favoriteImageList: LiveData<List<ImageDocument>> = Transformations.map(_favoriteImageList) { it?.toList() }

    private val _moveToDetailScreenEvent = SingleLiveEvent<ImageDocument>()
    val moveToDetailScreenEvent: LiveData<ImageDocument> = _moveToDetailScreenEvent

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    fun loadFavoriteImageList() {
        imageRepository.requestFavoriteImageList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ favoriteList ->
                _favoriteImageList.value = favoriteList.toMutableList()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickImage(imageDocument: ImageDocument) {
        _moveToDetailScreenEvent.value = imageDocument
    }

    fun updateFavorite(imageDocument: ImageDocument) {
        with(imageDocument) {
            if(isFavorite) {
                _favoriteImageList.removeFirstAndAddFirst(imageDocument) { it.id == id}
            } else {
                _favoriteImageList.removeFirst { it.id == id }
            }
        }
    }

    fun onClickBackPress() {
        _finishEvent.call()
    }

    fun observeChangingFavoriteImage() {
        imageRepository.observeChangingFavoriteImage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ changedImageDocument ->
                if(changedImageDocument.isFavorite.not()){
                    _favoriteImageList.removeFirst { it.id == changedImageDocument.id }
                }
            }, {
                Log.d(TAG, it.message)
            })
            .register()
    }
}