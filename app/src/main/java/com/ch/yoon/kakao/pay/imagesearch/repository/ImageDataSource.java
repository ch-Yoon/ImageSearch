package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageDataSource {

    Single<ImageSearchResponse> requestImageList(@NonNull ImageListRequest imageListRequest);

}
