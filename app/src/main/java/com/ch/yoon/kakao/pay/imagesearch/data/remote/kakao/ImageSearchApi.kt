package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageSearchApi {

    @GET("search/image.json")
    fun searchImageList(
        @Query(value = "query") query: String,
        @Query(value = "sort") sortType: String,
        @Query(value = "page") pageNumber: Int,
        @Query(value = "size") requiredSize: Int
    ): Single<ImageSearchResponse>

}