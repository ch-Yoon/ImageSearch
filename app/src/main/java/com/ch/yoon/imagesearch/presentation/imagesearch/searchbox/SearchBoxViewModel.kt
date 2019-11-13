package com.ch.yoon.imagesearch.presentation.imagesearch.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogRepository
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog

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
    private val searchLogRepository: SearchLogRepository
) : BaseViewModel (application) {

    private val _searchLogList = MutableLiveData<MutableList<SearchLog>>(mutableListOf())
    val searchLogList: LiveData<List<String>> = Transformations.map(_searchLogList) { list -> list?.map { it.keyword }?.toList() }

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val _searchEnableEvent = SingleLiveEvent<Boolean>()
    val searchEnableEvent: LiveData<Boolean> = _searchEnableEvent

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    private var isOpen = false

    fun loadSearchLogList() {
        searchLogRepository.requestSearchLogList()
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
            searchLogRepository.deleteSearchLog(targetLog)
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
        searchLogRepository.deleteAllSearchLog()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _searchLogList.clear()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickShowButton() {
        if(!isOpen) {
            _searchEnableEvent.value = true
        }
    }

    fun onClickBackPressButton() {
        if(isOpen) {
            _searchEnableEvent.value = false
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
        searchLogRepository.insertOrUpdateSearchLog(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newLog ->
                _searchLogList.removeFirstAndAddFirst(newLog) { it.keyword == keyword }
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }
}