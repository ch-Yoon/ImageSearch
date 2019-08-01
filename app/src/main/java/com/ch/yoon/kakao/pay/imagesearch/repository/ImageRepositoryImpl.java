package com.ch.yoon.kakao.pay.imagesearch.repository;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageRepositoryImpl implements ImageRepository {

    private static ImageRepository INSTANCE;

    private ImageRepositoryImpl() {

    }

    public static synchronized ImageRepository getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ImageRepositoryImpl();
        }

        return INSTANCE;
    }


}
