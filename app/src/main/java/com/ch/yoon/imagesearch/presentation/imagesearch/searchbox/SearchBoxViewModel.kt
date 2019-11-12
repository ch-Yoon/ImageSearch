package com.ch.yoon.imagesearch.presentation.imagesearch.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.ImageRepository
import com.ch.yoon.imagesearch.data.repository.model.SearchLogModel

import com.ch.yoon.imagesearch.util.extension.*
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-25.
 */
class SearchBoxViewModel(
    application: Application,
    private val imageRepository: ImageRepository
) : BaseViewModel (application) {

    private val _searchLogList = MutableLiveData<MutableList<SearchLogModel>>(mutableListOf())
    val searchLogList: LiveData<List<String>> = Transformations.map(_searchLogList) { list -> list?.map { it.keyword }?.toList() }

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val _searchBoxCloseEvent = SingleLiveEvent<Unit>()
    val searchBoxCloseEvent: LiveData<Unit> = _searchBoxCloseEvent

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    private var isOpen = false

    fun loadSearchLogList() {
        imageRepository.requestSearchLogList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ receivedSearchLogList ->
                _searchLogList.value = receivedSearchLogList.sorted().toMutableList()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickSearchLogDeleteButton(keyword: String) {
        val targetLog = _searchLogList.find { it.keyword == keyword }
        if(targetLog != null) {
            imageRepository.deleteSearchLog(targetLog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _searchLogList.removeFirst { it.keyword == keyword }
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .register()
        }
    }

    fun onClickSearchLogAllDelete() {
        imageRepository.deleteAllSearchLog()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _searchLogList.clear()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickBackPressButton() {
        if(isOpen) {
            _searchBoxCloseEvent.call()
        } else {
            _searchBoxFinishEvent.call()
        }
    }

    fun onStateChange(isOpen: Boolean) {
        this.isOpen = isOpen
    }

    fun onClickSearchButton(keyword: String) {
        processingSearchApproval(keyword)
    }

    private fun processingSearchApproval(keyword: String) {
        if(checkSearchPossibility(keyword)) {
            _searchEvent.value = keyword
            saveKeywordToRepository(keyword)
        }
    }

    private fun checkSearchPossibility(keyword: String): Boolean {
        return if (keyword.isEmpty()) {
            updateShowMessage(R.string.empty_keyword_guide)
            false
        } else {
            true
        }
    }

    private fun saveKeywordToRepository(keyword: String) {
        imageRepository.insertOrUpdateSearchLog(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newLog ->
                _searchLogList.removeFirstAndAddFirst(newLog) { it.keyword == keyword }
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }
}