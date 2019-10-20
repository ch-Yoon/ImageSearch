package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageSearchResponse {

    @Nullable
    @SerializedName("meta")
    private final SearchMetaInfo searchMetaInfo;

    @Nullable
    @SerializedName("documents")
    private final List<ImageDocument> imageDocumentList;

    public ImageSearchResponse(@Nullable SearchMetaInfo searchMetaInfo,
                               @Nullable List<ImageDocument> imageDocumentList) {
        this.searchMetaInfo = searchMetaInfo;
        this.imageDocumentList = imageDocumentList;
    }

    @Nullable
    public SearchMetaInfo getSearchMetaInfo() {
        return searchMetaInfo;
    }

    @NonNull
    public List<ImageDocument> getImageDocumentList() {
        return Optional.ofNullable(imageDocumentList).orElseGet(ArrayList::new);
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchResponse{" +
            "searchMetaInfo=" + searchMetaInfo +
            ", imageDocumentList=" + imageDocumentList +
            '}';
    }

}
