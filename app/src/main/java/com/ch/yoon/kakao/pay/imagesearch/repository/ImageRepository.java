package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageRepository {

    @NonNull
    Single<ImageSearchResponse> requestImageList(ImageSearchRequest imageSearchRequest);

//    @NonNull
//    Single<DetailImageInfo> requestImageDetailInfo(@NonNull String id);

    @NonNull
    Completable deleteAllByKeyword(@NonNull String keyword);

    @NonNull
    Single<List<SearchLog>> requestSearchLogList();

    @NonNull
    Single<SearchLog> updateSearchLog(@NonNull String keyword);

}
