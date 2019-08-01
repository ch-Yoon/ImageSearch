package com.ch.yoon.kakao.pay.imagesearch.repository;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageRepository {

    Single<ImageSearchResult> requestImageList(ImageListRequest imageListRequest);

}
