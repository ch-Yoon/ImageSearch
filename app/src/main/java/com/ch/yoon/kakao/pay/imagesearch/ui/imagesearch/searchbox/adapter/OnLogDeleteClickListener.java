package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;

/**
 * Creator : ch-yoon
 * Date : 2019-08-07.
 */
public interface OnLogDeleteClickListener {

    void onClick(@NonNull SearchLog searchLog, int position);

}
