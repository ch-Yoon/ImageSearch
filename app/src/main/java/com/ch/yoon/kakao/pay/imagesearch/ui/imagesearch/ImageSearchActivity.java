package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.os.Bundle;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageSearchBinding;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;

public class ImageSearchActivity extends BaseActivity<ActivityImageSearchBinding> {

    private ActivityImageSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_search);

    }

    private void initViewModel() {

    }

}
