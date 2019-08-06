package com.ch.yoon.kakao.pay.imagesearch.repository;


import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.ImageInfoConverter;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;


import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Flowable;
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

    private ImageRepositoryImpl(@NonNull ImageLocalDataSource imageLocalDataSource,
                                @NonNull ImageRemoteDataSource imageRemoteDataSource) {
        this.imageLocalDataSource = imageLocalDataSource;
        this.imageRemoteDataSource = imageRemoteDataSource;
    }

    @NonNull
    public Single<ImageSearchResult> requestImageList(@NonNull ImageSearchRequest imageSearchRequest) {
        return Single.concat(
            imageLocalDataSource
                .getImageSearchList(imageSearchRequest)
                .subscribeOn(Schedulers.io()),
            imageRemoteDataSource
                .requestImageList(imageSearchRequest)
                .filter(response -> CollectionUtil.isNotEmpty(response.getImageDocumentList()))
                .map(response -> {
                    final List<ImageDocument> documentList = response.getImageDocumentList();
                    final List<LocalImageDocument> localDocumentList = ImageInfoConverter.
                        toLocalImageDocumentList(imageSearchRequest, documentList);

                    imageLocalDataSource.saveLocalImageDocumentList(localDocumentList);

                    final SearchMetaInfo metaInfo = response.getSearchMetaInfo();
                    return ImageInfoConverter.toImageSearchResult(imageSearchRequest, metaInfo, localDocumentList);
                })
                .subscribeOn(Schedulers.io())
                .toSingle()
            )
            .filter(imageSearchResult -> CollectionUtil.isNotEmpty(imageSearchResult.getImageInfoList()))
            .onErrorResumeNext(throwable -> {
                if(throwable instanceof NoSuchElementException) {
                    String errorMessage = throwable.getMessage();
                    ImageSearchError imageSearchError = ImageSearchError.NO_RESULT_ERROR;
                    return Flowable.error(new ImageSearchException(errorMessage, imageSearchError));
                }
                return Flowable.error(throwable);
            })
            .firstElement()
            .subscribeOn(Schedulers.io())
            .toSingle();
    }

    @NonNull
    @Override
    public Single<ImageDetailInfo> requestImageDetailInfo(@NonNull String id) {
        return imageLocalDataSource.getImageDetailInfo(id)
            .subscribeOn(Schedulers.io());
    }

}

