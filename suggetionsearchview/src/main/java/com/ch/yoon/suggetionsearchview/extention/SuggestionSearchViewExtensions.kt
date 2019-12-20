package com.ch.yoon.suggetionsearchview.extention

import com.ch.yoon.suggetionsearchview.SuggestionSearchView
import com.ch.yoon.suggetionsearchview.rx.SearchButtonClickEventStream
import com.ch.yoon.suggetionsearchview.rx.StateChangeEventStream
import com.ch.yoon.suggetionsearchview.rx.TextChangeEventStream
import io.reactivex.Observable

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
fun SuggestionSearchView.searchButtonClicks(): Observable<String> {
    return SearchButtonClickEventStream().run {
        onSearchButtonClickListener = this
        events
    }
}

fun SuggestionSearchView.stateChanges(): Observable<SuggestionSearchView.State> {
    return StateChangeEventStream().run {
        onStateChangeListener = this
        events
    }
}

fun SuggestionSearchView.textChanges(): Observable<String> {
    return TextChangeEventStream().run {
        onTextChangeListener = this
        events
    }
}