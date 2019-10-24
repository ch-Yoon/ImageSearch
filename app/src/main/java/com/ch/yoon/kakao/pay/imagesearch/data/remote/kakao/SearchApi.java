package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao;

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface SearchApi {

    @GET("search/image.json")
    Single<ImageSearchResponse> searchImageList(@Query(value = "query") String query,
                                                @Query(value = "sort") String sortType,
                                                @Query(value = "page") int pageNumber,
                                                @Query(value = "size") int requiredSize);

}
