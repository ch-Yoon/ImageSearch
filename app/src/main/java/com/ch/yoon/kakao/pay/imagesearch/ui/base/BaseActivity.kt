package com.ch.yoon.kakao.pay.imagesearch.ui.base

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected fun binding(@LayoutRes layoutId: Int): B {
        return DataBindingUtil.setContentView<B>(this, layoutId).apply {
            lifecycleOwner = this@BaseActivity
        }
    }

    protected fun showToast(message: String?) {
        message?.let { Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show() }
    }

}