package com.ch.yoon.kakao.pay.imagesearch.ui.base;

import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    protected B binding(@LayoutRes int layoutId) {
        B dataBinding = DataBindingUtil.setContentView(this, layoutId);
        dataBinding.setLifecycleOwner(this);

        return dataBinding;
    }

    protected void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
