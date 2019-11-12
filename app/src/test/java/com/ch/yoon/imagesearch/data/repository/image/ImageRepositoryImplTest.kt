package com.ch.yoon.imagesearch.data.repository.image

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
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

    private lateinit var imageRepository: ImageRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = ImageRepositoryImpl(mockImageRemoteDataSource)
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

    private fun emptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, true)
    }

    private fun emptyImageSearchResponse(): ImageSearchResponse {
        val imageSearchMeta = ImageSearchMeta(false)
        val imageDocumentList = mutableListOf<ImageDocument>()
        return ImageSearchResponse(imageSearchMeta, imageDocumentList)
    }
}