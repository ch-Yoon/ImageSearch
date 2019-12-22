package com.ch.yoon.domain.repository

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRepository {

    fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

    fun getAllFavoriteImages(): Single<List<ImageDocument>>

    fun saveFavoriteImage(imageDocument: ImageDocument): Completable

    fun deleteFavoriteImage(imageDocument: ImageDocument): Completable

    fun observeChangingFavoriteImage(): Observable<ImageDocument>

}