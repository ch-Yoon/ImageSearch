package com.ch.yoon.kakao.pay.imagesearch.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class BaseViewModel2 extends AndroidViewModel {

    public BaseViewModel2(@NonNull Application application) {
        super(application);
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @NonNull
    private final SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    protected void updateMessage(@StringRes final int stringResId) {
        final String message = getApplication().getString(stringResId);
        showMessageLiveEvent.setValue(message);
    }

    protected void registerDisposable(final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @NonNull
    protected String getString(@StringRes final int stringResourceId) {
        return getApplication().getString(stringResourceId);
    }

}
