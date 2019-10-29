package com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
class ImageSearchException(
    errorMessage: String,
    val imageSearchError: ImageSearchError
) :  RuntimeException(errorMessage)