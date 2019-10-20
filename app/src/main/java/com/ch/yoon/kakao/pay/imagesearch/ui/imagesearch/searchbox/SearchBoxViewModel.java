package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class SearchBoxViewModel extends BaseViewModel {

    private static final String TAG = SearchBoxViewModel.class.getName();

    @NonNull
    private final ImageRepository imageRepository;

    @NonNull
    private final MutableLiveData<List<SearchLog>> searchLogListLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Boolean> searchBoxFocusLiveData = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<String> searchKeywordLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> searchBoxFinishEvent = new SingleLiveEvent<>();

    SearchBoxViewModel(@NonNull Application application,
                       @NonNull ImageRepository imageRepository) {
        super(application);
        this.imageRepository = imageRepository;

        init();
    }

    private void init() {
        searchBoxFocusLiveData.setValue(false);
    }

    @NonNull
    public LiveData<List<SearchLog>> observeSearchLogList() {
        return searchLogListLiveData;
    }

    @NonNull
    public LiveData<Boolean> observeSearchBoxFocus() {
        return searchBoxFocusLiveData;
    }

    @NonNull
    public LiveData<String> observeSearchKeyword() {
        return searchKeywordLiveEvent;
    }

    @NonNull
    public LiveData<Void> observeSearchBoxFinish() {
        return searchBoxFinishEvent;
    }

    public void clickKeywordDeleteButton(@NonNull final String keyword) {
        registerDisposable(
            imageRepository.deleteSearchLog(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        deleteKeywordFromList(keyword);
                        updateMessage(R.string.success_all_image_info_delete_by_keyword);
                    },
                    throwable -> {
                        Log.d(TAG, throwable.getMessage());
                    }
                )
        );
    }

    public void clickBackground() {
        searchBoxFocusLiveData.setValue(false);
    }

    public void clickBackPress() {
        if(isSearchBoxFocus()) {
            searchBoxFocusLiveData.setValue(false);
        } else {
            searchBoxFinishEvent.call();
        }
    }

    public void clickSearchButton(@NonNull final String keyword) {
        if(TextUtils.isEmpty(keyword)) {
            updateMessage(R.string.empty_keyword_guide);
        } else {
            searchKeywordLiveEvent.setValue(keyword);
            searchBoxFocusLiveData.setValue(false);

            registerDisposable(
                imageRepository.insertOrUpdateSearchLog(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::applyReceivedSearchLog,
                        throwable -> Log.d(TAG, throwable.getMessage())
                    )
            );
        }
    }

    public void clickSearchBox() {
        if(notHasSearchLogList()) {
            registerDisposable(
                imageRepository.requestSearchLogList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::updateSearchLogList,
                        throwable -> Log.d(TAG, throwable.getMessage())
                    )
            );
        }

        if(isSearchBoxNotFocus()) {
            searchBoxFocusLiveData.setValue(true);
        }
    }

    private void updateSearchLogList(final List<SearchLog> searchLogList) {
        if(CollectionUtil.isNotEmpty(searchLogList)) {
            Collections.sort(searchLogList);
            searchLogListLiveData.setValue(searchLogList);
            if(isSearchBoxNotFocus()) {
                searchBoxFocusLiveData.setValue(true);
            }
        }
    }

    private void deleteKeywordFromList(final String keyword) {
        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        if(searchLogList != null) {
            if(removeIfExistKeyword(keyword, searchLogList)) {
                searchLogListLiveData.setValue(searchLogList);
            }
        }
    }

    private void applyReceivedSearchLog(final SearchLog newSearchLog) {
        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        if(searchLogList != null) {
            removeIfExistKeyword(newSearchLog.getKeyword(), searchLogList);
            searchLogList.add(0, newSearchLog);
        } else {
            searchLogList = new ArrayList<>();
            searchLogList.add(newSearchLog);
        }

        searchLogListLiveData.setValue(searchLogList);
    }

    private boolean removeIfExistKeyword(final String keyword, final List<SearchLog> searchLogList) {
        for(int i=0; i<searchLogList.size(); i++) {
            SearchLog oldLog = searchLogList.get(i);
            if(oldLog.getKeyword().equals(keyword)) {
                searchLogList.remove(i);
                return true;
            }
        }

        return false;
    }

    private boolean notHasSearchLogList() {
        return !hasSearchLogList();
    }

    private boolean hasSearchLogList() {
        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        return CollectionUtil.isNotEmpty(searchLogList);
    }

    private boolean isSearchBoxNotFocus() {
        return !isSearchBoxFocus();
    }

    private boolean isSearchBoxFocus() {
        Boolean viewVisible = searchBoxFocusLiveData.getValue();
        return viewVisible != null && viewVisible;
    }

}
