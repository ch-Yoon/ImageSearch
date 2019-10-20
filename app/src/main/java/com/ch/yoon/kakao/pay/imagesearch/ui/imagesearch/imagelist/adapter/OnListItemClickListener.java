package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDocument;

/**
 * Creator : ch-yoon
 * Date : 2019-08-04.
 */
public interface OnListItemClickListener {

    void onClick(@NonNull ImageDocument imageDocument, int position);

}
