package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public interface OnImageSearchApproveListener {

    void onImageSearchApprove(@NonNull ImageSearchRequest imageSearchRequest);

}
