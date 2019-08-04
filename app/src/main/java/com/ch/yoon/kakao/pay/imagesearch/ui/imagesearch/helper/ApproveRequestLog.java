package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper;

import androidx.annotation.NonNull;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
final class ApproveRequestLog {

    @NonNull
    private String keyword = "";
    private int dataTotalSize;
    private int pageNumber;

    @NonNull
    String getKeyword() {
        return keyword;
    }

    void setKeyword(@NonNull String keyword) {
        this.keyword = keyword;
    }

    int getDataTotalSize() {
        return dataTotalSize;
    }

    void setDataTotalSize(int dataTotalSize) {
        this.dataTotalSize = dataTotalSize;
    }

    int getPageNumber() {
        return pageNumber;
    }

    void setPageNumber(int pageNumber) {
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
