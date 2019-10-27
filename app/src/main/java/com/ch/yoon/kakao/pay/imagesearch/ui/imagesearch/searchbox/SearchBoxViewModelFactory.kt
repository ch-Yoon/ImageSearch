package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class SearchBoxViewModelFactory(
    private val application: Application,
    private val imageRepository: ImageRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBoxViewModel::class.java)) {
            return (SearchBoxViewModel(application, imageRepository) as T)
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}