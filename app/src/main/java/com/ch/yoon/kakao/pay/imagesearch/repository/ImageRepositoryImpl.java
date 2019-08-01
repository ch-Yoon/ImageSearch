package com.ch.yoon.kakao.pay.imagesearch.repository;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public Single<ImageSearchResult> requestImageList(ImageListRequest imageListRequest) {
        return imageDataSource.requestImageList(imageListRequest)
            .subscribeOn(Schedulers.io())
            .map(this::convertToImageSearchResult)
            .subscribeOn(Schedulers.io());
    }

    private ImageSearchResult convertToImageSearchResult(ImageSearchResponse imageSearchResponse) {
        ImageSearchMeta meta = imageSearchResponse.getMeta();
        List<ImageSearchDocument> documents = imageSearchResponse.getImageSearchDocuments();

        boolean isEnd = true;
        final List<String> imageUrls = new ArrayList<>();
        if(meta == null || documents == null) {
            return new ImageSearchResult(isEnd, new ArrayList<>());
        } else {
            for (ImageSearchDocument document : documents) {
                String url = document.getImageUrl();
                if (url != null) {
                    imageUrls.add(url);
                }
            }

            if (!imageUrls.isEmpty()) {
                isEnd = meta.isEnd();
            }

            return new ImageSearchResult(isEnd, imageUrls);
        }
    }

}

