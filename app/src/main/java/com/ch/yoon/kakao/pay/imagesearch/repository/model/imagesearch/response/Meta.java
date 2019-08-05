package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class Meta {

    @SerializedName("is_end")
    private final boolean isEnd;

    public Meta(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @Override
    public String toString() {
        return "Meta{, isEnd=" + isEnd + '}';
    }

}
