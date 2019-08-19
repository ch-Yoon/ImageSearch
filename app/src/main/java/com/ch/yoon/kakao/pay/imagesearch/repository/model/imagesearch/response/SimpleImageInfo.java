package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class SimpleImageInfo {

    @NonNull
    private final String id;

    @Nullable
    private final String thumbnailUrl;

    public SimpleImageInfo(@NonNull String id, @Nullable String thumbnailUrl) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleImageInfo target = (SimpleImageInfo) o;
        return Objects.equals(id, target.id) &&
            Objects.equals(thumbnailUrl, target.thumbnailUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "SimpleImageInfo{" +
            "id='" + id + '\'' +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            '}';
    }

}
