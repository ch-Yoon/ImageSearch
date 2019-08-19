package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class ImageSearchResult {

    @NonNull
    private final ResultMeta resultMeta;

    @NonNull
    private final List<SimpleImageInfo> simpleImageInfoList;

    public ImageSearchResult(@NonNull ResultMeta resultMeta,
                             @NonNull List<SimpleImageInfo> simpleImageInfoList) {
        this.resultMeta = resultMeta;
        this.simpleImageInfoList = simpleImageInfoList;
    }

    @NonNull
    public ResultMeta getResultMeta() {
        return resultMeta;
    }

    @NonNull
    public List<SimpleImageInfo> getSimpleImageInfoList() {
        return simpleImageInfoList;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchResult{" +
            "resultMeta=" + resultMeta +
            ", simpleImageInfoList=" + simpleImageInfoList +
            '}';
    }

}
