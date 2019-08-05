package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageInfo {

    @NonNull
    @SerializedName("collection")
    private final String collection;

    @NonNull
    @SerializedName("thumbnail_url")
    private final String thumbnailUrl;

    @NonNull
    @SerializedName("image_url")
    private final String imageUrl;

    @SerializedName("width")
    private final int width;

    @SerializedName("height")
    private final int height;

    @NonNull
    @SerializedName("display_sitename")
    private final String displaySiteName;

    @NonNull
    @SerializedName("doc_url")
    private final String docUrl;

    @NonNull
    @SerializedName("dateTime")
    private final String dateTime;

    public ImageInfo(@NonNull String collection,
                     @NonNull String thumbnailUrl,
                     @NonNull String imageUrl,
                     int width,
                     int height,
                     @NonNull String displaySiteName,
                     @NonNull String docUrl,
                     @NonNull String dateTime) {
        this.collection = collection;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
        this.displaySiteName = displaySiteName;
        this.docUrl = docUrl;
        this.dateTime = dateTime;
    }

    @NonNull
    public String getCollection() {
        return collection;
    }

    @NonNull
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NonNull
    public String getDisplaySiteName() {
        return displaySiteName;
    }

    @NonNull
    public String getDocUrl() {
        return docUrl;
    }

    @NonNull
    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return width == imageInfo.width &&
            height == imageInfo.height &&
            collection.equals(imageInfo.collection) &&
            thumbnailUrl.equals(imageInfo.thumbnailUrl) &&
            imageUrl.equals(imageInfo.imageUrl) &&
            displaySiteName.equals(imageInfo.displaySiteName) &&
            docUrl.equals(imageInfo.docUrl) &&
            dateTime.equals(imageInfo.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, thumbnailUrl, imageUrl, width, height, displaySiteName, docUrl, dateTime);
    }

}
