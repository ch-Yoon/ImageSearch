package com.ch.yoon.imagesearch.presentation.common.pageload

data class PageLoadConfiguration(
    val startPageNumber: Int,
    val maxPageNumber: Int,
    val requiredDataSize: Int,
    val preloadAllowCount: Int
)