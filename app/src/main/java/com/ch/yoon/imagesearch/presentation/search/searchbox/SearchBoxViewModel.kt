package com.ch.yoon.imagesearch.presentation.search.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogRepository
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog

import com.ch.yoon.imagesearch.extension.*
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

    private val _searchLogs = MutableLiveData<MutableList<SearchLog>>(mutableListOf())
    val searchLogs: LiveData<List<String>> = Transformations.map(_searchLogs) { list -> list?.map { it.keyword }?.toList() }

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val _searchBoxEnableEvent = SingleLiveEvent<Boolean>()
    val searchBoxEnableEvent: LiveData<Boolean> = _searchBoxEnableEvent

    var isOpen = false
        private set

    fun loadSearchLogList() {
        searchLogRepository.getAllSearchLogs()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ receivedSearchLogList ->
                _searchLogs.value = receivedSearchLogList.sorted().toMutableList()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickSearchLogDeleteButton(keyword: String) {
        val targetLog = _searchLogs.find { it.keyword == keyword }
        if(targetLog != null) {
            searchLogRepository.deleteSearchLog(targetLog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _searchLogs.removeFirst { it.keyword == keyword }
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .register()
        }
    }

    fun onClickSearchLogAllDelete() {
        searchLogRepository.deleteAllSearchLogs()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _searchLogs.clear()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickShowButton() {
        if(!isOpen) {
            _searchBoxEnableEvent.value = true
        }
    }

    fun onClickHideButton() {
        if(isOpen) {
            _searchBoxEnableEvent.value = false
        }
    }

    fun onStateChange(isOpen: Boolean) {
        this.isOpen = isOpen
    }

    fun onClickSearchButton(keyword: String) {
        if (keyword.isEmpty()) {
            updateShowMessage(R.string.empty_keyword_guide)
        } else {
            _searchEvent.value = keyword
            saveKeywordToRepository(keyword)
        }
    }

    private fun saveKeywordToRepository(keyword: String) {
        searchLogRepository.insertOrUpdateSearchLog(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newLog ->
                _searchLogs.removeFirstAndAddFirst(newLog) { it.keyword == keyword }
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }
}