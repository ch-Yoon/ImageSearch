package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository
import com.ch.yoon.kakao.pay.imagesearch.extention.*
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

    private val _searchLogList = MutableLiveData<MutableList<SearchLog>>().apply { value = mutableListOf() }
    val searchLogList: LiveData<List<SearchLog>> = Transformations.map(_searchLogList) { it.toList() }

    private val _searchBoxFocus = MutableLiveData<Boolean>().apply { value = false }
    val searchBoxFocus: LiveData<Boolean> = _searchBoxFocus

    private val _searchKeyword = SingleLiveEvent<String>()
    val searchKeyword: LiveData<String> = _searchKeyword

    private val _searchBoxFinishEvent = SingleLiveEvent<Unit>()
    val searchBoxFinishEvent: LiveData<Unit> = _searchBoxFinishEvent

    fun onClickSearchBox() {
        if (notHasSearchLogList()) {
            imageRepository.requestSearchLogList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ receivedSearchLogList ->
                    _searchLogList.value = receivedSearchLogList.sorted().toMutableList()
                }, { throwable ->
                    Log.d(TAG, throwable.message)
                })
                .register()
        }

        if (notHasFocus()) {
            _searchBoxFocus.value = true
        }
    }

    fun onClickSearchButton(keyword: String) {
        if (TextUtils.isEmpty(keyword)) {
            updateShowMessage(R.string.empty_keyword_guide)
        } else {
            _searchKeyword.value = keyword
            _searchBoxFocus.value = false

            imageRepository.insertOrUpdateSearchLog(keyword)
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
        imageRepository.deleteSearchLog(targetSearchLog.keyword)
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
        if(hasFocus()) {
            _searchBoxFocus.value = false
        } else {
            _searchBoxFinishEvent.call()
        }
    }

    private fun updateSearchLogList(newSearchLog: SearchLog) {
        _searchLogList.updateOnMainThread { beforeSearchLogList ->
            beforeSearchLogList?.apply {
                removeFirstIf { oldLog -> oldLog.keyword == newSearchLog.keyword }
                add(0, newSearchLog)
            } ?: mutableListOf(newSearchLog)
        }
    }

    private fun removeFromSearchLogList(targetSearchLog: SearchLog) {
        _searchLogList.updateOnMainThread { beforeSearchLogList ->
            beforeSearchLogList?.removeFirstIf { oldLog -> oldLog.keyword == targetSearchLog.keyword }
        }
    }

    private fun notHasFocus() = hasFocus().not()

    private fun hasFocus() = _searchBoxFocus.value ?: false

    private fun notHasSearchLogList() = hasSearchLogList().not()

    private fun hasSearchLogList() = _searchLogList.value?.isEmpty()?.not() ?: false

}