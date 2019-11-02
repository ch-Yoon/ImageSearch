package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.extension.*
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent
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

    private val _searchBoxFocus = MutableLiveData<Boolean>(false)
    val searchBoxFocus: LiveData<Boolean> = _searchBoxFocus

    private val _searchKeyword = SingleLiveEvent<String>()
    val searchKeyword: LiveData<String> = _searchKeyword

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    private val _searchEvent = SingleLiveEvent<String>()
    val searchEvent: LiveData<String> = _searchEvent

    private val notHasFocus
        get() = hasFocus.not()

    private val hasFocus
        get() = _searchBoxFocus.value ?: false

    private val currentKeyword
        get() = _searchKeyword.value ?: ""

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

    fun onClickSearchBox() {
        if(notHasFocus) {
            _searchBoxFocus.value = true
        }
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

    fun onChangeKeyword(keyword: String) {
        changeKeyword(keyword)
    }

    fun onClickSearchButton() {
        validationSearchKeyword()
    }

    fun onClickSearchButton(keyword: String) {
        changeKeyword(keyword)
        validationSearchKeyword()
    }

    private fun changeKeyword(keyword: String) {
        if(currentKeyword != keyword) {
            _searchKeyword.value = keyword
        }
    }

    private fun validationSearchKeyword() {
        if (currentKeyword.isEmpty()) {
            updateShowMessage(R.string.empty_keyword_guide)
        } else {
            _searchEvent.value = currentKeyword
            _searchBoxFocus.value = false

            imageRepository.insertOrUpdateSearchLog(currentKeyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updatedSearchLog ->
                    updateSearchLogList(updatedSearchLog)
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .register()
        }
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