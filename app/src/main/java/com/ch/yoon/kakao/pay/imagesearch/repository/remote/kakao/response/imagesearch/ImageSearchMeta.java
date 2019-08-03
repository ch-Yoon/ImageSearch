package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageSearchMeta {

    @SerializedName("is_end")
    private final boolean isEnd;

    public ImageSearchMeta(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchMeta {isEnd=" + isEnd + "}";
    }
}
