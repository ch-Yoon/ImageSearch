package com.ch.yoon.kakao.pay.imagesearch.remote.response.imagesearch;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageSearchResponse {

    @SerializedName("meta")
    private ImageSearchMeta meta;

    @SerializedName("imageSearchDocuments")
    private ArrayList<ImageSearchDocument> imageSearchDocuments;

    public ImageSearchResponse(ImageSearchMeta meta, ArrayList<ImageSearchDocument> imageSearchDocuments) {
        this.meta = meta;
        this.imageSearchDocuments = imageSearchDocuments;
    }

    @Nullable
    public ImageSearchMeta getMeta() {
        return meta;
    }

    @Nullable
    public ArrayList<ImageSearchDocument> getImageSearchDocuments() {
        return imageSearchDocuments;
    }

}
