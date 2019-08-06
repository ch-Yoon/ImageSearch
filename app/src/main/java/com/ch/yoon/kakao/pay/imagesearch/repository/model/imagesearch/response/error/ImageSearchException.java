package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public final class ImageSearchException extends RuntimeException {

    private final ImageSearchError imageSearchError;

    public ImageSearchException(@Nullable String errorMessage,
                                @NonNull ImageSearchError imageSearchError) {
        super(errorMessage);
        this.imageSearchError = imageSearchError;
    }

    @Override
    public String getMessage() {
        return "Error Code : " + imageSearchError.getErrorCode() +
                ", ErrorType : " + imageSearchError.name() +
                ", Message : " + super.getMessage();
    }

    @NonNull
    public ImageSearchError getImageSearchError() {
        return imageSearchError;
    }

    public int getErrorCode() {
        return imageSearchError.getErrorCode();
    }

    @StringRes
    public int getErrorMessageResourceId() {
        return imageSearchError.getErrorMessageResourceId();
    }

    @Override
    public String toString() {
        return "ImageSearchException{" +
            "imageSearchError=" + imageSearchError +
            '}';
    }

}
