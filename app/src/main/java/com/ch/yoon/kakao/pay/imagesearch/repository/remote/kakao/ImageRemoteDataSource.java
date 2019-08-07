package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.BuildConfig;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;

import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageRemoteDataSource {

    private static final String BASE_URL = "https://dapi.kakao.com/v2/";
    private static final String KAKAO_API_KEY = BuildConfig.KAKAO_API_KEY;

    private static ImageRemoteDataSource INSTANCE;

    private final Retrofit retrofit;

    public static synchronized ImageRemoteDataSource getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ImageRemoteDataSource();
        }

        return INSTANCE;
    }

    private ImageRemoteDataSource() {
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if(BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            );
        }

        okHttpClientBuilder.addInterceptor(chain ->
            chain.proceed(chain.request()
                .newBuilder()
                .header("Authorization", KAKAO_API_KEY)
                .build())
        );

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuilder.build());

        retrofit = retrofitBuilder.build();
    }

    @NonNull
    public Single<ImageSearchResponse> requestImageList(@NonNull ImageSearchRequest imageSearchRequest) {
        final String keyword = imageSearchRequest.getKeyword();
        final String sortType = imageSearchRequest.getImageSortType().getType();
        final int pageNumber = imageSearchRequest.getPageNumber();
        final int requiredSize = imageSearchRequest.getRequiredSize();

        return retrofit.create(SearchApi.class)
            .searchImageList(keyword, sortType, pageNumber, requiredSize)
            .onErrorResumeNext(throwable -> {
                final String errorMessage;
                final ImageSearchError imageSearchError;

                if(throwable instanceof HttpException) {
                    final HttpException exception = (HttpException) throwable;
                    errorMessage = exception.message();
                    imageSearchError = ImageSearchError.convertToImageSearchError(exception.code());
                } else if(throwable instanceof UnknownHostException) {
                    errorMessage = throwable.getMessage();
                    imageSearchError = ImageSearchError.NETWORK_NOT_CONNECTING_ERROR;
                } else {
                    errorMessage = throwable.getMessage();
                    imageSearchError = ImageSearchError.UNKNOWN_ERROR;
                }

                return Single.error(new ImageSearchException(errorMessage, imageSearchError));
            });
    }

}
