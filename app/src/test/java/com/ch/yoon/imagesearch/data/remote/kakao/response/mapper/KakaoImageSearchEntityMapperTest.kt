package com.ch.yoon.imagesearch.data.remote.kakao.response.mapper

import com.ch.yoon.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.remote.kakao.response.KakaoImageSearchMetaInfo
import com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.data.model.image.ImageDocument
import com.ch.yoon.data.model.image.ImageSearchMeta
import com.ch.yoon.data.model.image.ImageSearchResponse
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
        val convertedResponse = com.ch.yoon.remote.kakao.response.mapper.KakaoImageSearchEntityMapper.fromEntity(kakaoImaggSearchResponse)

        // then
        val meta = com.ch.yoon.data.model.image.ImageSearchMeta(kakaoImaggSearchResponse.kakaoImageSearchMeta.isEnd)
        val list = kakaoImaggSearchResponse.kakaoImageDocuments.map {
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
        val expectedResponse = com.ch.yoon.data.model.image.ImageSearchResponse(meta, list)
        assertEquals(expectedResponse, convertedResponse)
    }

    private fun createVritualKakaoImageResponse(imageDocumentListSize: Int): com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse {
        val kakaoMeta = com.ch.yoon.remote.kakao.response.KakaoImageSearchMetaInfo(1, 1, true)
        val kakaoList = createVirtualKakaoImageDocumentList(imageDocumentListSize)
        return com.ch.yoon.remote.kakao.response.KakaoImageSearchResponse(kakaoMeta, kakaoList)
    }

    private fun createVirtualKakaoImageDocumentList(size: Int): List<com.ch.yoon.remote.kakao.response.KakaoImageDocument> {
        val kakaoList = mutableListOf<com.ch.yoon.remote.kakao.response.KakaoImageDocument>()
        for(i in 0 until size) {
            kakaoList.add(com.ch.yoon.remote.kakao.response.KakaoImageDocument(
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