package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageSearchRepository
import com.ch.yoon.kakao.pay.imagesearch.extention.*
import com.ch.yoon.kakao.pay.imagesearch.ui.base.KBaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-25.
 */
class SearchBoxViewModel(
    application: Application,
    private val imageSearchRepository: ImageSearchRepository
) : KBaseViewModel (application) {

    private val _searchLogList = MutableLiveData<MutableList<SearchLog>>().apply { value = mutableListOf() }
    val searchLogList: LiveData<List<SearchLog>> = Transformations.map(_searchLogList) { it.toList() }

    private val _searchBoxFocus = MutableLiveData<Boolean>().apply { value = false }
    val searchBoxFocus: LiveData<Boolean> = _searchBoxFocus

    private val _searchKeyword = SingleLiveEvent<String>()
    val searchKeyword: LiveData<String> = _searchKeyword

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    private val notHasFocus
        get() = hasFocus.not()

    private val hasFocus
        get() = _searchBoxFocus.value == true

    fun loadSearchLogList() {
        imageSearchRepository.requestSearchLogList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ receivedSearchLogList ->
                _searchLogList.value = receivedSearchLogList.sorted().toMutableList()
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickSearchBox() {
        if(notHasFocus) {
            _searchBoxFocus.value = true
        }
    }

    fun onClickSearchButton(keyword: String) {
        if (TextUtils.isEmpty(keyword)) {
            updateShowMessage(R.string.empty_keyword_guide)
        } else {
            _searchKeyword.value = keyword
            _searchBoxFocus.value = false

            imageSearchRepository.insertOrUpdateSearchLog(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updatedSearchLog ->
                    updateSearchLogList(updatedSearchLog)
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .register()
        }
    }

    fun onClickSearchLogDeleteButton(targetSearchLog: SearchLog) {
        imageSearchRepository.deleteSearchLog(targetSearchLog.keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                removeFromSearchLogList(targetSearchLog)
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .register()
    }

    fun onClickBackground() {
        _searchBoxFocus.value = false
    }

    fun onClickBackPressButton() {
        if(hasFocus) {
            _searchBoxFocus.value = false
        } else {
            _searchBoxFinishEvent.call()
        }
    }

    private fun updateSearchLogList(newLog: SearchLog) {
        _searchLogList.updateOnMainThread { currentSearchLogList ->
            currentSearchLogList?.removeFirstIf { oldLog -> oldLog.keyword == newLog.keyword }
                ?.addFirst(newLog)
                ?: mutableListOf(newLog)
        }
    }

    private fun removeFromSearchLogList(targetLog: SearchLog) {
        _searchLogList.updateOnMainThread { currentSearchLogList ->
            currentSearchLogList?.removeFirstIf { oldLog -> oldLog.keyword == targetLog.keyword }
        }
    }

}