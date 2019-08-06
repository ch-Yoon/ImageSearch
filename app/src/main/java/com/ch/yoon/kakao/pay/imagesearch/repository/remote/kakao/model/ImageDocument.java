package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageDocument {

    @Nullable
    @SerializedName("collection")
    private final String collection;

    @Nullable
    @SerializedName("thumbnail_url")
    private final String thumbnailUrl;

    @Nullable
    @SerializedName("image_url")
    private final String imageUrl;

    @SerializedName("width")
    private final int width;

    @SerializedName("height")
    private final int height;

    @Nullable
    @SerializedName("display_sitename")
    private final String displaySiteName;

    @Nullable
    @SerializedName("doc_url")
    private final String docUrl;

    @Nullable
    @SerializedName("dateTime")
    private final String dateTime;

    public ImageDocument(@Nullable String collection,
                         @Nullable String thumbnailUrl,
                         @Nullable String imageUrl,
                         int width,
                         int height,
                         @Nullable String displaySiteName,
                         @Nullable String docUrl,
                         @Nullable String dateTime) {
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

    @Override
    public String toString() {
        return "ImageDocument{" +
            "collection='" + collection + '\'' +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", width=" + width +
            ", height=" + height +
            ", displaySiteName='" + displaySiteName + '\'' +
            ", docUrl='" + docUrl + '\'' +
            ", dateTime='" + dateTime + '\'' +
            '}';
    }

}
