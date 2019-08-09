package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.SearchHistoryAdapter;
import com.ch.yoon.kakao.pay.imagesearch.utils.BooleanUtil;
import com.ch.yoon.kakao.pay.imagesearch.utils.KeyboardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class SearchBoxBindingAdapter {

    @BindingAdapter("searchLogList")
    public static void setItems(@NonNull RecyclerView recyclerView,
                                @Nullable List<SearchLog> searchLogList) {
        final SearchHistoryAdapter adapter = ((SearchHistoryAdapter)recyclerView.getAdapter());
        if(adapter != null) {
            adapter.submitList(searchLogList == null ? null : new ArrayList<>(searchLogList));
        }
    }

    @BindingAdapter("hideKeyboard")
    public static void hideKeyboard(@NonNull EditText editText, @Nullable Boolean isFocus) {
        if(BooleanUtil.isFalse(isFocus)) {
            KeyboardUtil.hideKeyboard(editText);
        }
    }

}
