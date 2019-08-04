package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.error;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.ch.yoon.kakao.pay.imagesearch.R;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public enum ImageSearchError {

    WRONG_REQUEST_ERROR(400, R.string.error_image_search_wrong_request),
    AUTHENTICATION_ERROR(401, R.string.error_image_search_authentication_error),
    PERMISSION_ERROR(403, R.string.error_image_search_permission_error),
    SYSTEM_ERROR_1(500, R.string.error_image_search_kakao_server_error),
    SYSTEM_ERROR_2(502, R.string.error_image_search_kakao_server_error),
    SYSTEM_CHECKING_ERROR(503, R.string.error_image_search_kakao_server_error),
    NETWORK_NOT_CONNECTING_ERROR(-998, R.string.error_network_not_connecting_error),
    UNKNOWN_ERROR(-999, R.string.error_unknown_error);

    private final int errorCode;
    @StringRes
    private final int errorMessageResourceId;

    ImageSearchError(int errorCode, @StringRes int errorMessageResourceId) {
        this.errorCode = errorCode;
        this.errorMessageResourceId = errorMessageResourceId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @StringRes
    public int getErrorMessageResourceId() {
        return errorMessageResourceId;
    }

    @NonNull
    public static ImageSearchError convertToImageSearchError(int errorCode) {
        final ImageSearchError[] errors = values();
        for(ImageSearchError error : errors) {
            if(error.getErrorCode() == errorCode) {
                return error;
            }
        }

        return UNKNOWN_ERROR;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageSearchError {errorCode=" + errorCode +
                ", errorType=" + name();
    }

}