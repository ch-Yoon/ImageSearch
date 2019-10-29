package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSortType
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchException
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageSearchRepository
import com.ch.yoon.kakao.pay.imagesearch.extention.TAG
import com.ch.yoon.kakao.pay.imagesearch.extention.updateOnMainThread
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.NotNullMutableLiveData
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent
import com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload.PageLoadHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageListViewModel(
    application: Application,
    private val imageSearchRepository: ImageSearchRepository,
    private val pageLoadHelper: PageLoadHelper<String>
) : BaseViewModel(application) {

    init {
        observePageLoadInspector()
    }

    private val _imageSortType = NotNullMutableLiveData(ImageSortType.ACCURACY)
    val imageSortType: LiveData<ImageSortType> = _imageSortType

    private val _countOfItemInLine = NotNullMutableLiveData(2)
    val countOfItemInLine: LiveData<Int> = _countOfItemInLine

    private val _imageDocumentList = MutableLiveData<MutableList<ImageDocument>>()
    val imageDocumentList: LiveData<List<ImageDocument>> = Transformations.map(_imageDocumentList) { it?.toList() }

    private val _imageSearchState = MutableLiveData<ImageSearchState>(ImageSearchState.NONE)
    val imageSearchState: LiveData<ImageSearchState> = _imageSearchState

    private val _moveToDetailScreenEvent = SingleLiveEvent<ImageDocument>()
    val moveToDetailScreenEvent: LiveData<ImageDocument> = _moveToDetailScreenEvent

    private val isNotRemainingMoreData
        get() = isRemainingMoreData.not()

    private val isRemainingMoreData
        get() = searchMeta?.isEnd?.not() ?: true

    private var searchMeta: SearchMetaInfo? = null

    fun changeImageSortType(imageSortType: ImageSortType) {
        _imageDocumentList.value = null
        _imageSortType.value = imageSortType
        pageLoadHelper.requestStartOverFromTheBeginning()
    }

    fun changeCountOfItemInLine(countOfItemInLine: Int) {
        _countOfItemInLine.value = countOfItemInLine
    }

    fun loadImageList(keyword: String) {
        _imageDocumentList.value = null
        pageLoadHelper.requestFirstLoad(keyword)
    }

    fun retryLoadMoreImageList() {
        pageLoadHelper.requestRetryAsPreviousValue()
    }

    fun loadMoreImageListIfPossible(displayPosition: Int) {
        if (isRemainingMoreData) {
            pageLoadHelper.requestPreloadIfPossible(
                displayPosition,
                _imageDocumentList.value?.size ?: 0,
                _countOfItemInLine.value
            )
        }
    }

    fun onClickImage(imageDocument: ImageDocument) {
        _moveToDetailScreenEvent.value = imageDocument
    }

    private fun observePageLoadInspector() {
        pageLoadHelper.onPageLoadApprove = { key, pageNumber, dataSize ->
            val request = ImageSearchRequest(key, _imageSortType.value, pageNumber, dataSize, pageNumber == 1)
            requestImageSearchToRepository(request)
        }
    }

    private fun requestImageSearchToRepository(imageSearchRequest: ImageSearchRequest) {
        _imageSearchState.value = ImageSearchState.NONE

        imageSearchRepository.requestImageList(imageSearchRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ imageSearchResult ->
                _imageSearchState.value = ImageSearchState.SUCCESS
                handlingImageSearchResult(imageSearchResult)
            }, { throwable ->
                _imageSearchState.value = ImageSearchState.FAIL
                handlingImageSearchError(throwable)
            })
            .register()
    }

    private fun handlingImageSearchResult(imageSearchResult: ImageSearchResult) {
        imageSearchResult.run {
            clearBeforeDocumentListIfFirstRequest(imageSearchRequest)
            updateSearchMetaInfo(searchMetaInfo)
            updateImageDocumentList(imageDocumentList)
        }
    }

    private fun clearBeforeDocumentListIfFirstRequest(imageSearchRequest: ImageSearchRequest) {
        if (imageSearchRequest.isFirstRequest) {
            _imageDocumentList.value = null
        }
    }

    private fun updateSearchMetaInfo(searchMetaInfo: SearchMetaInfo?) {
        searchMeta = searchMetaInfo
    }

    private fun updateImageDocumentList(receivedImageDocumentList: List<ImageDocument>) {
        _imageDocumentList.updateOnMainThread { oldList ->
            val newList = oldList ?: ArrayList()
            newList.addAll(receivedImageDocumentList)
            if(newList.isEmpty()) {
                updateShowMessage(R.string.success_image_search_no_result)
            } else if(isNotRemainingMoreData) {
                updateShowMessage(R.string.success_image_search_last_data)
            }
            newList
        }
    }

    private fun handlingImageSearchError(throwable: Throwable) {
        when(throwable) {
            is ImageSearchException -> {
                updateShowMessage(throwable.errorMessageResourceId)
            }
            else -> {
                Log.d(TAG, throwable.message)
            }
        }
    }

}