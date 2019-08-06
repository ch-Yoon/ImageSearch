package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.App;
import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class SearchBoxViewModel extends BaseViewModel {

    @NonNull
    private final ImageRepository imageRepository;

    @NonNull
    private MutableLiveData<List<SearchLog>> searchLogHistoryLiveData = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<Boolean> searchHistoryViewVisibleLiveData = new MutableLiveData<>();
    @NonNull
    private SingleLiveEvent<String> searchKeywordLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private SingleLiveEvent<Void> searchBoxFinishEvent = new SingleLiveEvent<>();
    @NonNull
    private SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();

    @NonNull
    public LiveData<String> observeSearchKeywordEvent() {
        return searchKeywordLiveEvent;
    }

    @NonNull
    public LiveData<List<SearchLog>> observeSearchLogHistory() {
        return searchLogHistoryLiveData;
    }

    @NonNull
    public LiveData<Boolean> observeSearchViewVisible() {
        return searchHistoryViewVisibleLiveData;
    }

    @NonNull
    public LiveData<Void> observeSearchBoxFinishEvent() {
        return searchBoxFinishEvent;
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    public SearchBoxViewModel(@NonNull Application application,
                              @NonNull ImageRepository imageRepository) {
        super(application);
        this.imageRepository = imageRepository;

        init();
    }

    private void init() {
        searchHistoryViewVisibleLiveData.setValue(false);
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
                    throwable -> Log.d("ch-yoon", throwable.getMessage())
                )
        );
    }

    public void clickBackground() {
        searchHistoryViewVisibleLiveData.setValue(false);
    }

    public void clickBackPress() {
        if(isSearchHistoryViewVisible()) {
            searchHistoryViewVisibleLiveData.setValue(false);
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
            if(isSearchHistoryViewVisible()) {
                searchHistoryViewVisibleLiveData.setValue(false);
            }

            registerDisposable(
                imageRepository.updateSearchHistory(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::applyRequestedKeyword,
                        throwable -> Log.d("ch-yoon", throwable.getMessage())
                    )
            );
        }
    }

    public void clickSearchBox() {
        if(notHasSearchHistory()) {
            registerDisposable(
                imageRepository.requestSearchHistory()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::updateSearchHistory,
                        throwable -> Log.d("ch-yoon", throwable.getMessage())
                    )
            );
        } else {
            if(isSearchHistoryViewInvisible()) {
                searchHistoryViewVisibleLiveData.setValue(true);
            }
        }
    }

    private void deleteKeywordFromList(String keyword) {
        List<SearchLog> searchLogList = searchLogHistoryLiveData.getValue();
        if(searchLogList != null) {
            int position = findPosition(keyword, searchLogList);
            if(position != -1) {
                searchLogList.remove(position);
                searchLogHistoryLiveData.setValue(searchLogList);
            }
        }
    }

    private void updateSearchHistory(List<SearchLog> searchLogList) {
        if(CollectionUtil.isNotEmpty(searchLogList)) {
            Collections.sort(searchLogList);
            searchLogHistoryLiveData.setValue(searchLogList);
            if(isSearchHistoryViewInvisible()) {
                searchHistoryViewVisibleLiveData.setValue(true);
            }
        }
    }

    private void applyRequestedKeyword(SearchLog newSearchLog) {
        final String keyword = newSearchLog.getKeyword();
        List<SearchLog> searchLogList = searchLogHistoryLiveData.getValue();
        if(searchLogList != null) {
            int position = findPosition(keyword, searchLogList);
            if(position != -1) {
                searchLogList.remove(position);
            }
            searchLogList.add(0, newSearchLog);
        } else {
            searchLogList = new ArrayList<>();
            searchLogList.add(newSearchLog);
        }
        searchLogHistoryLiveData.setValue(searchLogList);
    }

    private int findPosition(String keyword, List<SearchLog> searchLogList) {
        for(int i=0; i<searchLogList.size(); i++) {
            SearchLog oldLog = searchLogList.get(i);
            if(oldLog.getKeyword().equals(keyword)) {
                return i;
            }
        }

        return -1;
    }

    private boolean hasSearchHistory() {
        List<SearchLog> searchLogList = searchLogHistoryLiveData.getValue();
        return CollectionUtil.isNotEmpty(searchLogList);
    }

    private boolean notHasSearchHistory() {
        return !hasSearchHistory();
    }

    private boolean isSearchHistoryViewInvisible() {
        return !isSearchHistoryViewVisible();
    }

    private boolean isSearchHistoryViewVisible() {
        Boolean viewVisible = searchHistoryViewVisibleLiveData.getValue();
        return viewVisible != null && viewVisible;
    }

}
