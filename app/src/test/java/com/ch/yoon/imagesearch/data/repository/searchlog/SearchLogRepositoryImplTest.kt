package com.ch.yoon.imagesearch.data.repository.searchlog

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLogModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
class SearchLogRepositoryImplTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockSearchLogLocalDataSource: SearchLogLocalDataSource

    private lateinit var imageRepository: SearchLogRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = SearchLogRepositoryImpl(mockSearchLogLocalDataSource)
    }

    @Test
    fun `로컬 데이터소스에 키워드 업데이트 요청을 전달하는지 테스트`() {
        // given
        every { mockSearchLogLocalDataSource.insertOrUpdateSearchLog("테스트") } returns Single.just(emptySearchLog())

        // when
        imageRepository.insertOrUpdateSearchLog("테스트")

        // then
        verify(exactly = 1) { mockSearchLogLocalDataSource.insertOrUpdateSearchLog("테스트") }
    }

    @Test
    fun `로컬 데이터소스에 검색 목록 요청을 전달하는지 테스트`() {
        // given
        every { mockSearchLogLocalDataSource.selectAllSearchLog() } returns Single.just(emptyList())

        // when
        imageRepository.requestSearchLogList()

        // then
        verify(exactly = 1) { mockSearchLogLocalDataSource.selectAllSearchLog() }
    }

    @Test
    fun `로컬 데이터소스에 키워드 데이터 삭제 요청을 전달하는지 테스트`() {
        // given
        val targetSearchLog = emptySearchLog()
        every { mockSearchLogLocalDataSource.deleteSearchLog(targetSearchLog) } returns Completable.complete()

        // when
        imageRepository.deleteSearchLog(emptySearchLog())

        // then
        verify(exactly = 1) { mockSearchLogLocalDataSource.deleteSearchLog(targetSearchLog) }
    }

    @Test
    fun `로컬 데이터소스에 검색 기록 전체 삭제 요청을 전달하는지 테스트`() {
        // given
        every { mockSearchLogLocalDataSource.deleteAllSearchLog() } returns Completable.complete()

        // when
        imageRepository.deleteAllSearchLog()

        // then
        verify(exactly = 1) { mockSearchLogLocalDataSource.deleteAllSearchLog() }
    }

    private fun emptySearchLog(): SearchLogModel {
        return SearchLogModel("", 0)
    }
}