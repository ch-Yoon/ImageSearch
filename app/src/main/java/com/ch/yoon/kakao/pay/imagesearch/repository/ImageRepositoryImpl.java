package com.ch.yoon.kakao.pay.imagesearch.repository;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResponse;

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
    public Single<SearchLog> updateSearchLog(@NonNull final String keyword) {
        return imageLocalDataSource.insertOrUpdate(keyword)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<SearchLog>> requestSearchLogList() {
        return imageLocalDataSource.getSearchLogList()
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<ImageSearchResponse> requestImageList(@NonNull final ImageSearchRequest request) {
        return imageRemoteDataSource.requestImageList(request)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteAllByKeyword(@NonNull final String keyword) {
        return imageLocalDataSource.deleteSearchLog(keyword)
            .subscribeOn(Schedulers.io());
    }

}

