package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.error

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
enum class ImageSearchError(val errorCode: Int, val errorMessageResId: Int) {

    NO_RESULT_ERROR(400, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_wrong_request),
    AUTHENTICATION_ERROR(401, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_authentication_error),
    PERMISSION_ERROR(403, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_permission_error),
    SYSTEM_ERROR_1(500, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_kakao_server_error),
    SYSTEM_ERROR_2(502, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_kakao_server_error),
    SYSTEM_CHECKING_ERROR(503, com.ch.yoon.kakao.pay.imagesearch.R.string.error_image_search_kakao_server_error),
    NETWORK_NOT_CONNECTING_ERROR(-998, com.ch.yoon.kakao.pay.imagesearch.R.string.error_network_not_connecting_error),
    UNKNOWN_ERROR(-999, com.ch.yoon.kakao.pay.imagesearch.R.string.error_unknown_error);

    companion object {
        fun convertToImageSearchError(errorCode: Int): ImageSearchError {
            for (error in values()) {
                if (error.errorCode == errorCode) {
                    return error
                }
            }

            return UNKNOWN_ERROR
        }
    }
}