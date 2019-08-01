package com.ch.yoon.kakao.pay.imagesearch.remote.response.imagesearch;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageSearchMeta {

    @SerializedName("total_count")
    private final int totalCount;

    @SerializedName("pageable_count")
    private final int pageableCount;

    @SerializedName("is_end")
    private final boolean isEnd;

    public ImageSearchMeta(int totalCount, int pageableCount, boolean isEnd) {
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
        this.isEnd = isEnd;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageableCount() {
        return pageableCount;
    }

    public boolean isEnd() {
        return isEnd;
    }

}
