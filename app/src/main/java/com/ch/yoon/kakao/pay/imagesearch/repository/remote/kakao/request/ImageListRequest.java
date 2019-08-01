package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageSortType;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageListRequest {

    @NonNull
    private final String keyword;

    @NonNull
    private final ImageSortType imageSortType;

    private final int pageNumber;
    private final int requiredSize;

    public ImageListRequest(@NonNull String keyword,
                            @NonNull ImageSortType imageSortType,
                            int pageNumber,
                            int requiredSize) {
        this.keyword = keyword;
        this.imageSortType = imageSortType;
        this.pageNumber = pageNumber;
        this.requiredSize = requiredSize;
    }

    @NonNull
    public String getKeyword() {
        return keyword;
    }

    @NonNull
    public ImageSortType getImageSortType() {
        return imageSortType;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getRequiredSize() {
        return requiredSize;
    }

}
