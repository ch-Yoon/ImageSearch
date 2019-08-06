package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class ImageInfo {

    @Nullable
    private final String id;

    @Nullable
    private final String thumbnailUrl;

    public ImageInfo(@Nullable String id, @Nullable String thumbnailUrl) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return Objects.equals(id, imageInfo.id) &&
            Objects.equals(thumbnailUrl, imageInfo.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, thumbnailUrl);
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

}
