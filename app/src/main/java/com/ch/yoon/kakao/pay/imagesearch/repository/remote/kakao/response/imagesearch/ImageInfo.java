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

    public ImageInfo(@Nullable String thumbnailUrl,
                     @Nullable String imageUrl,
                     int width,
                     int height) {
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
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


    @Override
    public boolean equals(Object o) {
        if(o == null || !getClass().getName().equals(o.getClass().getName())) {
            return false;
        } else {
            ImageInfo target = (ImageInfo) o;
            return Objects.equals(thumbnailUrl, target.thumbnailUrl) &&
                Objects.equals(imageUrl, target.imageUrl) &&
                width == target.width &&
                height == target.height;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageInfo {thumbnailUrl=" + thumbnailUrl + ", imageUrl=" + imageUrl +
            ", width=" + width + ", height=" + height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thumbnailUrl, imageUrl, width, height);
    }

}
