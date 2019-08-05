package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.ImageDocumentConverter;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Document;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Meta;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;


import java.util.ArrayList;
import java.util.List;

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
    public Single<ImageSearchResponse> requestImageList(@NonNull ImageSearchRequest imageSearchRequest) {
        return Single.concat(
            imageLocalDataSource
                .getLocalImageDocumentList(imageSearchRequest)
                .map(localImageDocumentList -> convertToImageSearchResponse(imageSearchRequest, localImageDocumentList))
                .subscribeOn(Schedulers.io()),
            imageRemoteDataSource.requestImageList(imageSearchRequest)
                .doOnSuccess(imageSearchResponse -> saveReceivedResponse(imageSearchResponse, imageSearchRequest))
                .subscribeOn(Schedulers.io())
        ).filter(imageSearchResponse ->
            CollectionUtil.isNotEmpty(imageSearchResponse.getDocumentList())
        ).firstElement().subscribeOn(Schedulers.io())
            .toSingle();
    }

    private ImageSearchResponse convertToImageSearchResponse(ImageSearchRequest imageSearchRequest,
                                                             List<LocalImageDocument> localImageDocumentList) {
        final int requiredDataSize = imageSearchRequest.getRequiredSize();

        Meta meta = new Meta(false);
        List<Document> documentList;
        if(CollectionUtil.isEmpty(localImageDocumentList) || localImageDocumentList.size() < requiredDataSize) {
            documentList = new ArrayList<>();
        } else {
            documentList = ImageDocumentConverter.convertToDocumentList(localImageDocumentList);
        }
        return new ImageSearchResponse(meta, documentList);
    }

    private void saveReceivedResponse(ImageSearchResponse imageSearchResponse,
                                      ImageSearchRequest imageSearchRequest) {
        final List<Document> documentList = imageSearchResponse.getDocumentList();
        if(documentList != null) {
            final String keyword = imageSearchRequest.getKeyword();final int pageNumber = imageSearchRequest.getPageNumber();
            final int requiredDataSize = imageSearchRequest.getRequiredSize();
            final String imageSortType = imageSearchRequest.getImageSortType().getType();

            final int startDocumentNumber = (pageNumber - 1) * requiredDataSize;

            final List<LocalImageDocument> localImageDocumentList = ImageDocumentConverter.convertToLocalImageDocuments(
                keyword,
                startDocumentNumber,
                imageSortType,
                documentList
            );

            imageLocalDataSource.saveLocalImageDocumentList(localImageDocumentList);
        }
    }

}

