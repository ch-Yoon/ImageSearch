package com.ch.yoon.kakao.pay.imagesearch.repository;


import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.ImageInfoConverter;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.RequestMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;


import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Completable;
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

    public Single<SearchLog> updateSearchHistory(@NonNull String keyword) {
        return imageLocalDataSource.updateSearchHistory(keyword)
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<SearchLog>> requestSearchHistory() {
        return imageLocalDataSource.getSearchHistory()
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<ImageSearchResult> requestImageList(@NonNull final ImageSearchRequest request) {
        return Single.concat(
            imageLocalDataSource.getImageSearchList(request)
                .map(imageInfoList -> ImageInfoConverter.toImageSearchResult(request, imageInfoList))
                .subscribeOn(Schedulers.io()),
            imageRemoteDataSource.requestImageList(request)
                .filter(response -> CollectionUtil.isNotEmpty(response.getImageDocumentList()))
                .map(response -> {
                    final List<ImageDocument> documentList = response.getImageDocumentList();
                    final List<LocalImageDocument> localDocumentList = ImageInfoConverter.
                        toLocalImageDocumentList(request, documentList);

                    imageLocalDataSource.saveLocalImageDocumentList(localDocumentList);

                    final SearchMetaInfo metaInfo = response.getSearchMetaInfo();
                    return ImageInfoConverter.toImageSearchResult(request, metaInfo, localDocumentList);
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

    @NonNull
    @Override
    public Completable deleteAllByKeyword(@NonNull String keyword) {
        return imageLocalDataSource.deleteAllByKeyword(keyword)
            .subscribeOn(Schedulers.io());
    }

}

