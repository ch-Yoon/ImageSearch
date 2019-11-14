package com.ch.yoon.imagesearch.data.remote.kakao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchMetaInfo
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
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
    lateinit var mockKakaoSearchApi: KakaoSearchApi

    private lateinit var imageRemoteDataSource: ImageRemoteDataSource

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        imageRemoteDataSource = ImageRemoteDataSourceImpl(mockKakaoSearchApi)
    }

    override fun after() {
    }

    @Test
    fun `서버로부터 수신한 데이터를 반환하는지 테스트`() {
        // given
        val response = createKakaoImageSearchResponse(3)
        every { mockKakaoSearchApi.searchImageList(any(), any(), any(), any()) } returns Single.just(response)

        // when
        var actualResponse: ImageSearchResponse? = null
        imageRemoteDataSource.requestImageList(createEmptyImageSearchRequest())
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

    private fun createKakaoImageSearchResponse(imageDocumentSize: Int): KakaoImageSearchResponse {
        val meta = KakaoImageSearchMetaInfo(0, 0, false)
        val list = mutableListOf<KakaoImageDocument>().apply {
            for(i in 0 until imageDocumentSize) {
                add(createKakaoImageDocument(i.toString()))
            }
        }
        return KakaoImageSearchResponse(meta, list)
    }

    private fun createKakaoImageDocument(id: String): KakaoImageDocument {
        return KakaoImageDocument(
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

    private fun createEmptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, false)
    }

    private fun fromKakaiImageSearchResponse(kakaoImageSearchResponse: KakaoImageSearchResponse): ImageSearchResponse {
        return kakaoImageSearchResponse.run {
            val imageSearchMeta = kakaoImageSearchMeta.run {
                ImageSearchMeta(isEnd)
            }

            val imageDocumentList = kakaoImageDocumentList.map {
                ImageDocument(
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

            ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }
}