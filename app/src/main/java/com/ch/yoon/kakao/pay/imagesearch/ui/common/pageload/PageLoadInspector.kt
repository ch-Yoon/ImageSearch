package com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload

import com.ch.yoon.kakao.pay.imagesearch.extention.safeLet

/**
 * Creator : ch-yoon
 * Date : 2019-10-28.
 */
class PageLoadInspector<T>(private val pageLoadConfiguration: PageLoadConfiguration) {

    private val previousApproveLog = PageLoadApproveLog<T>()

    var onPageLoadApprove: ((key: T, pageNumber: Int, dataSize: Int) -> Unit)? = null

    fun requestFirstLoad(key: T) {
        approveFirstImageSearch(key)
    }

    fun requestPreloadIfPossible(
        currentPosition: Int,
        dataTotalSize: Int,
        countOfItemInLine: Int = 1
    ) {
        previousApproveLog.run {
            safeLet(key, pageNumber) { previousKey, previousPageNumber ->
                if (isPreloadPossible(currentPosition, dataTotalSize, countOfItemInLine)) {
                    approveImageSearch(previousKey, previousPageNumber+1, dataTotalSize)
                }
            }
        }
    }

    fun requestRetryAsPreviousValue() {
        previousApproveLog.run {
            safeLet(key, pageNumber, dataTotalSize) { currentKey, previousPageNumber, previousDataTotalSize ->
                approveImageSearch(currentKey, previousPageNumber, previousDataTotalSize)
            }
        }
    }

    fun requestStartOverFromTheBegining() {
        previousApproveLog.key?.let { currentKey ->
            approveImageSearch(currentKey, pageLoadConfiguration.startPageNumber, 0)
        }
    }

    private fun approveFirstImageSearch(key: T) {
        initApproveRequestLog()
        approveImageSearch(key, pageLoadConfiguration.startPageNumber, 0)
    }

    private fun initApproveRequestLog() {
        previousApproveLog.apply {
            key = null
            dataTotalSize = null
            pageNumber = pageLoadConfiguration.startPageNumber-1
        }
    }

    private fun isPreloadPossible(
        displayPosition: Int,
        dataTotalSize: Int,
        countOfItemInLine: Int
    ): Boolean {
        return isNotMaxPage() && isAllowPreloadRange(displayPosition, dataTotalSize, countOfItemInLine)
    }

    private fun isNotMaxPage(): Boolean {
        return previousApproveLog.pageNumber?.let { previousPageNumber ->
            previousPageNumber < pageLoadConfiguration.maxPageNumber
        } ?: false
    }

    private fun isAllowPreloadRange(
        displayPosition: Int,
        dataTotalSize: Int,
        countOfItemInLine: Int
    ): Boolean {
        return previousApproveLog.dataTotalSize?.let { previousDataTotalSize ->
            val preloadAllowLimit = dataTotalSize - (countOfItemInLine * pageLoadConfiguration.preloadAllowLineMultiple)
            (dataTotalSize > previousDataTotalSize) && (displayPosition > preloadAllowLimit)
        } ?: false
    }

    private fun approveImageSearch(key: T, pageNumber: Int, dataTotalSize: Int) {
        recordApproveRequest(key, dataTotalSize, pageNumber)
        onPageLoadApprove?.invoke(key, pageNumber, pageLoadConfiguration.requiredDataSize)
    }

    private fun recordApproveRequest(key: T, dataTotalSize: Int, pageNumber: Int) {
        previousApproveLog.apply {
            this.key = key
            this.dataTotalSize = dataTotalSize
            this.pageNumber = pageNumber
        }
    }

}