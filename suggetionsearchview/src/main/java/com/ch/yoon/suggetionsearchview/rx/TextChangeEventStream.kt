package com.ch.yoon.suggetionsearchview.rx

import com.ch.yoon.suggetionsearchview.OnTextChangeListener

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
internal class TextChangeEventStream : RxEventStream<String>(), OnTextChangeListener {

    override fun onTextChange(changedText: String) {
        publishEvent(changedText)
    }
}