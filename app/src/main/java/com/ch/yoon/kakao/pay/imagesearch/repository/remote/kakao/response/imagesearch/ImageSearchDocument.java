package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageSearchDocument {

    @SerializedName("collection")
    private final String collection;

    @SerializedName("thumbnail_url")
    private final String thumbnailUrl;

    @SerializedName("image_url")
    private final String imageUrl;

    @SerializedName("width")
    private final int width;

    @SerializedName("height")
    private final int height;

    @SerializedName("display_sitename")
    private final String displaySiteName;

    @SerializedName("doc_url")
    private final String docUrl;

    @SerializedName("dateTime")
    private final String dateTime;

    public ImageSearchDocument(String collection, String thumbnailUrl, String imageUrl, int width, int height, String displaySiteName, String docUrl, String dateTime) {
        this.collection = collection;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
        this.displaySiteName = displaySiteName;
        this.docUrl = docUrl;
        this.dateTime = dateTime;
    }

    @Nullable
    public String getCollection() {
        return collection;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Nullable
    public String getDisplaySiteName() {
        return displaySiteName;
    }

    @Nullable
    public String getDocUrl() {
        return docUrl;
    }

    @Nullable
    public String getDateTime() {
        return dateTime;
    }

}
