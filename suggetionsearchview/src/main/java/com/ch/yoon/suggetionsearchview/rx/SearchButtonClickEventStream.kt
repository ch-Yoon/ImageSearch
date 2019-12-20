package com.ch.yoon.suggetionsearchview.rx

import com.ch.yoon.suggetionsearchview.OnSearchButtonClickListener

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
internal class SearchButtonClickEventStream : RxEventStream<String>(), OnSearchButtonClickListener {

    override fun onClick(inputtedText: String) {
        publishEvent(inputtedText)
    }
}