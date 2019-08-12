package com.ch.yoon.kakao.pay.imagesearch.repository;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.ImageInfoConverter;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.DetailImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;
import com.ch.yoon.kakao.pay.imagesearch.utils.NetworkUtil;

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
    public Single<SearchLog> updateSearchLog(@NonNull String keyword) {
        return imageLocalDataSource.updateSearchLog(keyword)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<SearchLog>> requestSearchLogList() {
        return imageLocalDataSource.getSearchLogList()
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<ImageSearchResult> requestImageList(@NonNull final ImageSearchRequest request) {
        if(NetworkUtil.isNetworkConnecting()) {
            return imageRemoteDataSource
                .requestImageList(request)
                .map(response -> {
                    final List<ImageDocument> documentList = response.getImageDocumentList();
                    final List<LocalImageDocument> localDocumentList = ImageInfoConverter.
                        toLocalImageDocumentList(request, documentList);

                    imageLocalDataSource.saveLocalImageDocumentList(localDocumentList);

                    final SearchMetaInfo metaInfo = response.getSearchMetaInfo();
                    return ImageInfoConverter.toImageSearchResult(request, metaInfo, localDocumentList);
                })
                .subscribeOn(Schedulers.io());
        } else {
            return imageLocalDataSource
                .getImageSearchList(request)
                .map(simpleImageInfoList -> {
                    if(CollectionUtil.isEmpty(simpleImageInfoList)) {
                        return null;
                    } else {
                        return ImageInfoConverter.toImageSearchResult(request, simpleImageInfoList);
                    }
                })
                .onErrorResumeNext(throwable -> {
                    if(throwable instanceof NullPointerException) {
                        ImageSearchError imageSearchError = ImageSearchError.NETWORK_NOT_CONNECTING_ERROR;
                        String errorMessage = imageSearchError.toString();
                        return Single.error(new ImageSearchException(errorMessage, imageSearchError));
                    } else {
                        String errorMessage = throwable.getMessage();
                        ImageSearchError imageSearchError = ImageSearchError.UNKNOWN_ERROR;
                        return Single.error(new ImageSearchException(errorMessage, imageSearchError));
                    }
                })
                .subscribeOn(Schedulers.io());
        }
    }

    @NonNull
    @Override
    public Single<DetailImageInfo> requestImageDetailInfo(@NonNull String id) {
        return imageLocalDataSource.getImageDetailInfo(id)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteAllByKeyword(@NonNull String keyword) {
        return imageLocalDataSource.deleteAllByKeyword(keyword)
            .subscribeOn(Schedulers.io());
    }

}

