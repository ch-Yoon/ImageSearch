package com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request;

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
    private final boolean isFirstRequest;

    public ImageSearchRequest(@NonNull String keyword,
                              @NonNull ImageSortType imageSortType,
                              int pageNumber,
                              int requiredSize,
                              boolean isFirstRequest) {
        this.keyword = keyword;
        this.imageSortType = imageSortType;
        this.pageNumber = pageNumber;
        this.requiredSize = requiredSize;
        this.isFirstRequest = isFirstRequest;
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

    public boolean isFirstRequest() {
        return isFirstRequest;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchRequest {keyword=" + keyword +
            ", imageSortType=" + imageSortType +
            ", pageNumber=" + pageNumber +
            ", requiredSize=" + requiredSize + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final ImageSearchRequest target = (ImageSearchRequest) o;
        return pageNumber == target.pageNumber &&
                requiredSize == target.requiredSize &&
                keyword.equals(target.keyword) &&
                imageSortType == target.imageSortType;
    }

}
