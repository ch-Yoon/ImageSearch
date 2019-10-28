package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class SearchBoxViewModelFactory(
    private val application: Application,
    private val imageSearchRepository: ImageSearchRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBoxViewModel::class.java)) {
            return (SearchBoxViewModel(application, imageSearchRepository) as T)
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}