package com.ch.yoon.kakao.pay.imagesearch.repository.model;

import androidx.annotation.NonNull;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public enum ImageSortType {

    ACCURACY("accuracy"),
    RECENCY("recency");

    @NonNull
    private final String type;

    ImageSortType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getType() {
        return type;
    }

}
