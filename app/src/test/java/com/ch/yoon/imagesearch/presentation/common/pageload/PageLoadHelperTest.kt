package com.ch.yoon.imagesearch.presentation.common.pageload

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class PageLoadHelperTest {

    private lateinit var pageLoadHelper: PageLoadHelper<String>

    @Test
    fun `최초 페이지 로드 요청시 승인을 하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 3, 10, 1)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var actualKey: String? = null
        var actualPageNumber: Int? = null
        var actualDataSize: Int? = null
        var actualIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            actualKey = key
            actualPageNumber = pageNumber
            actualDataSize = dataSize
            actualIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestFirstLoad("테스트")

        // then
        assertEquals("테스트", actualKey)
        assertEquals(1, actualPageNumber)
        assertEquals(10, actualDataSize)
        assertEquals(true, actualIsFirstPage)
    }

    @Test
    fun `추가 페이지 로드 요청 시 승인을 하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 3, 10, 2)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var actualKey: String? = null
        var actualPageNumber: Int? = null
        var actualDataSize: Int? = null
        var actualIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            actualKey = key
            actualPageNumber = pageNumber
            actualDataSize = dataSize
            actualIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestFirstLoad("테스트")
        pageLoadHelper.requestPreloadIfPossible(9, 10, 1)

        assertEquals("테스트", actualKey)
        assertEquals(2, actualPageNumber)
        assertEquals(10, actualDataSize)
        assertEquals(false, actualIsFirstPage)
    }

    @Test
    fun `추가 페이지 로드 요청 시, 현재 포지션이 프리로드 허용 범위안에 속하지 못한다면 승인을 안하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 3, 10, 2)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var actualKey: String? = null
        var actualPageNumber: Int? = null
        var actualDataSize: Int? = null
        var actualIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            actualKey = key
            actualPageNumber = pageNumber
            actualDataSize = dataSize
            actualIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestFirstLoad("테스트")
        pageLoadHelper.requestPreloadIfPossible(1, 10, 1)

        assertEquals("테스트", actualKey)
        assertEquals(1, actualPageNumber)
        assertEquals(10, actualDataSize)
        assertEquals(true, actualIsFirstPage)
    }

    @Test
    fun `추가 페이지 로드 요청 시, 마지막 페이지라면 승인을 안하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 1, 10, 2)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var approveCount = 0
        pageLoadHelper.onPageLoadApproveCallback = { _, _, _, _ ->
            approveCount++
        }
        pageLoadHelper.requestFirstLoad("테스트")
        pageLoadHelper.requestPreloadIfPossible(9, 10, 1)

        assertEquals(1, approveCount)
    }

    @Test
    fun `처음부터 재시작 요청 시 같은 key로 최초 요청을 발행하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 1, 10, 2)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var firstKey: String? = null
        var firstPageNumber: Int? = null
        var firstDataSize: Int? = null
        var firstIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            firstKey = key
            firstPageNumber = pageNumber
            firstDataSize = dataSize
            firstIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestFirstLoad("테스트")

        var targetKey: String? = null
        var targetPageNumber: Int? = null
        var targetDataSize: Int? = null
        var targetIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            targetKey = key
            targetPageNumber = pageNumber
            targetDataSize = dataSize
            targetIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestStartOverFromTheBeginning()

        // then
        assertEquals(firstKey, targetKey)
        assertEquals(firstPageNumber, targetPageNumber)
        assertEquals(firstDataSize, targetDataSize)
        assertEquals(firstIsFirstPage, targetIsFirstPage)
    }

    @Test
    fun `직전 값과 동일한 값 재시도 요청 시 동일한 값으로 승인하는지 테스트`() {
        // given
        val config = PageLoadConfiguration(1, 3, 10, 2)
        pageLoadHelper = PageLoadHelper(config)

        // when
        var baseKey: String? = null
        var basePageNumber: Int? = null
        var baseDataSize: Int? = null
        var baseIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            baseKey = key
            basePageNumber = pageNumber
            baseDataSize = dataSize
            baseIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestFirstLoad("테스트")
        pageLoadHelper.requestPreloadIfPossible(10, 10, 1)
        pageLoadHelper.requestPreloadIfPossible(20, 10, 1)

        var targetKey: String? = null
        var targetPageNumber: Int? = null
        var targetDataSize: Int? = null
        var targetIsFirstPage: Boolean? = null
        pageLoadHelper.onPageLoadApproveCallback = { key, pageNumber, dataSize, isFirstPage ->
            targetKey = key
            targetPageNumber = pageNumber
            targetDataSize = dataSize
            targetIsFirstPage = isFirstPage
        }
        pageLoadHelper.requestRetryAsPreviousValue()

        // then
        assertEquals(baseKey, targetKey)
        assertEquals(basePageNumber, targetPageNumber)
        assertEquals(baseDataSize, targetDataSize)
        assertEquals(baseIsFirstPage, targetIsFirstPage)
    }

}