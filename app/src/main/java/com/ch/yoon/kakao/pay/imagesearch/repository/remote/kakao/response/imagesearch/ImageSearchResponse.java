package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
    private final List<ImageSearchDocument> imageSearchDocuments;

    public ImageSearchResponse(@Nullable ImageSearchMeta meta,
                               @Nullable List<ImageSearchDocument> imageSearchDocuments) {
        this.meta = meta;
        this.imageSearchDocuments = imageSearchDocuments;
    }

    @Nullable
    public ImageSearchMeta getMeta() {
        return meta;
    }

    @Nullable
    public List<ImageSearchDocument> getImageSearchDocuments() {
        return imageSearchDocuments;
    }

}
