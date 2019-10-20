package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import androidx.annotation.NonNull;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public final class ApproveRequestLog {

    @NonNull
    private String keyword = "";
    private int dataTotalSize;
    private int pageNumber;

    @NonNull
    String getKeyword() {
        return keyword;
    }

    void setKeyword(@NonNull final String keyword) {
        this.keyword = keyword;
    }

    int getDataTotalSize() {
        return dataTotalSize;
    }

    void setDataTotalSize(final int dataTotalSize) {
        this.dataTotalSize = dataTotalSize;
    }

    int getPageNumber() {
        return pageNumber;
    }

    void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApproveRequestLog {keyword=" + keyword +
            ", dataTotalSize=" + dataTotalSize +
            ", pageNumber= " + pageNumber + "}";
    }

}
