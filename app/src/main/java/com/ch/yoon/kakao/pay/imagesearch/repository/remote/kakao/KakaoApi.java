package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class KakaoApi implements SearchApi {

    private static final String BASE_URL = "https://dapi.kakao.com/v2/";

    private static SearchApi INSTANCE;

    private KakaoApi() {

    }

    public static synchronized SearchApi getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new KakaoApi();
        }

        return INSTANCE;
    }

}
