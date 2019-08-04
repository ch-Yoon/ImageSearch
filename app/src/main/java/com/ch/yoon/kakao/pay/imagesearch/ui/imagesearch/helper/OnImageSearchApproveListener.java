package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchRequest;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public interface OnImageSearchApproveListener {

    void onImageSearchApprove(@NonNull ImageSearchRequest imageSearchRequest);

}
