package com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload

data class PageLoadConfiguration(
    val startPageNumber: Int,
    val maxPageNumber: Int,
    val requiredDataSize: Int,
    val preloadAllowLineMultiple: Int = 1
)