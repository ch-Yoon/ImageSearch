package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageSearchResponse {

    @Nullable
    @SerializedName("meta")
    private final ImageSearchMeta meta;

    @Nullable
    @SerializedName("documents")
    private final List<ImageInfo> imageInfoList;

    public ImageSearchResponse(@Nullable ImageSearchMeta meta,
                               @Nullable List<ImageInfo> imageInfoList) {
        this.meta = meta;
        this.imageInfoList = imageInfoList;
    }

    @Nullable
    public ImageSearchMeta getMeta() {
        return meta;
    }

    @Nullable
    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchResponse { " + meta + ", " + imageInfoList + "}";
    }
}
