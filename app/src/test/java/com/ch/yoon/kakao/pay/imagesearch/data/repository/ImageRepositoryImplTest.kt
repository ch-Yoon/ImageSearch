package com.ch.yoon.kakao.pay.imagesearch.data.repository

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageSearchMeta
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class ImageRepositoryImplTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockImageRemoteDataSource: ImageRemoteDataSource
    @MockK
    private lateinit var mockImageLocalDataSource: ImageLocalDataSource

    private lateinit var imageRepository: ImageRepository
    private lateinit var compositeDisposable: CompositeDisposable

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = ImageRepositoryImpl(mockImageRemoteDataSource, mockImageLocalDataSource)
        compositeDisposable = CompositeDisposable()
    }

    @After
    fun clear() {
        compositeDisposable.clear()
    }

    @Test
    fun `리모트 데이터소스에 이미지 요청을 전달하는지 테스트`() {
        // given
        every { mockImageRemoteDataSource.requestImageList(any()) } returns Single.just(emptyImageSearchResponse())

        // when
        val request = emptyImageSearchRequest()
        imageRepository.requestImageList(request)

        // then
        verify(exactly = 1) { mockImageRemoteDataSource.requestImageList(request) }
    }

    @Test
    fun `로컬 데이터소스에 키워드 업데이트 요청을 전달하는지 테스트`() {
        // given
        every { mockImageLocalDataSource.insertOrUpdateSearchLog("테스트") } returns Single.just(emptySearchLog())

        // when
        imageRepository.insertOrUpdateSearchLog("테스트")

        // then
        verify(exactly = 1) { mockImageLocalDataSource.insertOrUpdateSearchLog("테스트") }
    }

    @Test
    fun `로컬 데이터소스에 검색 목록 요청을 전달하는지 테스트`() {
        // given
        every { mockImageLocalDataSource.selectAllSearchLog() } returns Single.just(emptyList())

        // when
        imageRepository.requestSearchLogList()

        // then
        verify(exactly = 1) { mockImageLocalDataSource.selectAllSearchLog() }
    }

    @Test
    fun `로컬 데이터소스에 키워드 데이터 삭제 요청을 전달하는지 테스트`() {
        // given
        val targetSearchLog = emptySearchLog()
        every { mockImageLocalDataSource.deleteSearchLog(targetSearchLog) } returns Completable.complete()

        // when
        imageRepository.deleteSearchLog(emptySearchLog())

        // then
        verify(exactly = 1) { mockImageLocalDataSource.deleteSearchLog(targetSearchLog) }
    }

    private fun emptySearchLog(): SearchLog {
        return SearchLog("", 0)
    }

    private fun emptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, true)
    }

    private fun emptyImageSearchResponse(): ImageSearchResponse {
        val imageSearchMeta = ImageSearchMeta(false)
        val imageDocumentList = mutableListOf<ImageDocument>()
        return ImageSearchResponse(imageSearchMeta, imageDocumentList)
    }
}