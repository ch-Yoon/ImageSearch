package com.ch.yoon.imagesearch.presentation.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.util.extension.TAG
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-11-13
 **/
class FavoriteListViewModel(
    application: Application,
    private val imageRepository: ImageRepository
) : BaseViewModel(application) {

    private val _favoriteImageList = MutableLiveData<List<ImageDocument>>()
    val favoriteImageList: LiveData<List<ImageDocument>> = _favoriteImageList

    fun loadFavoriteImageList() {
        imageRepository.requestFavoriteImageList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ favoriteList ->
                _favoriteImageList.value = favoriteList
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }
}