package com.ch.yoon.kakao.pay.imagesearch.repository.model;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageSearchResult {

    private final boolean isEnd;

    @NonNull
    private final List<String> imageUrls;

    public ImageSearchResult(boolean isEnd, @NonNull List<String> imageUrls) {
        this.isEnd = isEnd;
        this.imageUrls = imageUrls;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @NonNull
    public List<String> getImageUrls() {
        return imageUrls;
    }
}
