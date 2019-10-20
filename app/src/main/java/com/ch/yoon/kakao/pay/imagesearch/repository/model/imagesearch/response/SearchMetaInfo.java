package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class SearchMetaInfo {

    @SerializedName("is_end")
    private final boolean isEnd;

    public SearchMetaInfo(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @NonNull
    @Override
    public String toString() {
        return "SearchMetaInfo{, isEnd=" + isEnd + '}';
    }

}
