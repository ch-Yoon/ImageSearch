package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class RequestMeta {

    private final boolean isLastData;

    @NonNull
    private final String keyword;

    @NonNull
    private final ImageSortType imageSortType;

    public RequestMeta(boolean isLastData,
                       @NonNull String keyword,
                       @NonNull ImageSortType imageSortType) {
        this.isLastData = isLastData;
        this.keyword = keyword;
        this.imageSortType = imageSortType;
    }

    public boolean isLastData() {
        return isLastData;
    }

    @NonNull
    public String getKeyword() {
        return keyword;
    }

    @NonNull
    public ImageSortType getImageSortType() {
        return imageSortType;
    }

    @Override
    public String toString() {
        return "RequestMeta{" +
            "isLastData=" + isLastData +
            ", keyword='" + keyword + '\'' +
            ", imageSortType=" + imageSortType +
            '}';
    }

}
