package com.ch.yoon.imagesearch.presentation.search.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogRepository
import com.ch.yoon.data.model.searchlog.SearchLog

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

    private val _searchLogs = MutableLiveData<MutableList<com.ch.yoon.data.model.searchlog.SearchLog>>(mutableListOf())
    val searchLogs: LiveData<List<com.ch.yoon.data.model.searchlog.SearchLog>> = Transformations.map(_searchLogs) { it?.toList() }

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val _searchBoxEnableEvent = SingleLiveEvent<Boolean>()
    val searchBoxEnableEvent: LiveData<Boolean> = _searchBoxEnableEvent

    private val _searchLogsEnableEvent = SingleLiveEvent<Boolean>()
    val searchLogsEnableEvent: LiveData<Boolean> = _searchLogsEnableEvent

    var isOpen = false
        private set

    fun loadSearchLogs() {
        searchLogRepository.getAllSearchLogs()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ receivedSearchLogList ->
                _searchLogs.value = receivedSearchLogList.sorted().toMutableList()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .disposeByOnCleared()
    }

    fun onClickSearchLogDeleteButton(searchLog: com.ch.yoon.data.model.searchlog.SearchLog) {
        val targetLog = _searchLogs.find { it == searchLog }
        if(targetLog != null) {
            _searchLogs.removeFirst { it == searchLog }
            searchLogRepository.deleteSearchLog(targetLog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateShowMessage(R.string.success_delete_search_log)
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .disposeByOnCleared()
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
            .disposeByOnCleared()
    }

    fun onClickShowButton() {
        _searchBoxEnableEvent.value = true
    }

    fun onClickHideButton() {
        _searchBoxEnableEvent.value = false
    }

    fun onStateChange(isOpen: Boolean) {
        this.isOpen = isOpen
    }

    fun onClickSearchButton(keyword: String) {
        _searchLogsEnableEvent.value = false

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
            .disposeByOnCleared()
    }
}