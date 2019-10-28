package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSortType
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchException
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageSearchRepository
import com.ch.yoon.kakao.pay.imagesearch.extention.TAG
import com.ch.yoon.kakao.pay.imagesearch.ui.base.KBaseViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.OnImageSearchApproveListener
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class KImageListViewModel(
    application: Application,
    private val imageSearchRepository: ImageSearchRepository,
    private val imageSearchInspector: ImageSearchInspector
) : KBaseViewModel(application) {

    init {
        observeImageSearchApprove()
    }

    companion object {
        private val DEFAULT_IMAGE_SORT_TYPE = ImageSortType.ACCURACY
        private val DEFAULT_COUNT_OF_ITEM_IN_LINE = 2
    }

    private val _imageSortType = MutableLiveData<ImageSortType>().apply { value = DEFAULT_IMAGE_SORT_TYPE }
    val imageSortType: LiveData<ImageSortType> = _imageSortType

    private val _countOfItemInLine = MutableLiveData<Int>().apply { value = DEFAULT_COUNT_OF_ITEM_IN_LINE }
    val countOfItemInLine: LiveData<Int> = _countOfItemInLine

    private val _imageDocumentList = MutableLiveData<List<ImageDocument>>()
    val imageDocumentList: LiveData<List<ImageDocument>> = _imageDocumentList

    private val _imageSearchState = MutableLiveData<ImageSearchState>().apply { value = ImageSearchState.NONE }
    val imageSearchState: LiveData<ImageSearchState> = _imageSearchState

    private var searchMetaInfo: SearchMetaInfo? = null

    private val currentImageSortType: ImageSortType
        get() = _imageSortType.value ?: ImageSortType.ACCURACY

    fun changeImageSortType(imageSortType: ImageSortType) {
        _imageDocumentList.value = null
        _imageSortType.value = imageSortType

        val previousKeyword = imageSearchInspector.previousRequestKeyword
        if (!TextUtils.isEmpty(previousKeyword)) {
            imageSearchInspector.submitFirstImageSearchRequest(previousKeyword, getImageSortType())
        }
    }

    fun changeCountOfItemInLine(countOfItemInLine: Int) {
        _countOfItemInLine.value = countOfItemInLine
    }

    fun loadImageList(keyword: String) {
        _imageDocumentList.value = null
        imageSearchInspector.submitFirstImageSearchRequest(keyword, getImageSortType())
    }

    fun retryLoadMoreImageList() {
        imageSearchInspector.submitRetryRequest(getImageSortType())
    }

    fun loadMoreImageListIfPossible(displayPosition: Int) {
        if (isRemainingMoreData()) {
            val imageDocumentList = _imageDocumentList.value
            imageSearchInspector.submitPreloadRequest(
                displayPosition,
                imageDocumentList?.size ?: 0,
                getImageSortType(),
                getCountOfItemInLine()
            )
        }
    }

    private fun observeImageSearchApprove() {
        imageSearchInspector.observeImageSearchApprove { this.requestImageSearchToRepository(it) }
    }

    private fun requestImageSearchToRepository(imageSearchRequest: ImageSearchRequest) {
        changeImageSearchState(ImageSearchState.NONE)

            imageSearchRepository.requestImageList(imageSearchRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ imageSearchResult ->
                    changeImageSearchState(ImageSearchState.SUCCESS)
                    clearBeforeDocumentListIfFirstRequest(imageSearchResult.getImageSearchRequest())
                    updateSearchMetaInfo(imageSearchResult.getSearchMetaInfo())
                    updateImageInfoList(imageSearchResult.getImageDocumentList())
                }, { throwable ->
                    changeImageSearchState(ImageSearchState.FAIL)
                    when(throwable) {
                        is ImageSearchException -> {
                            val error = throwable.imageSearchError
                            updateShowMessage(error.errorMessageResourceId)
                        }
                        else -> {
                            Log.d(TAG, throwable.message)
                        }
                    }
                })
                .register()
    }

    private fun clearBeforeDocumentListIfFirstRequest(imageSearchRequest: ImageSearchRequest) {
        if (imageSearchRequest.isFirstRequest) {
            _imageDocumentList.value = null
        }
    }

    private fun updateSearchMetaInfo(searchMetaInfo: SearchMetaInfo?) {
        this.searchMetaInfo = searchMetaInfo
    }

    private fun updateImageInfoList(receivedImageDocumentList: List<ImageDocument>) {
        val oldList = _imageDocumentList.value
        val newList = ArrayList(oldList ?: ArrayList())
        newList.addAll(receivedImageDocumentList)

        if (CollectionUtil.isEmpty(newList)) {
            updateShowMessage(R.string.success_image_search_no_result)
        } else if (isNotRemainingMoreData()) {
            updateShowMessage(R.string.success_image_search_last_data)
        }

        _imageDocumentList.value = newList
    }


    private fun isNotRemainingMoreData(): Boolean {
        return !isRemainingMoreData()
    }

    private fun isRemainingMoreData(): Boolean {
        return searchMetaInfo == null || !searchMetaInfo!!.isEnd
    }

    private fun changeImageSearchState(imageSearchState: ImageSearchState) {
        val previousImageSearchState = _imageSearchState.value
        if (previousImageSearchState !== imageSearchState) {
            _imageSearchState.value = imageSearchState
        }
    }

    private fun getImageSortType(): ImageSortType {
        val imageSortType = _imageSortType.value
        return imageSortType ?: DEFAULT_IMAGE_SORT_TYPE
    }

    private fun getCountOfItemInLine(): Int {
        val countOfItemInLine = _countOfItemInLine.value
        return countOfItemInLine ?: DEFAULT_COUNT_OF_ITEM_IN_LINE
    }
}