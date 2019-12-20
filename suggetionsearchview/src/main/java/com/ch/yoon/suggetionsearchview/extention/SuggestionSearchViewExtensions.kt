package com.ch.yoon.suggetionsearchview.extention

import com.ch.yoon.suggetionsearchview.SuggestionSearchView
import com.ch.yoon.suggetionsearchview.rx.SearchButtonClickEventStream
import com.ch.yoon.suggetionsearchview.rx.StateChangeEventStream
import com.ch.yoon.suggetionsearchview.rx.TextChangeEventStream

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
fun SuggestionSearchView.searchButtonClicks() {
    return SearchButtonClickEventStream().run {
        onSearchButtonClickListener = this
        events
    }
}

fun SuggestionSearchView.stateChanges() {
    return StateChangeEventStream().run {
        onStateChangeListener = this
        events
    }
}

fun SuggestionSearchView.textChanges() {
    return TextChangeEventStream().run {
        onTextChangeListener = this
        events
    }
}