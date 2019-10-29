package com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error

import com.ch.yoon.kakao.pay.imagesearch.R

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
enum class ImageSearchError(val errorCode: Int, val errorMessageResId: Int) {

    NO_RESULT_ERROR(400, R.string.error_image_search_wrong_request),
    AUTHENTICATION_ERROR(401, R.string.error_image_search_authentication_error),
    PERMISSION_ERROR(403, R.string.error_image_search_permission_error),
    SYSTEM_ERROR_1(500, R.string.error_image_search_kakao_server_error),
    SYSTEM_ERROR_2(502, R.string.error_image_search_kakao_server_error),
    SYSTEM_CHECKING_ERROR(503, R.string.error_image_search_kakao_server_error),
    NETWORK_NOT_CONNECTING_ERROR(-998, R.string.error_network_not_connecting_error),
    UNKNOWN_ERROR(-999, R.string.error_unknown_error);

}