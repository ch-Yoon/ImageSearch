package com.ch.yoon.kakao.pay.imagesearch.ui.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }

    protected void registerDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

}
