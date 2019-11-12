package com.ch.yoon.imagesearch.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.data.remote.kakao.ImageRemoteDataSourceImpl
import com.ch.yoon.imagesearch.data.remote.kakao.KakaoSearchApi
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchMetaInfo
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class ImageRemoteDataSourceImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockKakaoSearchApi: KakaoSearchApi

    private lateinit var imageRemoteDataSource: ImageRemoteDataSource
    private lateinit var compositeDisposable: CompositeDisposable

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRemoteDataSource = ImageRemoteDataSourceImpl(mockKakaoSearchApi)
        compositeDisposable = CompositeDisposable()
    }

    @After
    fun clear() {
        compositeDisposable.clear()
    }

    @Test
    fun `이미지 요청이 들어왔을 떄, kakaoSearchAPI로 요청을 하는지 테스트`() {
        // given
        val emptyResponse = createEmptyKakaoImageSearchResponse()
        every { mockKakaoSearchApi.searchImageList(any(), any(), any(), any()) } returns Single.just(emptyResponse)

        // when
        imageRemoteDataSource.requestImageList(createEmptyImageSearchRequest())

        // then
        verify(exactly = 1) { mockKakaoSearchApi.searchImageList(any(), any(), any(), any()) }
    }

    @Test
    fun `이미지 요청이 들어왔을 때 KakaoSearchAPI로부터 수신한 데이터를 반환하는지 테스트`() {
        // given
        val emptyResponse = createEmptyKakaoImageSearchResponse()
        every { mockKakaoSearchApi.searchImageList(any(), any(), any(), any()) } returns Single.just(emptyResponse)

        // when
        var response: ImageSearchResponse? = null
        imageRemoteDataSource.requestImageList(createEmptyImageSearchRequest())
            .subscribe({ imageSearchResponse ->
                response = imageSearchResponse
            }, { throwable ->
                response = null
            })

        // then
        assertEquals(true, response != null)
    }

    @Test
    fun `이미지 요청 후 에러가 발생했을 때, Repository Exception으로 변환하는지 테스트`() {
        // given
        every { mockKakaoSearchApi.searchImageList(any(), any(), any(), any()) } returns Single.error(Exception())

        // when && then
        var throwableCount = 0
        imageRemoteDataSource.requestImageList(createEmptyImageSearchRequest())
            .subscribe ({ response ->
                assertTrue(false)
            }, { throwable ->
                throwableCount = 1
                when(throwable) {
                    is RepositoryException -> {
                        assertTrue(true)
                    }
                    else -> {
                        assertTrue(false)
                    }
                }
            })

        assertEquals(1, throwableCount)
    }

    private fun createEmptyKakaoImageSearchResponse(): KakaoImageSearchResponse {
        val meta = KakaoImageSearchMetaInfo(0, 0, false)
        val list = mutableListOf<KakaoImageDocument>()
        return KakaoImageSearchResponse(meta, list)
    }

    private fun createEmptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, false)
    }
}