package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.BuildConfig;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageRemoteDataSource implements ImageDataSource {

    private static final String BASE_URL = "https://dapi.kakao.com/v2/";
    private static final String KAKAO_API_KEY = BuildConfig.KAKAO_API_KEY;

    private static ImageDataSource INSTANCE;

    private final Retrofit retrofit;

    public static synchronized ImageDataSource getInstance() {
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

    @Override
    public Single<ImageSearchResponse> requestImageList(@NonNull ImageListRequest imageListRequest) {
        final String keyword = imageListRequest.getKeyword();
        final String sortType = imageListRequest.getImageSortType().getType();
        final int pageNumber = imageListRequest.getPageNumber();
        final int requiredSize = imageListRequest.getRequiredSize();

        return retrofit.create(SearchApi.class).searchImageList(keyword, sortType, pageNumber, requiredSize);
    }

}
