package com.ch.yoon.kakao.pay.imagesearch.data.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public interface ImageRepository {

    @NonNull
    Single<ImageSearchResult> requestImageList(@NonNull ImageSearchRequest imageSearchRequest);

    @NonNull
    Completable deleteSearchLog(@NonNull String keyword);

    @NonNull
    Single<List<SearchLog>> requestSearchLogList();

    @NonNull
    Single<SearchLog> insertOrUpdateSearchLog(@NonNull String keyword);

}
