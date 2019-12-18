package com.ch.yoon.imagesearch.presentation.search.imagesearch

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.presentation.base.BaseViewModel
import com.ch.yoon.imagesearch.presentation.common.livedata.NonNullMutableLiveData
import com.ch.yoon.imagesearch.presentation.common.livedata.SingleLiveEvent
import com.ch.yoon.imagesearch.presentation.common.pageload.PageLoadHelper
import com.ch.yoon.imagesearch.extension.*
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageSearchViewModel(
    application: Application,
    private val imageRepository: ImageRepository,
    private val pageLoadHelper: PageLoadHelper<String>
) : BaseViewModel(application) {

    private val _imageSortType = NonNullMutableLiveData(ImageSortType.ACCURACY)
    val imageSortType: LiveData<ImageSortType> = _imageSortType

    private val _countOfItemInLine = NonNullMutableLiveData(2)
    val countOfItemInLine: LiveData<Int> = _countOfItemInLine

    private val _imageDocuments = MutableLiveData<MutableList<ImageDocument>>()
    val imageDocuments: LiveData<List<ImageDocument>> = Transformations.map(_imageDocuments) { it?.toList() }

    private val _imageSearchState = MutableLiveData<ImageSearchState>(ImageSearchState.NONE)
    val imageSearchState: LiveData<ImageSearchState> = _imageSearchState

    private val _moveToDetailScreenEvent = SingleLiveEvent<ImageDocument>()
    val moveToDetailScreenEvent: LiveData<ImageDocument> = _moveToDetailScreenEvent

    private var imageSearchMeta: ImageSearchMeta? = null

    private val isRemainingMoreData
        get() = imageSearchMeta?.isEnd?.not() ?: true

    init {
        observeChangingFavoriteImage()
        observePageLoadInspector()
    }

    fun changeCountOfItemInLine(countOfItemInLine: Int) {
        _countOfItemInLine.value = countOfItemInLine
    }

    fun onClickImage(imageDocument: ImageDocument) {
        _moveToDetailScreenEvent.value = imageDocument
    }

    fun changeImageSortType(imageSortType: ImageSortType) {
        _imageDocuments.clear()
        _imageSortType.value = imageSortType
        pageLoadHelper.getCurrentKey()?.let { currentKey ->
            pageLoadHelper.requestFirstLoad(currentKey)
        }
    }

    fun loadImageList(keyword: String) {
        _imageDocuments.clear()
        pageLoadHelper.requestFirstLoad(keyword)
    }

    fun retryLoadMoreImageList() {
        pageLoadHelper.requestRetryAsPreviousValue()
    }

    fun loadMoreImageListIfPossible(position: Int) {
        if (isRemainingMoreData) {
            pageLoadHelper.requestPreloadIfPossible(position, _imageDocuments.size(), _countOfItemInLine.value)
        }
    }

    private fun observePageLoadInspector() {
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            val request = ImageSearchRequest(key, _imageSortType.value, pageNumber, dataSize, isFirstPage)
            requestImagesToRepository(request)
        }
    }

    private fun observeChangingFavoriteImage() {
        imageRepository.observeChangingFavoriteImage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ changedImageDocument ->
                _imageDocuments.replace(changedImageDocument) { it.id == changedImageDocument.id }
            }, { throwable ->
                Log.d(TAG, throwable.message)
            })
            .disposeByOnCleared()
    }

    private fun requestImagesToRepository(request: ImageSearchRequest) {
        _imageSearchState.value = ImageSearchState.NONE

        imageRepository.getImages(request)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { _imageSearchState.value = ImageSearchState.SUCCESS }
            .doOnError { _imageSearchState.value = ImageSearchState.FAIL }
            .subscribe({ response ->
                updateImageDocuments(request, response.imageDocuments)
                updateSearchMeta(response.imageSearchMeta)
            }, { throwable ->
                handlingImageSearchError(throwable)
            })
            .disposeByOnCleared()
    }

    private fun updateImageDocuments(
        previousRequest: ImageSearchRequest,
        receivedImageDocuments: List<ImageDocument>
    ) {
        _imageDocuments.apply {
            if (previousRequest.isFirstRequest) {
                clear()
            }
            addAll(receivedImageDocuments)
        }.also {
            if(it.isEmpty()) {
                updateShowMessage(R.string.success_image_search_no_result)
            }
        }
    }

    private fun updateSearchMeta(searchMeta: ImageSearchMeta) {
        imageSearchMeta = searchMeta
        if(_imageDocuments.isNotEmpty() && searchMeta.isEnd) {
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