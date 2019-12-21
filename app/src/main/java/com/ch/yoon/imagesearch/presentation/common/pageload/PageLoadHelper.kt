package com.ch.yoon.imagesearch.presentation.common.pageload

import com.ch.yoon.imagesearch.extension.safeLet

/**
 * Creator : ch-yoon
 * Date : 2019-10-28.
 */
class PageLoadHelper<T>(private val config: PageLoadConfiguration) {

    private val previousApproveLog = PageLoadApproveLog<T>()

    var onPageLoadApproveCallback: ((key: T, pageNumber: Int, dataSize: Int, isFirstPage: Boolean) -> Unit)? = null

    fun requestFirstLoad(key: T) {
        approveFirstImageSearch(key)
    }

    fun requestPreloadIfPossible(currentPosition: Int, dataTotalSize: Int) {
        previousApproveLog.run {
            safeLet(key, pageNumber) { previousKey, previousPageNumber ->
                if (isPreloadPossible(currentPosition, dataTotalSize)) {
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

    fun getCurrentKey(): T? = previousApproveLog.key

    private fun approveFirstImageSearch(key: T) {
        initApproveRequestLog()
        approveImageSearch(key, config.startPageNumber, 0)
    }

    private fun initApproveRequestLog() {
        previousApproveLog.apply {
            key = null
            dataTotalSize = null
            pageNumber = config.startPageNumber-1
        }
    }

    private fun isPreloadPossible(displayPosition: Int, dataTotalSize: Int): Boolean {
        return isNotMaxPage() && isAllowPreloadRange(displayPosition, dataTotalSize)
    }

    private fun isNotMaxPage(): Boolean {
        return previousApproveLog.pageNumber?.let { previousPageNumber ->
            previousPageNumber < config.maxPageNumber
        } ?: false
    }

    private fun isAllowPreloadRange(displayPosition: Int, dataTotalSize: Int): Boolean {
        return previousApproveLog.dataTotalSize?.let { previousDataTotalSize ->
            val preloadAllowLimit = dataTotalSize - config.preloadAllowCount
            (dataTotalSize > previousDataTotalSize) && (displayPosition > preloadAllowLimit)
        } ?: false
    }

    private fun approveImageSearch(key: T, pageNumber: Int, dataTotalSize: Int) {
        recordApproveRequest(key, dataTotalSize, pageNumber)
        onPageLoadApproveCallback?.invoke(key, pageNumber, config.requiredDataSize, pageNumber == config.startPageNumber)
    }

    private fun recordApproveRequest(key: T, dataTotalSize: Int, pageNumber: Int) {
        previousApproveLog.apply {
            this.key = key
            this.dataTotalSize = dataTotalSize
            this.pageNumber = pageNumber
        }
    }
}