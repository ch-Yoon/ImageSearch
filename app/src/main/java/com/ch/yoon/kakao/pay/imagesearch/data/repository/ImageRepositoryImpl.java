package com.ch.yoon.kakao.pay.imagesearch.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageRepositoryImpl implements ImageRepository {

    private static ImageRepository INSTANCE;

    @NonNull
    private final ImageRemoteDataSource imageRemoteDataSource;
    @NonNull
    private final ImageLocalDataSource imageLocalDataSource;

    public static synchronized ImageRepository getInstance(@NonNull ImageLocalDataSource imageLocalDataSource,
                                                           @NonNull ImageRemoteDataSource imageRemoteDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new ImageRepositoryImpl(imageLocalDataSource, imageRemoteDataSource);
        }

        return INSTANCE;
    }

    @VisibleForTesting
    static void destroyInstanceForTesting() {
        INSTANCE = null;
    }

    private ImageRepositoryImpl(@NonNull ImageLocalDataSource imageLocalDataSource,
                                @NonNull ImageRemoteDataSource imageRemoteDataSource) {
        this.imageLocalDataSource = imageLocalDataSource;
        this.imageRemoteDataSource = imageRemoteDataSource;
    }

    @NonNull
    @Override
    public Single<List<SearchLog>> requestSearchLogList() {
        return imageLocalDataSource.selectAllSearchLog()
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<ImageSearchResult> requestImageList(@NonNull final ImageSearchRequest imageSearchRequest) {
        return imageRemoteDataSource.requestImageList(imageSearchRequest)
            .map(imageSearchResponse -> {
                final List<ImageDocument> imageDocumentList = imageSearchResponse.getImageDocumentList();
                final SearchMetaInfo searchMetaInfo = imageSearchResponse.getSearchMetaInfo();
                return new ImageSearchResult(imageSearchRequest, searchMetaInfo, imageDocumentList);
            })
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteSearchLog(@NonNull final String keyword) {
        return imageLocalDataSource.deleteSearchLog(keyword)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<SearchLog> insertOrUpdateSearchLog(@NonNull final String keyword) {
        return imageLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io());
    }

}

