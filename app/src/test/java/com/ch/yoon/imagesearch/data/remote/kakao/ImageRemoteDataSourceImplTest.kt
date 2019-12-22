package com.ch.yoon.imagesearch.data.remote.kakao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.remote.kakao.request.ImageSortType
import com.ch.yoon.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.remote.kakao.response.KakaoImageSearchMetaInfo
import com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.data.model.error.RepositoryException
import com.ch.yoon.data.model.image.ImageDocument
import com.ch.yoon.data.model.image.ImageSearchMeta
import com.ch.yoon.data.model.image.ImageSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class ImageRemoteDataSourceImplTest : BaseRxTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockKakaoSearchApi: com.ch.yoon.remote.kakao.KakaoSearchApi

    private lateinit var imageRemoteDataSource: ImageRemoteDataSource

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        imageRemoteDataSource = com.ch.yoon.remote.kakao.ImageRemoteDataSourceImpl(mockKakaoSearchApi)
    }

    override fun after() {
    }

    @Test
    fun `서버로부터 수신한 데이터를 반환하는지 테스트`() {
        // given
        val response = createKakaoImageSearchResponse(3)
        every {
            mockKakaoSearchApi.searchImageList(any(), any(), any(), any())
        } returns Single.just(response)

        // when
        var actualResponse: com.ch.yoon.data.model.image.ImageSearchResponse? = null
        imageRemoteDataSource.getImages(createEmptyImageSearchRequest())
            .subscribe({
                actualResponse = it
            }, {
                actualResponse = null
            })
            .register()

        // then
        val expected = fromKakaiImageSearchResponse(response)
        assertEquals(expected, actualResponse)
    }

    @Test
    fun `이미지 요청 후 에러가 발생했을 때, Repository Exception으로 변환하는지 테스트`() {
        // given
        every {
            mockKakaoSearchApi.searchImageList(any(), any(), any(), any())
        } returns Single.error(Exception())

        // when && then
        var throwableCount = 0
        imageRemoteDataSource.getImages(createEmptyImageSearchRequest())
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

    private fun createKakaoImageSearchResponse(imageDocumentSize: Int): com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse {
        val meta = com.ch.yoon.remote.kakao.response.KakaoImageSearchMetaInfo(0, 0, false)
        val list = mutableListOf<com.ch.yoon.remote.kakao.response.KakaoImageDocument>().apply {
            for(i in 0 until imageDocumentSize) {
                add(createKakaoImageDocument(i.toString()))
            }
        }
        return com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse(meta, list)
    }

    private fun createKakaoImageDocument(id: String): com.ch.yoon.remote.kakao.response.KakaoImageDocument {
        return com.ch.yoon.remote.kakao.response.KakaoImageDocument(
            "collection$id",
            "thumbnailUrl$id",
            "imageUrl$id",
            0,
            0,
            "displaySiteName$id",
            "docUrl$id",
            "dateTime$id"
        )
    }

    private fun createEmptyImageSearchRequest(): com.ch.yoon.remote.kakao.request.ImageSearchRequest {
        return com.ch.yoon.remote.kakao.request.ImageSearchRequest("", com.ch.yoon.remote.kakao.request.ImageSortType.ACCURACY, 1, 1, false)
    }

    private fun fromKakaiImageSearchResponse(response: com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse): com.ch.yoon.data.model.image.ImageSearchResponse {
        return response.run {
            val imageSearchMeta = kakaoImageSearchMeta.run {
                com.ch.yoon.data.model.image.ImageSearchMeta(isEnd)
            }

            val imageDocumentList = kakaoImageDocuments.map {
                com.ch.yoon.data.model.image.ImageDocument(
                    "${it.imageUrl}&${it.docUrl}",
                    it.collection,
                    it.thumbnailUrl,
                    it.imageUrl,
                    it.width,
                    it.height,
                    it.displaySiteName,
                    it.docUrl,
                    it.dateTime,
                    false
                )
            }

            com.ch.yoon.data.model.image.ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }
}