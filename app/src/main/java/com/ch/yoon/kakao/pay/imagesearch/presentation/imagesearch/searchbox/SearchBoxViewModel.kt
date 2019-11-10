package com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.util.extension.*
import com.ch.yoon.kakao.pay.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.presentation.common.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-25.
 */
class SearchBoxViewModel(
    application: Application,
    private val imageRepository: ImageRepository
) : BaseViewModel (application) {

    private val _searchLogList = MutableLiveData<MutableList<SearchLog>>(mutableListOf())
    val searchLogList: LiveData<List<SearchLog>> = Transformations.map(_searchLogList) { it?.toList() }

    private val _searchKeyword = SingleLiveEvent<String>()
    val searchKeyword: LiveData<String> = _searchKeyword

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val _searchBoxCloseEvent = SingleLiveEvent<Unit>()
    val searchBoxCloseEvent: LiveData<Unit> = _searchBoxCloseEvent

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    private var currentKeyword: String = ""

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

    fun onClickSearchLogDeleteButton(targetSearchLog: SearchLog) {
        imageRepository.deleteSearchLog(targetSearchLog)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                removeFromSearchLogList(targetSearchLog)
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

    fun onChangeKeyword(keyword: String) {
        currentKeyword = keyword
    }

    fun onClickSearchButton() {
        processingSearchApproval()
    }

    fun onClickSearchLog(searchLog: SearchLog) {
        currentKeyword = searchLog.keyword
        processingSearchApproval()
    }

    private fun processingSearchApproval() {
        if(checkSearchPossibility()) {
            _searchEvent.value = currentKeyword
            saveKeywordToRepository()
        }
    }

    private fun checkSearchPossibility(): Boolean {
        return if (currentKeyword.isEmpty()) {
            updateShowMessage(R.string.empty_keyword_guide)
            false
        } else {
            true
        }
    }

    private fun saveKeywordToRepository() {
        imageRepository.insertOrUpdateSearchLog(currentKeyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ updatedSearchLog ->
                updateSearchLogList(updatedSearchLog)
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    private fun updateSearchLogList(newSearchLog: SearchLog) {
        _searchLogList.updateOnMainThread { currentSearchLogList ->
            currentSearchLogList?.removeFirstIf { oldLog -> oldLog.keyword == newSearchLog.keyword }
                ?.addFirst(newSearchLog)
                ?: mutableListOf()
        }
    }

    private fun removeFromSearchLogList(targetSearchLog: SearchLog) {
        _searchLogList.updateOnMainThread { currentSearchLogList ->
            currentSearchLogList?.removeFirstIf { oldLog -> oldLog.keyword == targetSearchLog.keyword }
        }
    }
}