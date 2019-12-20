package com.ch.yoon.suggetionsearchview.rx

import com.ch.yoon.suggetionsearchview.OnStateChangeListener
import com.ch.yoon.suggetionsearchview.SuggestionSearchView

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
internal class StateChangeEventStream : RxEventStream<SuggestionSearchView.State>(), OnStateChangeListener {

    override fun onChange(state: SuggestionSearchView.State) {
        publishEvent(state)
    }
}