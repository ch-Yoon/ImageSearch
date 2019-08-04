package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageRepository {

    @NonNull
    Single<ImageSearchResponse> requestImageList(ImageSearchRequest imageSearchRequest);

}
