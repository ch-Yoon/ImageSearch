package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageSearchResponse {

    @NonNull
    @SerializedName("meta")
    private final ImageSearchMeta meta;

    @NonNull
    @SerializedName("documents")
    private final List<ImageInfo> imageInfoList;

    public ImageSearchResponse(@NonNull ImageSearchMeta meta,
                               @NonNull List<ImageInfo> imageInfoList) {
        this.meta = meta;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    public ImageSearchMeta getMeta() {
        return meta;
    }

    @NonNull
    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    @Override
    public String toString() {
        return "ImageSearchResponse{" +
            "meta=" + meta +
            ", imageInfoList=" + imageInfoList +
            '}';
    }
}
