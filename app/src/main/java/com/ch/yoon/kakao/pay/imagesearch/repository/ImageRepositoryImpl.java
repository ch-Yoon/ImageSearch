package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageRepositoryImpl implements ImageRepository {

    private static ImageRepository INSTANCE;

    @NonNull
    private final ImageDataSource imageDataSource;

    public static synchronized ImageRepository getInstance(@NonNull ImageDataSource imageDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new ImageRepositoryImpl(imageDataSource);
        }

        return INSTANCE;
    }

    private ImageRepositoryImpl(@NonNull ImageDataSource imageDataSource) {
        this.imageDataSource = imageDataSource;
    }

    public Single<ImageSearchResponse> requestImageList(ImageListRequest imageListRequest) {
        return imageDataSource.requestImageList(imageListRequest)
            .subscribeOn(Schedulers.io());
    }

}

