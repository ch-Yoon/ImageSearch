package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageRepository {

    @NonNull
    Single<ImageSearchResult> requestImageList(ImageSearchRequest imageSearchRequest);

    @NonNull
    Single<ImageDetailInfo> requestImageDetailInfo(@NonNull String id);

}
