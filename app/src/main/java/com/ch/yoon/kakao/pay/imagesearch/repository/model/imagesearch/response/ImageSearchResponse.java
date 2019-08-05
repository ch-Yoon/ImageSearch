package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

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
    private final Meta meta;

    @Nullable
    @SerializedName("documents")
    private final List<Document> documentList;

    public ImageSearchResponse(@NonNull Meta meta,
                               @NonNull List<Document> documentList) {
        this.meta = meta;
        this.documentList = documentList;
    }

    @Nullable
    public Meta getMeta() {
        return meta;
    }

    @Nullable
    public List<Document> getDocumentList() {
        return documentList;
    }

    @Override
    public String toString() {
        return "ImageSearchResponse{" +
            "meta=" + meta +
            ", documentList=" + documentList +
            '}';
    }

}
