package com.ch.yoon.imagesearch.presentation.imagesearch.imagelist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.ImageRepository
import com.ch.yoon.imagesearch.data.repository.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.model.ImageSearchMeta
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import com.ch.yoon.imagesearch.presentation.common.pageload.PageLoadHelper
import com.ch.yoon.imagesearch.util.extension.*
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageListViewModel(
    application: Application,
    private val imageRepository: ImageRepository,
    private val pageLoadHelper: PageLoadHelper<String>
) : BaseViewModel(application) {

    init {
        observePageLoadInspector()
    }

    private val _imageSortType = MutableLiveData(ImageSortType.ACCURACY)
    val imageSortType: LiveData<ImageSortType> = _imageSortType

    private val _countOfItemInLine = MutableLiveData(2)
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

    private var searchMeta: ImageSearchMeta? = null

    fun changeImageSortType(imageSortType: ImageSortType) {
        _imageDocumentList.clear()
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
            safeLet(_countOfItemInLine.value, _imageDocumentList.value) { count, documents ->
                pageLoadHelper.requestPreloadIfPossible(displayPosition, documents.size, count)
            }
        }
    }

    fun onClickImage(imageDocument: ImageDocument) {
        _moveToDetailScreenEvent.value = imageDocument
    }

    private fun observePageLoadInspector() {
        pageLoadHelper.onPageLoadApprove = { key, pageNumber, dataSize, isFirstPage ->
            _imageSortType.value?.let { sortType ->
                val request = ImageSearchRequest(key, sortType, pageNumber, dataSize, isFirstPage)
                requestImageSearchToRepository(request)
            }
        }
    }

    private fun requestImageSearchToRepository(imageSearchRequest: ImageSearchRequest) {
        _imageSearchState.value = ImageSearchState.NONE

        imageRepository.requestImageList(imageSearchRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ imageSearchResponse ->
                _imageSearchState.value = ImageSearchState.SUCCESS

                if (imageSearchRequest.isFirstRequest) {
                    _imageDocumentList.clear()
                }

                with(imageSearchResponse) {
                    searchMeta = imageSearchMeta
                    updateImageDocumentList(imageDocumentList)
                }
            }, { throwable ->
                _imageSearchState.value = ImageSearchState.FAIL
                handlingImageSearchError(throwable)
            })
            .register()
    }

    private fun updateImageDocumentList(receivedImageDocumentList: List<ImageDocument>) {
        _imageDocumentList.addAll(receivedImageDocumentList)
        if(_imageDocumentList.isEmpty()) {
            updateShowMessage(R.string.success_image_search_no_result)
        } else if(isNotRemainingMoreData) {
            updateShowMessage(R.string.success_image_search_last_data)
        }
    }

    private fun handlingImageSearchError(throwable: Throwable) {
        when(throwable) {
            is RepositoryException.NetworkNotConnectingException -> {
                updateShowMessage(R.string.network_not_connecting_error)
            }
            else -> {
                updateShowMessage(R.string.unknown_error)
                Log.d(TAG, throwable.message ?: "unknown error")
            }
        }
    }

}