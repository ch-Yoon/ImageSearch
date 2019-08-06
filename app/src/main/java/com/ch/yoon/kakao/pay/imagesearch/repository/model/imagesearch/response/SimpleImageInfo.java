package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class SimpleImageInfo {

    @Nullable
    private final String id;

    @Nullable
    private final String thumbnailUrl;

    public SimpleImageInfo(@Nullable String id, @Nullable String thumbnailUrl) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleImageInfo simpleImageInfo = (SimpleImageInfo) o;
        return Objects.equals(id, simpleImageInfo.id) &&
            Objects.equals(thumbnailUrl, simpleImageInfo.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, thumbnailUrl);
    }

    @Override
    public String toString() {
        return "SimpleImageInfo{" +
            "id='" + id + '\'' +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            '}';
    }

}
