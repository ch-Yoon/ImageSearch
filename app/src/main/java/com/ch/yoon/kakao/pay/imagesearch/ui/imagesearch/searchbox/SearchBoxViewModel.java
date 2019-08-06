package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModel;
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
    private MutableLiveData<List<SearchLog>> searchLogListLiveData = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<Boolean> searchBoxFocusData = new MutableLiveData<>();

    @NonNull
    private SingleLiveEvent<String> searchKeywordLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private SingleLiveEvent<Void> searchBoxFinishEvent = new SingleLiveEvent<>();
    @NonNull
    private SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();

    SearchBoxViewModel(@NonNull Application application,
                       @NonNull ImageRepository imageRepository) {
        super(application);
        this.imageRepository = imageRepository;

        init();
    }

    private void init() {
        searchBoxFocusData.setValue(false);
    }

    @NonNull
    public LiveData<List<SearchLog>> observeSearchLogList() {
        return searchLogListLiveData;
    }

    @NonNull
    public LiveData<Boolean> observeSearchBoxFocus() {
        return searchBoxFocusData;
    }

    @NonNull
    public LiveData<String> observeSearchKeyword() {
        return searchKeywordLiveEvent;
    }

    @NonNull
    public LiveData<Void> observeSearchBoxFinish() {
        return searchBoxFinishEvent;
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    public void clickKeywordDeleteButton(@NonNull String keyword) {
        registerDisposable(
            imageRepository.deleteAllByKeyword(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        deleteKeywordFromList(keyword);
                        final String successMessage = getString(R.string.success_all_image_info_delete_by_keyword);
                        showMessageLiveEvent.setValue(successMessage);
                    },
                    throwable -> Log.d(TAG, throwable.getMessage())
                )
        );
    }

    public void clickBackground() {
        searchBoxFocusData.setValue(false);
    }

    public void clickBackPress() {
        if(isSearchBoxFocus()) {
            searchBoxFocusData.setValue(false);
        } else {
            searchBoxFinishEvent.call();
        }
    }

    public void clickSearchButton(@NonNull String keyword) {
        if(TextUtils.isEmpty(keyword)) {
            String emptyKeywordMessage = getString(R.string.empty_keyword_guide);
            showMessageLiveEvent.setValue(emptyKeywordMessage);
        } else {
            searchKeywordLiveEvent.setValue(keyword);
            searchBoxFocusData.setValue(false);

            registerDisposable(
                imageRepository.updateSearchLog(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::applyRequestedKeyword,
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
            searchBoxFocusData.setValue(true);
        }
    }

    private void updateSearchLogList(List<SearchLog> searchLogList) {
        if(CollectionUtil.isNotEmpty(searchLogList)) {
            Collections.sort(searchLogList);
            searchLogListLiveData.setValue(searchLogList);
            if(isSearchBoxNotFocus()) {
                searchBoxFocusData.setValue(true);
            }
        }
    }

    private void deleteKeywordFromList(String keyword) {
        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        if(searchLogList != null) {
            if(removeIfExistKeyword(keyword, searchLogList)) {
                searchLogListLiveData.setValue(searchLogList);
            }
        }
    }

    private void applyRequestedKeyword(SearchLog newSearchLog) {
        final String keyword = newSearchLog.getKeyword();

        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        if(searchLogList != null) {
            removeIfExistKeyword(keyword, searchLogList);
            searchLogList.add(0, newSearchLog);
        } else {
            searchLogList = new ArrayList<>();
            searchLogList.add(newSearchLog);
        }

        searchLogListLiveData.setValue(searchLogList);
    }

    private boolean removeIfExistKeyword(String keyword, List<SearchLog> searchLogList) {
        for(int i=0; i<searchLogList.size(); i++) {
            SearchLog oldLog = searchLogList.get(i);
            if(oldLog.getKeyword().equals(keyword)) {
                searchLogList.remove(i);
                return true;
            }
        }

        return false;
    }

    private boolean hasSearchLogList() {
        List<SearchLog> searchLogList = searchLogListLiveData.getValue();
        return CollectionUtil.isNotEmpty(searchLogList);
    }

    private boolean notHasSearchLogList() {
        return !hasSearchLogList();
    }

    private boolean isSearchBoxNotFocus() {
        return !isSearchBoxFocus();
    }

    private boolean isSearchBoxFocus() {
        Boolean viewVisible = searchBoxFocusData.getValue();
        return viewVisible != null && viewVisible;
    }

}
