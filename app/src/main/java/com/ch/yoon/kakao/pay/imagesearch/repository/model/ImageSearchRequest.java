package com.ch.yoon.kakao.pay.imagesearch.repository.model;

import androidx.annotation.NonNull;


/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageSearchRequest {

    @NonNull
    private final String keyword;

    @NonNull
    private final ImageSortType imageSortType;

    private final int pageNumber;
    private final int requiredSize;

    public ImageSearchRequest(@NonNull String keyword,
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

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchRequest {keyword=" + keyword +
            ", imageSortType=" + imageSortType +
            ", pageNumber=" + pageNumber +
            ", requiredSize=" + requiredSize + "}";
    }

}
