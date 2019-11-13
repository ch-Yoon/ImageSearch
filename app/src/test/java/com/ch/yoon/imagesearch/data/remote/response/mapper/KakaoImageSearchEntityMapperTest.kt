package com.ch.yoon.imagesearch.data.remote.response.mapper

import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.imagesearch.data.remote.kakao.response.mapper.KakaoImageSearchEntityMapper
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchMetaInfo
import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class KakaoImageSearchEntityMapperTest {

    @Test
    fun `KakaoImageSearchResponse 를 ImageSearchResponse 로 맵핑하는지 테스트`() {
        // given
        val kakaoImaggSearchResponse = createVritualKakaoImageResponse(10)

        // when
        val convertedResponse = KakaoImageSearchEntityMapper.fromEntity(kakaoImaggSearchResponse)

        // then
        val meta = ImageSearchMeta(kakaoImaggSearchResponse.kakaoImageSearchMeta.isEnd)
        val list = kakaoImaggSearchResponse.kakaoImageDocumentList.map {
            ImageDocument(
                "${it.thumbnailUrl}&${it.imageUrl}",
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
        val expectedResponse = ImageSearchResponse(meta, list)
        assertEquals(expectedResponse, convertedResponse)
    }

    private fun createVritualKakaoImageResponse(imageDocumentListSize: Int): KakaoImageSearchResponse {
        val kakaoMeta = KakaoImageSearchMetaInfo(1, 1, true)
        val kakaoList = createVirtualKakaoImageDocumentList(imageDocumentListSize)
        return KakaoImageSearchResponse(kakaoMeta, kakaoList)
    }

    private fun createVirtualKakaoImageDocumentList(size: Int): List<KakaoImageDocument> {
        val kakaoList = mutableListOf<KakaoImageDocument>()
        for(i in 0 until size) {
            kakaoList.add(KakaoImageDocument(
                "collection$i",
                "thumbnailUrl$i",
                "imageUrl$i",
                i,
                i,
                "displaySiteName$i",
                "docUrl$i",
                "dateTime$i")
            )
        }

        return kakaoList
    }
}